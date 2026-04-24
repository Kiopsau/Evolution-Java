import java.util.concurrent.ThreadLocalRandom;

public class Vector2 {
    private double x; 
    private double y; 

    public Vector2(double x, double y) {
        this.x = x; 
        this.y = y; 
    } 

    public Vector2() {
        this(0.0, 0.0); 
    }

    public double distanceTo(Vector2 other) {
        return Math.hypot(this.x - other.x, this.y - other.y); 
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y); 
    } 

    public Vector2 add(double x, double y) {
        return new Vector2(this.x + x, this.y + y); 
    }

    public Vector2 copy() {
        return new Vector2(this.x, this.y); 
    } 

    public double[] toArray() {
        return new double[]{this.x, this.y}; 
    } 

    public double getX() {
        return this.x; 
    } 

    public double getY() {
        return this.y; 
    } 

    public Vector2 varyLinear (double offset) {
        double newX = this.x + ThreadLocalRandom.current().nextDouble(-offset, offset); 
        double newY = this.y + ThreadLocalRandom.current().nextDouble(-offset, offset); 
        return new Vector2(newX, newY); 
    } 

    public Vector2 varyRadial (double offset) {
        double angle = ThreadLocalRandom.current().nextDouble(0, 2 * Math.PI); 
        double radius = ThreadLocalRandom.current().nextDouble(0, offset); 
        double newX = this.x + radius * Math.cos(angle); 
        double newY = this.y + radius * Math.sin(angle); 
        return new Vector2(newX, newY); 
    }

    @Override 
    public String toString() {
        return String.format("(%.2f, %.2f)", this.x, this.y); 
    }
}
