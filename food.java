import java.util.concurrent.ThreadLocalRandom;

public class food {
    final public Vector2 position; 
    public double energy; 

    public food(Vector2 position, double energy) {
        if (position != null) {
            this.position = position; 
        } else {
            this.position = new Vector2(
                ThreadLocalRandom.current().nextDouble(
                    config.foodPositionUniform[0], 
                    config.foodPositionUniform[1] 
                ), 
                ThreadLocalRandom.current().nextDouble(
                    config.foodPositionUniform[0], 
                    config.foodPositionUniform[1] 
                )
            ); 
        }

        this.energy = ThreadLocalRandom.current().nextDouble(
            config.foodEnergyUniform[0], 
            config.foodEnergyUniform[1]
        ) * energy; 
    } 

    public food() {
        this(null, 40); 
    } 

    public food(Vector2 position) {
        this(position, 40); 
    }

    public food(double energy) {
        this(null, energy); 
    } 


    @Override
    public String toString() {
        return String.format("food(position=(%.2f, %.2f), energy=%.2f)", position.getX(), position.getY(), energy);
    }
}
