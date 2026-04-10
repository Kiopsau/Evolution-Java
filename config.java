public class config { 
    public static final double[] speedUniform = {0.5, 2.0}; 
    public static final double[] visionUniform = {0.5, 20.0}; 
    public static final double[] sizeUniform = {0.5, 2.0}; 
    public static final double[] fovUniform = {0.5, 2.5}; 

    public static final double[] worldSize = {500, 500};

    public static final double[] creaturePositionUniform = {0, worldSize[0]}; 

    public static final double[] foodPositionUniform = {0, worldSize[0]}; 
    public static final double[] foodEnergyUniform = {0.9, 1.2}; 

    public static final double minEnergyLoss = 0.1; 

    public static final double[] diseaseEnergyLossUniform = {10, 40}; 

    public static final double[] startingEnergyUniform = {0.9, 1.1};

    public static final double[] metabolismUniform = {0.6, 1.2}; 

    public static final double[] evolutionUniform = {0.75, 1.25};

    public static final int numCreatures = 200; 
    public static final int numFood = 200; 

    public static final int reproductionCost = 80; 

    public static final int numSteps = 100000000; //00

    public static final long lifeExpectancy = 365 * 80; 
    public static final double[] lifeExpectancyUniform = {0.8, 1.2}; 

    public static final int maxNeurons = 20; 
    public static final int maxLayers = 10; 

    public static final double maxTurn = 0.5; 


    public static long lastTime = System.nanoTime(); 
    public static double deltaTimeMs = 0.0; 

    public static int step = 0; 
}
