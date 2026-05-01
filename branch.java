import java.util.*; 
import java.util.concurrent.ThreadLocalRandom; 

public class branch extends plant {
    public double length = 0.1; 

    public Vector2 position; 
    
    public double maxLength; 
    public double maxFruits; 
    
    public ArrayList<food> fruits = new ArrayList<>(); 

    public double angle; 

    public String type; 

    public branch(Vector2 position) {
        this.type = super.type; 


        //tree branch 
        if (this.type.equals("tree")) {
            this.maxLength = ThreadLocalRandom.current().nextDouble(
                config.treeBranchLengthUniform[0], 
                config.treeBranchLengthUniform[1]
            ) * super.maxSize; 

            this.maxFruits = ThreadLocalRandom.current().nextDouble(
                config.fruitsPerBranchLength[0], 
                config.fruitsPerBranchLength[1]
            ); 
        }


        //bush branch 
        else if (type.equals("bush")) {
            this.maxLength = ThreadLocalRandom.current().nextDouble(
                config.bushBranchLengthUniform[0], 
                config.bushBranchLengthUniform[1]
            ) * super.maxSize; 

            this.maxFruits = ThreadLocalRandom.current().nextDouble(
                config.fruitsPerBranchLength[0], 
                config.fruitsPerBranchLength[1]
            ); 
        }
        
        this.angle = Math.toRadians(ThreadLocalRandom.current().nextDouble(0, 360)); 

        this.position = position; 
    }

    public void growFruit() {
        if (fruits.size() < length * maxFruits) {

            // thickness should match your rendering
            double halfWidth = length / 20;

            // pick a point ALONG the branch (not centered!)
            double t = Math.pow(ThreadLocalRandom.current().nextDouble(), 0.5) * length;

            // pick offset perpendicular to branch
            double offset = ThreadLocalRandom.current().nextDouble(-halfWidth, halfWidth);

            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            // direction vector (along branch)
            double dx = cos;
            double dy = sin;

            // perpendicular vector
            double px = -sin;
            double py = cos;

            // final position
            double spawnX = position.getX() + dx * t + px * offset;
            double spawnY = position.getY() + dy * t + py * offset;

            food f = new food(new Vector2(spawnX, spawnY));
            fruits.add(f);
        }
    }

    public void grow() {
        double targetLength = maxLength * super.size;

        double growth = ThreadLocalRandom.current().nextDouble(0, config.maxBranchGrowthPercentage); 

        length += (targetLength - length) * growth; 

        if (length > targetLength) {
            length = targetLength;
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
