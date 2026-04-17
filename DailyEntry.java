import java.util.ArrayList;

public class DailyEntry {
    // Attributes
    private double dailyCalories;
    private double proteinGoal;
    private ArrayList<Exercise> workoutList;

    // Constructor
    public DailyEntry() {
        this.workoutList = new ArrayList<>();
        this.dailyCalories = 0;
        this.proteinGoal = 0;
    }

    // Method to add a lift to the list
    public void addExercise(Exercise lift) {
        workoutList.add(lift);
    }

    // Getters and Setters
    public void setDailyCalories(double calories) { this.dailyCalories = calories; }
    public double getDailyCalories() { return dailyCalories; }
}