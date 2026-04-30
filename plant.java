import java.util.*; 
import java.util.concurrent.ThreadLocalRandom;

public class plant {
    public Vector2 position; 

    public ArrayList<branch> branches = new ArrayList<>(); 

    public boolean isCollidable; 
    public double maxSize; 
    public double size; 
    public String type; 

    public plant(Vector2 position, Double maxSize, String type) {
        this.type = (type != null) ? type : "bush"; 

        this.position = (position != null) ? position : new Vector2(
            Math.random() * config.plantPositionUniform[1], 
            Math.random() * config.plantPositionUniform[1]
        ); 

        if (type.equals("tree")) {
            this.maxSize = (maxSize != null) ? maxSize : ThreadLocalRandom.current().nextDouble(
                config.treeSizeUniform[0], 
                config.treeSizeUniform[1]
            ); 

            this.isCollidable = false; 
        } 
        this.size = 1; 
    } 

    public plant() {
        this(null, null, "tree"); 
    } 

    public void growBranch() {
        if (branches.size() < ThreadLocalRandom.current().nextInt(
            config.scalarMaxBranchUnifrom[0], 
            config.scalarMaxBranchUnifrom[1]
        ) * size / 2) {
            branches.add(new branch(position)); 
        } 
    } 

    public void update() {
        for (branch b : branches) {
            b.update(); 
        }
        
        if (Math.random() < config.plantBranchGrowthChance) {
            growBranch(); 
        } 

        grow(); 
    } 

    public void grow() {
        if (size < maxSize) {
            size = Math.min(size + (maxSize * ThreadLocalRandom.current().nextDouble(0, config.maxTreeGrowthPercentage)), maxSize); 
        } 
    }
}
