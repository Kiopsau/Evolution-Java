import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class world {

    public double width;
    public double height;

    public List<creature> creatures;
    public static List<food> foods; 
    public List<plant> plants; 

    // Constructor (default args equivalent)
    public world(double width, double height) {
        this.width = width;
        this.height = height;

        this.creatures = new ArrayList<>();
        this.foods = new ArrayList<>(); 
        this.plants = new ArrayList<>(); 
    }

    // Default constructor
    public world() {
        this(config.creaturePositionUniform[1],
             config.creaturePositionUniform[1]);
    }

    // Add methods
    public void addCreature(creature creature) {
        creatures.add(creature);
    }

    public void addFood(food food) {
        foods.add(food);
    }

    public void addPlant(plant plant) {
        plants.add(plant); 
    }

    // -------------------------
    // Update loop
    // -------------------------
    public Object[] update() {

        List<creature> bornCreatures = new ArrayList<>();
        Map<creature, String> causes = new HashMap<>();

        // Update creatures
        for (creature creature : new ArrayList<>(creatures)) {
            Object[] result = creature.update(this);

            creature child = (creature) result[0];
            String cause = (String) result[1];

            if (creature.isAlive() && child != null) {
                bornCreatures.add(child);
            }

            if (cause != null) {
                causes.put(creature, cause);
            }
        }

        // Collect dead
        List<creature> deadCreatures = new ArrayList<>();
        for (creature c : creatures) {
            if (!c.isAlive()) {
                deadCreatures.add(c);
            }
        }

        // Remove dead
        creatures.removeAll(deadCreatures);

        // Add newborns
        creatures.addAll(bornCreatures);

        



        //dynamic food spawning 
        int baseSpawn = Math.max(5, 10 - creatures.size() / 50);
        int creaturePenalty = creatures.size() / 50;

        int spawnAmount = Math.max(
                Math.max(1, creatures.size() / 100),
                baseSpawn - creaturePenalty
        );

        for (int i = 0; i < spawnAmount; i++) {
            if (ThreadLocalRandom.current().nextDouble() < 0.5) {
                addFood(new food());
            }
        }

        return new Object[]{deadCreatures, causes, bornCreatures};
    }
}