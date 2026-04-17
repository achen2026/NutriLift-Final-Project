import javax.swing.*;
import java.awt.*;

public class Nutrition {
    public static void main(String[] args) {
        JFrame frame = new JFrame("NutriLift: Fitness & Nutrition Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        // Create the Tabbed System
        JTabbedPane tabs = new JTabbedPane();

        // 1. Workout Panel
        JPanel workoutPanel = new JPanel();
        workoutPanel.add(new JLabel("Exercise Name:"));
        workoutPanel.add(new JTextField(10));
        workoutPanel.add(new JButton("Save Lift"));

        // 2. Nutrition Panel
        JPanel nutritionPanel = new JPanel();
        nutritionPanel.add(new JLabel("Enter Weight (lbs):"));
        nutritionPanel.add(new JTextField(5));
        nutritionPanel.add(new JButton("Calculate Calories"));

        // Add panels to tabs
        tabs.addTab("Workouts", workoutPanel);
        tabs.addTab("Nutrition", nutritionPanel);

        frame.add(tabs);
        frame.setVisible(true);
    }
}