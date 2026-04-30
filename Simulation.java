import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;

public class Simulation {

    public static void main(String[] args) {
        world world = new world(config.worldSize[0], config.worldSize[1]); 

        // #region Initialize Graphs 
        JFrame frame = new JFrame("Evolution Simulation");
        graphics panel = new graphics(world); 

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true); 

        // JFrame graph1frame = new JFrame("Live Metrics Graph"); 
        // LiveMetricsGraph graph1 = new LiveMetricsGraph(new String[]{"avgSpeed", "avgVision", "avgSize", "avgFOV"}, new Color[]{Color.ORANGE, Color.BLUE, Color.RED, Color.GREEN}); 

        // graph1frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // graph1frame.setSize(900, 650);

        // graph1frame.add(graph1);
        // graph1frame.setLocationRelativeTo(null);
        // graph1frame.setVisible(true); 

        // JFrame graph2frame = new JFrame("Live Metrics Graph"); 
        // LiveMetricsGraph graph2 = new LiveMetricsGraph(new String[]{"maxEnergy"}, new Color[]{Color.YELLOW}); 

        // graph2frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // graph2frame.setSize(500, 500);

        // graph2frame.add(graph2); 
        // graph2frame.setLocationRelativeTo(null);
        // graph2frame.setVisible(true); 

        // JFrame graph3frame = new JFrame("Live Metrics Graph"); 
        // LiveMetricsGraph graph3 = new LiveMetricsGraph(new String[]{"avgDeathAge", "avgDeathAgeSinceLast"}, new Color[]{Color.BLACK, Color.CYAN}); 

        // graph3frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // graph3frame.setSize(500, 500);

        // graph3frame.add(graph3);
        // graph3frame.setLocationRelativeTo(null);
        // graph3frame.setVisible(true); 
        //#endregion

        // Spawn initial creatures
        // for (int i = 0; i < config.numCreatures; i++) {
        //     world.addCreature(new creature());
        // } 

        //Spawn initial plants 
        for (int i = 0; i < config.numPlants; i++) {
            world.addPlant(new plant()); 
        }

        // Spawn initial food
        for (int i = 0; i < config.numFood; i++) {
            world.addFood(new food());
        }

        int numDeaths = 0;
        int totalDeathAge = 0; 
        int deathAgeSinceLast = 0; 

        try (FileWriter writer = new FileWriter("result.txt")) { 
            while (config.step < config.numSteps /*&& !world.creatures.isEmpty()*/) {
                if (!panel.paused) {
                    long currentTime = System.nanoTime();
                    config.deltaTimeMs = (currentTime - config.lastTime) / 1_000_000.0; // ms
                    config.lastTime = currentTime; 

                    List<creature> deaths;
                    List<creature> births;
                    // Map<Creature, String> causes;
                    // world.update() should return deaths, causes, births
                    // Adjust according to your Java World.update() signature
                    Object[] result = world.update();
                    deaths = (List<creature>) result[0];
                    births = (List<creature>) result[2];

                    // Log deaths
                    if (!deaths.isEmpty()) {
                        // writer.write("Step " + config.step + " deaths (year " + (config.step / 365) + "):\n");
                        for (creature c : deaths) {
                            String cause = ((Map<creature, String>) result[1]).get(c); // death reason
                            // writer.write(c.name + " died at age " + (c.age / 365) +
                            //         " due to " + cause + "\n");
                            numDeaths++;
                            totalDeathAge += c.age / 365; 
                            deathAgeSinceLast += c.age / 365; 
                        }
                    }

                    // Log births
                    /*if (!births.isEmpty()) {
                        writer.write("Step " + config.step + " births (year " + (config.step / 365) + "):\n");
                        for (creature baby : births) {
                            writer.write(baby.name + " born in year " + (config.step / 365) + "\n");
                        }
                    }*/ 

                    // Stats for alive creatures
                    int aliveCount = world.creatures.size();
                    double avgSpeed = 0, avgVision = 0, avgSize = 0, avgFOV = 0, maxEnergy = 0, avgDeathAge = 0, avgDeathAgeSinceLast = 0; 
                    if (aliveCount > 0) {
                        for (creature c : world.creatures) {
                            avgSpeed += c.dna.speed;
                            avgVision += c.dna.effectiveVision;
                            avgSize += c.dna.size; 
                            avgFOV += c.dna.FOV; 
                            maxEnergy = Math.max(maxEnergy, c.energy); 
                        }
                        avgSpeed /= aliveCount;
                        avgVision /= aliveCount;
                        avgSize /= aliveCount; 
                        avgFOV /= aliveCount; 
                    }

                    // Log summary stats
                    // writer.write(String.format("Step %d: %d alive | AvgSpeed=%.2f AvgVision=%.2f AvgSize=%.2f MaxEnergy=%.2f\n",
                    //         config.step, aliveCount, avgSpeed, avgVision, avgSize, maxEnergy));
                    avgDeathAge = numDeaths > 0 ? (double) totalDeathAge / numDeaths : 0; 
                    avgDeathAgeSinceLast = numDeaths > 0 ? (double) deathAgeSinceLast / numDeaths : 0; 
                    if (config.step % 1000 == 0) {
                        deathAgeSinceLast = 0; 
                        System.out.println("Average death age: " + avgDeathAge); 
                    }

                    config.step++; 
                    //#region Graphing 
                    // graph1.addStep(new double[]{avgSpeed, avgVision, avgSize, avgFOV}); 
                    // graph2.addStep(new double[]{maxEnergy}); 
                    // graph3.addStep(new double[]{avgDeathAge, avgDeathAgeSinceLast}); 
                    //#endregion


                    if (config.step == 200 * 365) {
                        for (int i = 0; i < config.numCreatures; i++) {
                            world.addCreature(new creature());
                        } 
                    }
                } 

                panel.repaint(); 
                
                try {
                    Thread.sleep(0); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (world.creatures.isEmpty()) {
                // writer.write("All creatures died.\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print average death age
        double averageDeathAge = numDeaths > 0 ? (double) totalDeathAge / numDeaths : 0;
        System.out.println("Average death age: " + averageDeathAge); 
    }
}