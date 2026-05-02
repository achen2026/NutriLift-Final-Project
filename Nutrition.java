import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Nutrition extends JFrame {
    private DailyEntry today;
    private JTextField dateField = new JTextField("2026-05-02", 10);
    private JProgressBar calProgress = new JProgressBar();
    private DefaultListModel<String> liftListModel = new DefaultListModel<>();
    
    // Labels for Visual Feedback
    private JLabel calLabel = new JLabel("Calories: 0 / 0");
    private JLabel pLabel = new JLabel("Protein: 0 / 0g");
    private JLabel cLabel = new JLabel("Carbs: 0 / 0g");
    private JLabel fLabel = new JLabel("Fats: 0 / 0g");

    // Profile Inputs
    private JTextField weightF = new JTextField(), heightF = new JTextField(), ageF = new JTextField();

    public Nutrition() {
        today = new DailyEntry(dateField.getText());
        setTitle("NutriLift Pro");
        setSize(850, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // TOP: Date and Reset
        JPanel top = new JPanel();
        top.add(new JLabel("Date:")); top.add(dateField);
        JButton resetBtn = new JButton("New Day");
        top.add(resetBtn);
        add(top, BorderLayout.NORTH);

        // MIDDLE: Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Workouts", createWorkoutPanel());
        tabs.addTab("Nutrition", createNutritionPanel());
        add(tabs, BorderLayout.CENTER);
        
        // BOTTOM: Progress Bar
        calProgress.setStringPainted(true);
        add(calProgress, BorderLayout.SOUTH);

        loadProfile(); // LOAD MEMORY ON STARTUP

        resetBtn.addActionListener(e -> {
            today = new DailyEntry(dateField.getText());
            liftListModel.clear();
            updateUI();
        });
    }

    private JPanel createWorkoutPanel() {
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel in = new JPanel(new GridLayout(6,2));
        JTextField name = new JTextField(), part = new JTextField(), s = new JTextField(), r = new JTextField(), w = new JTextField();
        JButton btn = new JButton("Log Workout");

        in.add(new JLabel("Exercise:")); in.add(name);
        in.add(new JLabel("Body Part:")); in.add(part);
        in.add(new JLabel("Sets:")); in.add(s);
        in.add(new JLabel("Reps:")); in.add(r);
        in.add(new JLabel("Weight:")); in.add(w);
        in.add(new JLabel("")); in.add(btn);

        btn.addActionListener(e -> {
            try {
                WeightLift lift = new WeightLift(name.getText(), part.getText(), Integer.parseInt(s.getText()), Integer.parseInt(r.getText()), Double.parseDouble(w.getText()));
                liftListModel.addElement("[" + today.getDate() + "] " + lift.toString());
                saveData("workout_log.tsv", today.getDate() + "\t" + lift.toString());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Check numbers!"); }
        });
        p.add(in, BorderLayout.NORTH);
        p.add(new JScrollPane(new JList<>(liftListModel)), BorderLayout.CENTER);
        return p;
    }

    private JPanel createNutritionPanel() {
        JPanel main = new JPanel(new GridLayout(3, 1));
        
        // Step 1: Goal/Profile
        JPanel p1 = new JPanel(new GridLayout(4,2));
        p1.setBorder(BorderFactory.createTitledBorder("1. User Profile"));
        p1.add(new JLabel("Weight:")); p1.add(weightF);
        p1.add(new JLabel("Height:")); p1.add(heightF);
        p1.add(new JLabel("Age:")); p1.add(ageF);
        JButton saveP = new JButton("Save & Set Goals");
        p1.add(new JLabel("")); p1.add(saveP);

        // Step 2: Food Inputs
        JPanel p2 = new JPanel(new GridLayout(4,2));
        p2.setBorder(BorderFactory.createTitledBorder("2. Log Food"));
        JTextField protIn = new JTextField(), carbIn = new JTextField(), fatIn = new JTextField();
        p2.add(new JLabel("Protein (g):")); p2.add(protIn);
        p2.add(new JLabel("Carbs (g):")); p2.add(carbIn);
        p2.add(new JLabel("Fats (g):")); p2.add(fatIn);
        JButton addF = new JButton("Add Food");
        p2.add(new JLabel("")); p2.add(addF);

        // Step 3: Summary
        JPanel p3 = new JPanel(new GridLayout(4,1));
        p3.setBorder(BorderFactory.createTitledBorder("3. Daily Summary"));
        p3.add(calLabel); p3.add(pLabel); p3.add(cLabel); p3.add(fLabel);

        saveP.addActionListener(e -> {
            try {
                double w = Double.parseDouble(weightF.getText());
                double h = Double.parseDouble(heightF.getText());
                int a = Integer.parseInt(ageF.getText());
                saveProfile(w, h, a); // MEMORY SAVING
                double bmr = (10 * (w*0.45)) + (6.25 * (h*2.5)) - (5 * a) + 5;
                today.setCalorieGoal(bmr);
                updateUI();
            } catch (Exception ex) { }
        });

        addF.addActionListener(e -> {
            try {
                double pr = Double.parseDouble(protIn.getText());
                double cr = Double.parseDouble(carbIn.getText());
                double ft = Double.parseDouble(fatIn.getText());
                today.addFood(pr, cr, ft);
                updateUI(); // VISUAL FEEDBACK TRIGGERS HERE
                saveData("nutrition_log.tsv", today.getDate() + "\tP:" + pr + " C:" + cr + " F:" + ft);
            } catch (Exception ex) { }
        });

        main.add(p1); main.add(p2); main.add(p3);
        return main;
    }

    private void updateUI() {
        calLabel.setText("Calories: " + (int)today.getCaloriesConsumed() + " / " + (int)today.getCalorieGoal());
        calProgress.setMaximum((int)today.getCalorieGoal());
        calProgress.setValue((int)today.getCaloriesConsumed());

        // VISUAL FEEDBACK LOGIC
        pLabel.setText(checkGoal("Protein", today.getPConsumed(), today.getPGoal(), pLabel));
        cLabel.setText(checkGoal("Carbs", today.getCConsumed(), today.getCGoal(), cLabel));
        fLabel.setText(checkGoal("Fats", today.getFConsumed(), today.getFGoal(), fLabel));
    }

    private String checkGoal(String name, double cons, double goal, JLabel label) {
        String text = name + ": " + (int)cons + " / " + (int)goal + "g";
        if (cons >= goal && goal > 0) {
            label.setForeground(new Color(0, 128, 0)); // Turn Green
            return text + " - GOAL REACHED!";
        }
        label.setForeground(Color.BLACK);
        return text;
    }

    private void saveData(String file, String data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) { pw.println(data); } catch (Exception e) {}
    }

    private void saveProfile(double w, double h, int a) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("profile.tsv", false))) { pw.println(w + "\t" + h + "\t" + a); } catch (Exception e) {}
    }

    private void loadProfile() {
        File f = new File("profile.tsv");
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            if (sc.hasNext()) {
                weightF.setText(sc.next()); heightF.setText(sc.next()); ageF.setText(sc.next());
            }
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Nutrition().setVisible(true));
    }
}