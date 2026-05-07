import java.util.concurrent.ThreadLocalRandom;

public class food {
    final public Vector2 position; 
    public double energy; 

    String type; 

    public food(Vector2 position, double energy, String type) {
        this.position = (position != null) ? position : new Vector2(
                ThreadLocalRandom.current().nextDouble(
                    config.foodPositionUniform[0], 
                    config.foodPositionUniform[1] 
                ), 
                ThreadLocalRandom.current().nextDouble(
                    config.foodPositionUniform[0], 
                    config.foodPositionUniform[1] 
                )
            ); 

        this.energy = (energy != 0.0) ? energy : ThreadLocalRandom.current().nextDouble(
            config.foodEnergyUniform[0], 
            config.foodEnergyUniform[1]
        ) * 40; 

        this.type = (type != null) ? type : "bush"; 


    } 

    public food() {
        this(null, 0.0, null); 
    } 

    // public food(Vector2 position) {
    //     this(position, 0.0, "bush"); 
    // }

    // public food(String type) {
    //     this(null, 0.0, type); 
    // }


    @Override
    public String toString() {
        return String.format("food(position=(%.2f, %.2f), energy=%.2f)", position.getX(), position.getY(), energy);
    }
}
