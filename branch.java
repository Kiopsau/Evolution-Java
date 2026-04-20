import java.util.*; 
import java.util.concurrent.ThreadLocalRandom; 

public class branch extends plant {
    public double length = 0.1; 
    public int numFruits = 0; 
    
    public int maxLength; 
    
    public ArrayList<food> fruits = new ArrayList<>(); 

    public branch() {
        this.maxLength = ThreadLocalRandom.current().nextInt(
            config.branchLengthUniform[0], 
            config.branchLengthUniform[1]
        ); 
    }

    public void growFruit() {
        food f = new food(
            super.position.vary(0.05)
        ); 
        fruits.add(f); 
        numFruits++; 
    }

    public void grow() {
        length += (int) (Math.random() * length * config.maxBranchGrowthPercentage); 
        if (length > maxLength) {
            length = maxLength; 
        }
    }

    public void dropFruit() {
        if (numFruits > 0) {
            int i = (int) (Math.random() * numFruits); 
            world.foods.add(fruits.get(i)); 
            fruits.remove(i); 
            numFruits--; 
        }
    }
}
