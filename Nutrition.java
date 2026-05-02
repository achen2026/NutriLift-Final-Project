import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Nutrition extends JFrame {
    private DailyEntry today;
    private JTextField dateField = new JTextField("2026-05-02", 10);
    private JProgressBar calProgress = new JProgressBar();
    private DefaultListModel<String> liftListModel = new DefaultListModel<>();
    
    // Summary Labels
    private JLabel goalDisplayLabel = new JLabel("Target Goal: 0 calories");
    private JLabel consumedLabel = new JLabel("Consumed: 0");
    private JLabel remainingLabel = new JLabel("Remaining: 0");

    // Profile Fields (Now global so loadProfile can access them)
    private JTextField weightF = new JTextField();
    private JTextField heightF = new JTextField();
    private JTextField ageF = new JTextField();

    public Nutrition() {
        today = new DailyEntry(dateField.getText());
        
        setTitle("NutriLift Pro: Performance Tracker");
        setSize(800, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Header
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Current Date:"));
        topPanel.add(dateField);
        JButton resetBtn = new JButton("Reset UI for New Day");
        topPanel.add(resetBtn);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Workouts", createWorkoutPanel());
        tabs.addTab("Nutrition Tracker", createNutritionPanel());
        add(tabs, BorderLayout.CENTER);
        
        // Footer Progress Bar
        calProgress.setStringPainted(true);
        add(calProgress, BorderLayout.SOUTH);

        // Load saved profile data immediately on startup
        loadProfile();

        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Reset screen? (Goal & Profile will be kept)");
            if (confirm == JOptionPane.YES_OPTION) {
                double currentGoal = today.getCalorieGoal();
                today = new DailyEntry(dateField.getText());
                today.setCalorieGoal(currentGoal);
                liftListModel.clear();
                updateUI();
            }
        });
    }

    private JPanel createWorkoutPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Workout Entry"));

        JTextField nameF = new JTextField();
        JTextField partF = new JTextField();
        JTextField setsF = new JTextField();
        JTextField repsF = new JTextField();
        JTextField weightInputF = new JTextField();
        JButton addBtn = new JButton("Log Workout");

        inputPanel.add(new JLabel("Exercise:")); inputPanel.add(nameF);
        inputPanel.add(new JLabel("Body Part:")); inputPanel.add(partF);
        inputPanel.add(new JLabel("Sets:")); inputPanel.add(setsF);
        inputPanel.add(new JLabel("Reps:")); inputPanel.add(repsF);
        inputPanel.add(new JLabel("Weight:")); inputPanel.add(weightInputF);
        inputPanel.add(new JLabel("")); inputPanel.add(addBtn);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JList<>(liftListModel)), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                WeightLift lift = new WeightLift(nameF.getText(), partF.getText(), 
                        Integer.parseInt(setsF.getText()), Integer.parseInt(repsF.getText()), 
                        Double.parseDouble(weightInputF.getText()));
                
                liftListModel.addElement("[" + today.getDate() + "] " + lift.toString());
                saveData("workout_log.tsv", today.getDate() + "\t" + lift.toString() + "\tVolume:" + lift.getVolume());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Input Error."); }
        });
        return panel;
    }

    private JPanel createNutritionPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Goal Logic
        JPanel calcPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        calcPanel.setBorder(BorderFactory.createTitledBorder("Step 1: Set Profile & Goal"));
        JButton bmrBtn = new JButton("Save Profile & Calculate");
        calcPanel.add(new JLabel("Weight (lbs):")); calcPanel.add(weightF);
        calcPanel.add(new JLabel("Height (in):")); calcPanel.add(heightF);
        calcPanel.add(new JLabel("Age:")); calcPanel.add(ageF);
        calcPanel.add(bmrBtn);

        // Food Entry
        JPanel foodPanel = new JPanel(new FlowLayout());
        foodPanel.setBorder(BorderFactory.createTitledBorder("Step 2: Log Food"));
        JTextField foodCals = new JTextField(10);
        JButton addFoodBtn = new JButton("Add Calories");
        foodPanel.add(foodCals); foodPanel.add(addFoodBtn);

        // Summary
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Daily Summary"));
        summaryPanel.add(goalDisplayLabel);
        summaryPanel.add(consumedLabel);
        summaryPanel.add(remainingLabel);

        bmrBtn.addActionListener(e -> {
            try {
                double w = Double.parseDouble(weightF.getText());
                double h = Double.parseDouble(heightF.getText());
                int a = Integer.parseInt(ageF.getText());
                
                // Save profile to file
                saveProfile(w, h, a);
                
                double kg = w * 0.4535;
                double cm = h * 2.54;
                double bmr = (10 * kg) + (6.25 * cm) - (5 * a) + 5;
                today.setCalorieGoal(bmr);
                updateUI();
                JOptionPane.showMessageDialog(this, "Profile Saved and Goal Updated!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Math Error."); }
        });

        addFoodBtn.addActionListener(e -> {
            try {
                double c = Double.parseDouble(foodCals.getText());
                today.addFood(c);
                updateUI();
                saveData("nutrition_log.tsv", today.getDate() + "\tConsumed: " + c);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Enter a number."); }
        });

        mainPanel.add(calcPanel); mainPanel.add(foodPanel); mainPanel.add(summaryPanel);
        return mainPanel;
    }

    private void updateUI() {
        int goal = (int)today.getCalorieGoal();
        int consumed = (int)today.getCaloriesConsumed();
        int remaining = (int)today.getRemaining();

        goalDisplayLabel.setText("Target Goal: " + goal + " calories");
        consumedLabel.setText("Consumed Today: " + consumed);
        remainingLabel.setText("Remaining: " + remaining);
        
        calProgress.setMaximum(goal > 0 ? goal : 100);
        calProgress.setValue(consumed);
        
        if (remaining < 0) {
            remainingLabel.setForeground(Color.RED);
            calProgress.setForeground(Color.RED);
        } else {
            remainingLabel.setForeground(new Color(0, 100, 0));
            calProgress.setForeground(new Color(0, 120, 0));
        }
    }

    private void saveData(String filename, String data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.println(data);
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    // New Profile saving method
    private void saveProfile(double w, double h, int a) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("profile.tsv", false))) { // false = overwrite
            pw.println(w + "\t" + h + "\t" + a);
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    // New Profile loading method
    private void loadProfile() {
        File file = new File("profile.tsv");
        if (!file.exists()) return;

        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNext()) {
                weightF.setText(sc.next());
                heightF.setText(sc.next());
                ageF.setText(sc.next());
                // Trigger calculation if data exists
                double kg = Double.parseDouble(weightF.getText()) * 0.4535;
                double cm = Double.parseDouble(heightF.getText()) * 2.54;
                double bmr = (10 * kg) + (6.25 * cm) - (5 * Integer.parseInt(ageF.getText())) + 5;
                today.setCalorieGoal(bmr);
                updateUI();
            }
        } catch (Exception ex) { System.out.println("No saved profile found."); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Nutrition().setVisible(true));
    }
}