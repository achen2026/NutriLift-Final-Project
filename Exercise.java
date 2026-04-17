public class Exercise {
    // Attributes
    private String exerciseName;
    private int sets;
    private int reps;
    private double weight;

    // Constructor
    public Exercise(String name, int sets, int reps, double weight) {
        this.exerciseName = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    // Getter method for the driver to use
    public String getExerciseName() {
        return exerciseName;
    }

    // Method to calculate volume
    public double calculateVolume() {
        return weight * sets * reps;
    }
}