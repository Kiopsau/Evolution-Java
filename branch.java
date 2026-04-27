import java.util.*; 
import java.util.concurrent.ThreadLocalRandom; 

public class branch extends plant {
    public double length = 0.1; 

    public Vector2 position; 
    
    public int maxLength; 
    public double maxFruits; 
    
    public ArrayList<food> fruits = new ArrayList<>(); 

    public double angle; 

    public branch(Vector2 position) {
        this.maxLength = ThreadLocalRandom.current().nextInt(
            config.branchLengthUniform[0], 
            config.branchLengthUniform[1]
        ); 

        this.maxFruits = ThreadLocalRandom.current().nextDouble(
            config.fruitsPerBranchLength[0], 
            config.fruitsPerBranchLength[1]
        ); 
        
        this.angle = Math.toRadians(ThreadLocalRandom.current().nextDouble(0, 360)); 

        this.position = position; 
    }

    public void growFruit() {
        if (fruits.size() < length * maxFruits) {
            /*food f = new food(
                super.position.varyRadial(10)
            ); */ 

            food f = new food(
                this.position
            ); 

            // int x = (int) p.position.getX();
            //     int y = (int) p.position.getY();
            //     g.fillOval(x - (int) p.size, y - (int) p.size, (int) (p.size * 2), (int) (p.size * 2)); 
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
        if (!this.fruits.isEmpty()) {
            int i = (int) (Math.random() * fruits.size()); 
            world.foods.add(fruits.get(i)); 
            fruits.remove(i); 
        }
    } 


    @Override
    public void update() {
        grow(); 

        if (Math.random() < config.branchFruitGrowthChance) {
            growFruit(); 
        } 

        if (Math.random() < 0.001 && !fruits.isEmpty()) {
            dropFruit(); 
        }
    }




    @Override
    public String toString() {
        return String.format("Branch (length: %.2f, fruits: %d)", length, fruits.size()); 
    }
} 
