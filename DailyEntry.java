import java.util.ArrayList;

/**
 * BRAIN: This class handles all the math for the day.
 */
public class DailyEntry {
    private String date;
    // Calorie tracking
    private double calorieGoal = 0, caloriesConsumed = 0;
    // Macro tracking (Protein, Carbs, Fats)
    private double pGoal, cGoal, fGoal;
    private double pConsumed, cConsumed, fConsumed;

    public DailyEntry(String date) { this.date = date; }

    // ALGORITHM: Calculates sub-goals for macros based on total calories
    public void setCalorieGoal(double goal) { 
        this.calorieGoal = goal; 
        this.pGoal = (goal * 0.30) / 4; // 30% Protein
        this.cGoal = (goal * 0.40) / 4; // 40% Carbs
        this.fGoal = (goal * 0.30) / 9; // 30% Fats
    }

    public void addFood(double p, double c, double f) {
        this.pConsumed += p;
        this.cConsumed += c;
        this.fConsumed += f;
        // Logic: Convert grams back to calories to update total
        this.caloriesConsumed += (p * 4) + (c * 4) + (f * 9);
    }

    // Getters so the GUI can "read" the math results
    public double getCalorieGoal() { return calorieGoal; }
    public double getCaloriesConsumed() { return caloriesConsumed; }
    public double getPGoal() { return pGoal; }
    public double getPConsumed() { return pConsumed; }
    public double getCGoal() { return cGoal; }
    public double getCConsumed() { return cConsumed; }
    public double getFGoal() { return fGoal; }
    public double getFConsumed() { return fConsumed; }
    public String getDate() { return date; }
}