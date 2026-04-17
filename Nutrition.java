import javax.swing.*;

public class Nutrition {
    public static void main(String[] args) {
        // 1. Create the GUI window
        JFrame frame = new JFrame("NutriLift: Fitness & Nutrition Tracker");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2. Instantiate the Exercise object (Requirement)
        Exercise myLift = new Exercise("Bench Press", 3, 10, 135.0);

        // 3. Print test data to the console
        System.out.println("Object created: " + myLift.getExerciseName());
        System.out.println("Total Volume: " + myLift.calculateVolume());

        // 4. Show the window
        frame.setVisible(true);
    }
}