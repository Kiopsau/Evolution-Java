import java.util.concurrent.ThreadLocalRandom;

public class DNA {
    public double speed; 
    public double visionRange; 
    public double size; 
    public double FOV; 
    public double effectiveVision; 

    public DNA (double speed, double visionRange, double size, double FOV) {
        this.speed = speed; 
        this.visionRange = visionRange; 
        this.size = size; 
        this.FOV = FOV; 

        this.effectiveVision = visionRange * FOV; 
    } 

    public static DNA random() {
        return new DNA(
            ThreadLocalRandom.current().nextDouble(
                config.speedUniform[0], 
                config.speedUniform[1]
            ), 
            ThreadLocalRandom.current().nextDouble(
                config.visionUniform[0], 
                config.visionUniform[1]
            ), 
            ThreadLocalRandom.current().nextDouble(
                config.sizeUniform[0], 
                config.sizeUniform[1]
            ), 
            ThreadLocalRandom.current().nextDouble(
                config.fovUniform[0], 
                config.fovUniform[1]
            )
        );
    } 

    public DNA copy() {
        return new DNA(this.speed, this.visionRange, this.size, this.FOV);
    } 

    @Override
    public String toString() {
        return String.format("DNA(speed=%.2f, visionRange=%.2f, size=%.2f, fov = %.2f)", speed, visionRange, size, FOV);
    }
}
