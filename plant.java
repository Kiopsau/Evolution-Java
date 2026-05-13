import java.util.*; 
import java.util.concurrent.ThreadLocalRandom;

public class plant {
    public Vector2 position; 

    public ArrayList<branch> branches = new ArrayList<>(); 

    public boolean isCollidable; 
    public double maxSize; 
    public double size; 
    public String type; 

    public int maxBranches; 

    public int age = 0; 
    public int lifeExpectancy; 

    public boolean isAlive = true; 



    public double fitness; 
    public double nutrition; 
    public double sunlight; 
    public double crowding; 
    public double health = 1.0; 

    public plant(Vector2 position, Double maxSize, String type) {
        this.type = (type != null) ? type : "bush"; 

        this.position = (position != null) ? position : new Vector2(
            Math.random() * config.plantPositionUniform[1], 
            Math.random() * config.plantPositionUniform[1]
        ); 

        this.maxBranches = ThreadLocalRandom.current().nextInt(
            config.scalarMaxBranchUnifrom[0], 
            config.scalarMaxBranchUnifrom[1]
        ); 


        //tree 
        if (this.type.equals("tree")) {
            this.maxSize = (maxSize != null) ? maxSize : ThreadLocalRandom.current().nextDouble(
                config.treeSizeUniform[0], 
                config.treeSizeUniform[1]
            ); 

            this.isCollidable = true; 

            this.lifeExpectancy = (int) (ThreadLocalRandom.current().nextDouble(
                config.treeLifeExpectancy[0], 
                config.treeLifeExpectancy[1]
            ) * 365); 
        } 

        //bush 
        else if (this.type.equals("bush")) {
            this.maxSize = (maxSize != null) ? maxSize : ThreadLocalRandom.current().nextDouble(
                config.bushSizeUniform[0], 
                config.bushSizeUniform[1]
            ); 

            this.isCollidable = false; 

            this.maxBranches *= 5; 

            this.lifeExpectancy = (int) (ThreadLocalRandom.current().nextDouble(
                config.bushLifeExpectancy[0], 
                config.bushLifeExpectancy[1]
            ) * 365); 
        }



        this.size = 1; 
    } 

    public plant(String type) {
        this(null, null, type); 
    } 

    public plant () {
        this(null, null, null); 
    } 

    public void growBranch() {
        if (branches.size() < this.maxBranches * size / 2) {
            branches.add(new branch(position, this.type, this)); 
        } 
    } 




    public void update() {
        if (!isAlive) return; 

        age++; 

        for (branch b : branches) {
            b.update(); 
        } 

        sampleEnvironment(getNearbyPlants()); 

        calculateFitness(); 

        grow(); 

        attemptBranchGrowth(); 

        //check starvation 
        starvationCheck(); 

        //Old age 
        if (age > lifeExpectancy) {
            kill(); 
        }
    } 


    private void sampleEnvironment(List<plant> nearby) {
        int clampedX = Math.max(0, Math.min(world.nutrientMap.length - 1, (int) position.getX())); 
        int clampedY = Math.max(0, Math.min(world.nutrientMap.length - 1, (int) position.getY())); 
        this.nutrition = world.nutrientMap[clampedX][clampedY]; 

        world.nutrientMap[clampedX][clampedY] *= 0.9999; 

        this.crowding = nearby.size(); 

        List<plant> tallers = new ArrayList<>(); 
        
        double nutritionFactor = 1.0; 
        double sunlightFactor = 1.0; 

        for(plant p : nearby) {
            if(p.size > this.size) {
                tallers.add(p); 

                nutritionFactor -= 0.1 * (p.size - this.size) / this.position.distanceTo(p.position); 
                sunlightFactor -= 0.05 * Math.max(0, p.size - this.size) / (this.size + 1); 
            }
        } 

        this.nutrition *= Math.max(0.1, nutritionFactor); 
        this.sunlight = Math.max(0.1, sunlightFactor); 
    } 



    public void calculateFitness() {
        double maintenanceCost = 0.02 * size; 

        fitness = (nutrition * sunlight * health * 10) / (1 + crowding) - maintenanceCost; 
    }



    public void attemptBranchGrowth() {
        int branchCapacity = (int) (maxBranches * fitness * health); 

        if (branches.size() < branchCapacity && Math.random() < fitness * 0.05) {
            growBranch(); 
        }
    } 



    public void starvationCheck() {
        if (fitness < 0.05) {
            health -= 0.001; 
        } else {
            health += 0.0005; 
        }

        health = Math.max(0, Math.min(1, health)); 

        if (health <= 0) {
            kill(); 
        }
    } 



    private List<plant> getNearbyPlants() {
        List<plant> nearby = new ArrayList<>(); 
        for (plant p : world.plants) {
            if (p != this && p.position.distanceTo(this.position) < 10 * (this.size + p.size)) {
                nearby.add(p); 
            }
        }
        return nearby; 
    }



    public void grow() {
        if (size >= maxSize) return;

        if (fitness <= 0) return;

        double growthRate =
            fitness *
            config.maxTreeGrowthPercentage;

        size = Math.min(
            size + growthRate,
            maxSize
        );
    }


    public void kill() {
        this.isAlive = false; 
    }

    public boolean isAlive() {
        return isAlive; 
    }
}
