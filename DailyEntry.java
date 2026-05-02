import java.util.ArrayList;

public class DailyEntry {
    private String date;
    private ArrayList<Exercise> lifts = new ArrayList<>();
    private double calorieGoal = 0;
    private double caloriesConsumed = 0;
    
    // User Stats
    private double weight, height;
    private int age;

    public DailyEntry(String date) {
        this.date = date;
    }

    public void setProfile(double w, double h, int a) {
        this.weight = w;
        this.height = h;
        this.age = a;
    }

    public void setCalorieGoal(double goal) { this.calorieGoal = goal; }
    public void addFood(double calories) { this.caloriesConsumed += calories; }
    
    public double getCaloriesConsumed() { return caloriesConsumed; }
    public double getCalorieGoal() { return calorieGoal; }
    public String getDate() { return date; }
    
    public double getRemaining() {
        return calorieGoal - caloriesConsumed;
    }
}