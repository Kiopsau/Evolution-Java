import java.util.*; 
import java.util.concurrent.ThreadLocalRandom; 

public class branch extends plant {
    public double length = 0.1; 
    
    public int maxLength; 
    public double maxFruits; 
    
    public ArrayList<food> fruits = new ArrayList<>(); 

    public double angle; 

    public branch() {
        this.maxLength = ThreadLocalRandom.current().nextInt(
            config.branchLengthUniform[0], 
            config.branchLengthUniform[1]
        ); 

        this.maxFruits = ThreadLocalRandom.current().nextDouble(
            config.fruitsPerBranchLength[0], 
            config.fruitsPerBranchLength[1]
        ); 
        
        this.angle = Math.toRadians(ThreadLocalRandom.current().nextDouble(0, 360)); 
    }

    public void growFruit() {
        if (fruits.size() < length * maxFruits) {
            /*food f = new food(
                super.position.varyRadial(10)
            ); */ 

            food f = new food(
                new Vector2(
                    super.position.getX() - super.size + Math.random() * 10, 
                    super.position.getY() - super.size + Math.random() * 10
                )
            ); 
            fruits.add(f); 
        }
    }

    public void grow() {
        //length += Math.random() * length * config.maxBranchGrowthPercentage; 
        length += ThreadLocalRandom.current().nextDouble(0, config.maxBranchGrowthPercentage) * length; 
        if (length > maxLength) {
            length = maxLength; 
        }
    }

    public void dropFruit() {
        if (fruits.size() > 0) {
            int i = (int) (Math.random() * fruits.size()); 
            world.foods.add(fruits.get(i)); 
            fruits.remove(i); 
        }
    } 


    public void update() {
        grow(); 

        if (Math.random() < config.branchFruitGrowthChance) {
            growFruit(); 
        } 

        if (Math.random() < 0.001 && fruits.size() > 0) {
            dropFruit(); 
        }
    }




    public String toString() {
        return String.format("Branch (length: %.2f, fruits: %d)", length, fruits.size()); 
    }
} 
