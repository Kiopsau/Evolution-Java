import java.awt.Color;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom; 

public class creature {
    public DNA dna; 
    public Vector2 position; 

    public double energy; 
    private boolean alive; 
    private double metabolism; 
    public int age; 

    public double direction; 

    private int lifespan; 

    public List<String> diseases; 
    public boolean diseased; 

    public String name; 

    private Net brain; 

    public Color color; 

    public creature(DNA dna, Vector2 position, Double energy, String name, Net brain, Double direction, Color color) {
        this.dna = (dna != null) ? dna : DNA.random(); 
        this.brain = (brain != null) ? brain : new Net(); 
        this.color = (color != null) ? color : config.creatureColorOptions[ThreadLocalRandom.current().nextInt(0, config.creatureColorOptions.length)]; 

        this.position = (position != null) ? position : new Vector2(
            ThreadLocalRandom.current().nextDouble(
                config.creaturePositionUniform[0], 
                config.creaturePositionUniform[1]
            ), 
            ThreadLocalRandom.current().nextDouble(
                config.creaturePositionUniform[0], 
                config.creaturePositionUniform[1]
            )
        ); 

        this.direction = (direction != null) ? direction : ThreadLocalRandom.current().nextDouble(0, 2 * Math.PI); 

        this.energy = config.reproductionCost * ThreadLocalRandom.current().nextDouble(
            config.startingEnergyUniform[0], 
            config.startingEnergyUniform[1]
        ) / 2.0; 

        this.alive = true; 
        this.metabolism = ThreadLocalRandom.current().nextDouble(
            config.metabolismUniform[0], 
            config.metabolismUniform[1]
        ); 

        this.age = 0; 

        this.lifespan = (int) (config.lifeExpectancy * ThreadLocalRandom.current().nextDouble(
            config.lifeExpectancyUniform[0], 
            config.lifeExpectancyUniform[1]
        )); 

        this.diseases = new ArrayList<>(); 
        
        this.name = (name != null) ? name : randomName(); 
    } 

    public creature() {
        this(null, null, null, null, null, null, null);
    }



    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    } 

    /*public Object[] update(world world) {
        if (!alive) return new Object[]{null, null};

        food nearestFood = null;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (food food : world.foods) {
            double distance = position.distanceTo(food.position);
            if (distance < nearestDistance && distance <= dna.vision) {
                nearestFood = food;
                nearestDistance = distance;
            }
        }

        double dx, dy;

        if (nearestFood != null) {
            dx = nearestFood.position.getX() - position.getX();
            dy = nearestFood.position.getY() - position.getY();
        } else {
            dx = ThreadLocalRandom.current().nextDouble(-1, 1);
            dy = ThreadLocalRandom.current().nextDouble(-1, 1);
        }

        double length = Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            dx /= length;
            dy /= length;
        }

        dx *= dna.speed;
        dy *= dna.speed;

        position = position.add(dx, dy);

        // Clamp to world bounds
        position = new Vector2(
                Math.max(0, Math.min(world.width, position.getX())),
                Math.max(0, Math.min(world.height, position.getY()))
        );

        // Energy drain
        energy -= 0.1 * dna.speed + 0.01 * metabolism * Math.pow(dna.size, 3);

        // Eating
        if (nearestFood != null && nearestDistance < dna.size) {
            energy += nearestFood.energy;
            world.foods.remove(nearestFood);
        }

        // Aging
        age++;

        String cause = "unknown reasons";
        String disease = diseaseCheck();

        if (disease != null) {
            kill();
            return new Object[]{null, disease};
        }

        if (energy <= 0) {
            cause = "starvation";
            kill();
        } else if (age > lifespan) {
            cause = "old age";
            kill();
        }

        creature child = reproduce();

        return new Object[]{child, cause};
    }*/ 

    /*public Object[] update(world world) {
        this.energy -= this.metabolism * (this.dna.speed + this.dna.size); // Basic cost of living

        // --- 1. SENSE (Gathering Inputs) ---
        // We need to find the closest food to give the brain some information
        food closest = null;
        double minDist = Double.MAX_VALUE;
        for (food f : world.foods) {
            double d = this.position.distanceTo(f.position);
            if (d < minDist) {
                minDist = d;
                closest = f;
            }
        }

        // Normalize inputs between 0 and 1
        double distInput = (closest != null) ? Math.min(minDist / this.dna.vision, 1.0) : 1.0;
        double energyInput = Math.min(this.energy / config.reproductionCost, 1.0);
        
        // Calculate angle to food (0 to 1 range)
        double angleInput = 0.5; 
        if (closest != null) {
            double dx = closest.position.getX() - this.position.getX();
            double dy = closest.position.getY() - this.position.getY();
            angleInput = (Math.atan2(dy, dx) + Math.PI) / (2 * Math.PI);
        }

        double[] inputs = new double[] {
            this.position.getX(), 
            this.position.getY(), 
            this.energy, 
            this.dna.speed, 
            this.dna.vision, 
            this.dna.size, 
            this.metabolism
        };

        // --- 2. THINK (Neural Net Forward Pass) ---
        double[] outputs = this.brain.forward(inputs); //
        double decision = outputs[0]; // Value between 0 and 1

        // --- 3. ACT (Move based on decision) ---
        // Decision 0.0-0.4: Turn Left, 0.4-0.6: Straight, 0.6-1.0: Turn Right
        double moveX = 0;
        double moveY = 0;

        if (decision < 0.4) {
            moveX = -this.dna.speed;
        } else if (decision > 0.6) {
            moveX = this.dna.speed;
        } else {
            moveY = -this.dna.speed; // Default "forward" behavior
        }

        // Update position
        this.position = this.position.add(moveX, moveY);

        // Keep creature inside the world bounds
        position = new Vector2(
                Math.max(0, Math.min(world.width, position.getX())),
                Math.max(0, Math.min(world.height, position.getY()))
        ); 

        // Check for death or reproduction
        // Aging
        this.age++;

        String cause = "unknown reasons";
        String disease = diseaseCheck();

        if (disease != null) {
            kill();
            return new Object[]{null, disease};
        }

        if (energy <= 0) {
            cause = "starvation";
            kill();
        } else if (age > lifespan) {
            cause = "old age";
            kill();
        }

        creature child = reproduce();

        return new Object[]{child, cause};
    }*/ 

    public Object[] update(world world) {
        if (!alive) return new Object[]{null, null};

        // #region 1. ENERGY CONSUMPTION: Base cost + movement cost 
        double mass = 0.5 * Math.pow(dna.size, 3); 

        double basalCost = 0.15 * metabolism * Math.pow(mass, 0.75); 

        double movementCost = 0.01 * Math.pow(mass, 0.5) * Math.pow(dna.speed, 2); 

        double visionCost = 0.1 * dna.effectiveVision; 
        
        double scale = 0.1; 

        energy -= scale * Math.max(config.minEnergyLoss, basalCost + movementCost + visionCost); 
        //energy -= 0.1 * dna.speed + 0.1 * dna.vision + 0.01 * metabolism * Math.pow(mass, 0.75); 

        //#endregion 


        // 2. SENSE: Find the nearest food within vision range
        food nearestFood = null;
        double minFoodDistance = Double.MAX_VALUE;
        double bestFoodAngle = 0.0; 

        for (food f : world.foods) {
            double dist = this.position.distanceTo(f.position); 

            if (dist > dna.size * 2 && dist > this.dna.visionRange) continue; 
            
            double dx = f.position.getX() - this.position.getX(); 
            double dy = f.position.getY() - this.position.getY(); 

            double angleToFood = Math.atan2(dy, dx); 
            
            double relativeAngle = angleToFood - this.direction; 

            relativeAngle = Math.atan2(Math.sin(relativeAngle), Math.cos(relativeAngle)); 

            if (Math.abs(relativeAngle) > this.dna.FOV / 2) continue;

            if (dist < minFoodDistance) {
                minFoodDistance = dist;
                nearestFood = f; 
                bestFoodAngle = relativeAngle; 
            }
        } 

        plant nearestPlant = null;
        double minPlantDistance = Double.MAX_VALUE;
        double bestPlantAngle = 0.0; 

        for (plant p : world.plants) {
            double dist = this.position.distanceTo(p.position); 

            if (dist > dna.size * 2 && dist > this.dna.visionRange) continue; 
            
            double dx = p.position.getX() - this.position.getX(); 
            double dy = p.position.getY() - this.position.getY(); 

            double angleToPlant = Math.atan2(dy, dx); 
            
            double relativeAngle = angleToPlant - this.direction; 

            relativeAngle = Math.atan2(Math.sin(relativeAngle), Math.cos(relativeAngle)); 

            if (Math.abs(relativeAngle) > this.dna.FOV / 2) continue;

            if (dist < minPlantDistance) {
                minPlantDistance = dist;
                nearestPlant = p; 
                bestPlantAngle = relativeAngle; 
            }
        }

        // 3. THINK: Prepare inputs for the Neural Net (Brain)
        // We provide 7 inputs as defined in your Net constructor
        double distFoodInput = (nearestFood != null) ? minFoodDistance / this.dna.effectiveVision : 0.0;
        double angleFoodInput = 0.0; // Default if no food seen 

        if (nearestFood != null) {
            angleFoodInput = bestFoodAngle / Math.PI; 
        }



        double distPlantInput = (nearestPlant != null) ? minPlantDistance / this.dna.effectiveVision : 0.0;
        double anglePlantInput = 0.0; // Default if no plant seen 

        if (nearestPlant != null) {
            anglePlantInput = bestPlantAngle / Math.PI; 
        }



        boolean onBorder = (this.position.getX() == config.worldSize[0] || this.position.getY() == config.worldSize[1]); 

        double[] inputs = new double[] {
            this.position.getX() / world.width, // Current X
            this.position.getY() / world.height, // Current Y
            this.energy / config.reproductionCost, // Current Energy
            distFoodInput,   // Distance to nearest food
            angleFoodInput,  // Angle to nearest food
            distPlantInput,   // Distance to nearest plant
            anglePlantInput,  // Angle to nearest plant 
            this.dna.speed, // Own speed DNA 
            this.dna.effectiveVision, 
            this.dna.size, 
            this.metabolism, 
            (double)age / 1000.0   // Age progress 
        };

        // The brain returns 2 outputs (x and y direction influence)
        double[] brainOutput = this.brain.forward(inputs); 

        // 4. ACT: Combine Innate Nature (Heuristic) with Brain
        double moveX = 0; 
        double moveY = 0; 

        if (nearestFood != null) {
            // INNATE NATURE: Vector pointing directly at food
            moveX = (nearestFood.position.getX() - this.position.getX()) / minFoodDistance;
            moveY = (nearestFood.position.getY() - this.position.getY()) / minFoodDistance;

            // BRAIN INFLUENCE: The brain can "nudge" the innate path
            // (Allows evolution to learn more complex paths/avoidance)
            moveX += (brainOutput[0] - 0.5);
            moveY += (brainOutput[1] - 0.5);
        } else {
            // WANDER: Use brain output to choose a direction when no food is visible
            moveX = Math.cos(brainOutput[0] * 2 * Math.PI);
            moveY = Math.sin(brainOutput[0] * 2 * Math.PI);
        }

        // Apply movement scaled by speed DNA
        this.position = this.position.add(moveX * dna.speed, moveY * dna.speed);

        // Turn itself 
        //double turn = (brainOutput[3] - 0.5) * 2 * config.maxTurn; 
        //this.direction += turn; 
        //this.direction = (this.direction + 2 * Math.PI) % (2 * Math.PI); 
        this.direction = Math.atan2(moveY, moveX); 
        
        // 5. EATING: If we are close enough to the food, consume it
        if (nearestFood != null && minFoodDistance < (dna.size * 2)) {
            this.energy += nearestFood.energy;
            world.foods.remove(nearestFood);
        }

        // 6. LIFE CYCLE: Check for death or reproduction
        position = new Vector2(
                Math.max(0, Math.min(world.width, position.getX())),
                Math.max(0, Math.min(world.height, position.getY()))
        ); 

        // Check for death or reproduction
        // Aging
        this.age++;

        String cause = "unknown reasons";
        String disease = diseaseCheck(); 
        this.diseased = !diseases.isEmpty(); 

        if (disease != null) {
            kill();
            return new Object[]{null, disease};
        }

        if (energy <= 0) {
            cause = "starvation";
            kill();
        } else if (age > lifespan) {
            cause = "old age";
            kill();
        }

        creature child = reproduce(brainOutput[2]);

        return new Object[]{child, cause};
    }

    public creature reproduce(double determinant) {
        if (energy > 120 && determinant < 0.5) {
            energy -= config.reproductionCost;

            DNA babyDNA = dna.copy();

            // Mutation
            babyDNA.speed *= rand(config.evolutionUniform);
            babyDNA.effectiveVision *= rand(config.evolutionUniform);
            babyDNA.size *= rand(config.evolutionUniform);
            
            creature baby; 

            if (Math.random() < 0.85) {
                Color c = this.color; 
                if (Math.random() < 0.01) {
                    c = config.creatureColorOptions[ThreadLocalRandom.current().nextInt(0, config.creatureColorOptions.length)]; 
                }
                if (Math.random() < 0.01) {
                    baby = new creature(babyDNA, position.copy(), config.reproductionCost * ThreadLocalRandom.current().nextDouble(
                        config.startingEnergyUniform[0], 
                        config.startingEnergyUniform[1]
                    ) / 2.0, null, this.brain.mutate(0.5), null, c); 
                } else {
                    baby = new creature(babyDNA, position.copy(), this.energy / (Math.random() * 5 + 10) + config.reproductionCost * ThreadLocalRandom.current().nextDouble(
                        config.startingEnergyUniform[0], 
                        config.startingEnergyUniform[1]
                    ), null, this.brain.mutate(0.1), null, c); 
                }
                return baby;
            }
            
        }
        return null;
    }

    public String diseaseCheck() {
        if (ThreadLocalRandom.current().nextDouble() < 0.001) {

            List<String> keys = new ArrayList<>(Diseases.diseases.keySet());
            int count = ThreadLocalRandom.current().nextInt(1, 4); // 1–3 diseases

            Iterator<String> it = diseases.iterator();

            while (it.hasNext()) {
                String name = it.next();
                Diseases d = Diseases.diseases.get(name);

                if (d == null) continue;

                if (ThreadLocalRandom.current().nextDouble() < d.infectionChance) {
                    if (ThreadLocalRandom.current().nextDouble() < d.fatalityRate) {
                        return name;
                    }
                } else if (!d.chronic) {
                    it.remove(); 
                    this.energy -= ThreadLocalRandom.current().nextDouble(
                        config.diseaseEnergyLossUniform[0], 
                        config.diseaseEnergyLossUniform[1]
                    ); 
                }
            } 


            for (int i = 0; i < count; i++) {
                String name = keys.get(ThreadLocalRandom.current().nextInt(keys.size()));
                Diseases d = Diseases.diseases.get(name); // now returns a Disease object

                if (d == null) continue; // safety check 

                if (diseases.contains(name)) continue; 

                if (ThreadLocalRandom.current().nextDouble() < d.infectionChance) {
                    diseases.add(name); 
                    if (ThreadLocalRandom.current().nextDouble() < d.fatalityRate) {
                        return name; // disease killed the creature
                    }
                }
            } 
        }

        
        /*if (this.position.getX() == config.worldSize[0] || this.position.getY() == config.worldSize[1]) {
            this.kill(); 
            return null; 
        }

        if (this.position.getX() == 0 || this.position.getY() == 0) {
            this.kill(); 
            return null; 
        }*/ 

        return null;
    }







    private static double rand(double[] range) {
        return ThreadLocalRandom.current().nextDouble(
            range[0], 
            range[1]
        ); 
    }

    public static String randomName() {
        try {
            List<String> names = Arrays.asList(
                    new String(java.nio.file.Files.readAllBytes(
                            java.nio.file.Paths.get("first_names.txt")
                    )).split("\\s+")
            ); 
            return names.get(ThreadLocalRandom.current().nextInt(names.size())); 
        } catch (Exception e) {
            return "I F'ED UP BIGTIME IF THIS HAPPENS"; 
        }
    }
}
