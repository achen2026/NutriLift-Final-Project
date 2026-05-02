/**
 * BLUEPRINT: This is the parent class for all exercises.
 */
public abstract class Exercise {
    protected String name;
    protected String bodyPart;

    public Exercise(String name, String bodyPart) {
        this.name = name;
        this.bodyPart = bodyPart;
    }

    public abstract double getVolume(); 
    public abstract String toString();
}

/**
 * CHILD CLASS: This is a specific type of exercise (Weightlifting).
 */
class WeightLift extends Exercise {
    private int sets, reps;
    private double weight;

    public WeightLift(String name, String bodyPart, int sets, int reps, double weight) {
        super(name, bodyPart);
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    @Override
    public double getVolume() {
        return sets * reps * weight; // Logic to see how much total weight was moved
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %d x %d @ %.1f lbs", name, bodyPart, sets, reps, weight);
    }
}