import java.util.*; 

public class plant {
    public Vector2 position; 

    public ArrayList<branch> branches = new ArrayList<>(); 

    public boolean isCollidable; 

    public plant(Vector2 position, Boolean isCollidable) {

        this.position = (position != null) ? position : new Vector2(
            Math.random() * config.plantPositionUniform[1], 
            Math.random() * config.plantPositionUniform[1]
        ); 

        this.isCollidable = (isCollidable != null) ? isCollidable : false; 
    } 

    public plant() {
        this(null, null); 
    }
}
