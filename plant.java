import java.util.*; 
import java.util.concurrent.ThreadLocalRandom;

public class plant {
    public Vector2 position; 

    public ArrayList<branch> branches = new ArrayList<>(); 

    public boolean isCollidable; 
    public double size; 

    public plant(Vector2 position, Boolean isCollidable, Double size) {

        this.position = (position != null) ? position : new Vector2(
            Math.random() * config.plantPositionUniform[1], 
            Math.random() * config.plantPositionUniform[1]
        ); 

        this.size = (size != null) ? size : ThreadLocalRandom.current().nextDouble(
            config.plantSizeUniform[0], 
            config.plantSizeUniform[1]
        ); 

        this.isCollidable = (isCollidable != null) ? isCollidable : false; 
    } 

    public plant() {
        this(null, null, null); 
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
    }
}
