/**
 * Abstract base class for physical activities.
 * Demonstrates Inheritance and Polymorphism.
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
 * Subclass for weightlifting exercises.
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
        return sets * reps * weight;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %d x %d @ %.1f lbs", name, bodyPart, sets, reps, weight);
    }
}