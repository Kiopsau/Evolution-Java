import java.awt.Color;

public class config { 
    public static final double[] speedUniform = {0.5, 2.0}; 
    public static final double[] visionUniform = {0.5, 20.0}; 
    public static final double[] sizeUniform = {0.5, 2.0}; 
    public static final double[] fovUniform = {0.5, 2.5}; 

    public static final double[] worldSize = {500, 500};

    public static final double[] creaturePositionUniform = {0, worldSize[0]}; 
    public static final Color[] creatureColorOptions = {
        Color.RED, 
        Color.GREEN, 
        Color.BLUE, 
        Color.ORANGE, 
        Color.CYAN, 
        Color.MAGENTA, 
        Color.WHITE
    }; 

    public static final double[] foodPositionUniform = {0, worldSize[0]}; 
    public static final double[] foodEnergyUniform = {0.9, 1.2}; 

    public static final double minEnergyLoss = 0.1; 

    public static final double[] diseaseEnergyLossUniform = {10, 40}; 

    public static final double[] startingEnergyUniform = {0.9, 1.1};

    public static final double[] metabolismUniform = {0.6, 1.2}; 

    public static final double[] evolutionUniform = {0.75, 1.25}; 

    public static final int numCreatures = 100; 
    public static final int numFood = 0; 
    public static final int numPlants = 50; 

    public static final int reproductionCost = 80; 

    public static final int numSteps = 1000000000; //00

    public static final long lifeExpectancy = 365 * 80; 
    public static final double[] lifeExpectancyUniform = {0.8, 1.2}; 

    public static final int maxNeurons = 20; 
    public static final int maxLayers = 10; 

    public static final double maxTurn = 0.5; 


    public static long lastTime = System.nanoTime(); 
    public static double deltaTimeMs = 0.0; 

    public static int step = 0; 




    // #region2. PLANTS 
    public static final double[] treeBranchLengthUniform = {1.5, 2.5}; 
    public static final double[] bushBranchLengthUniform = {0.1, 0.5}; 


    public static final double[] fruitsPerBranchLength = {0.05, 0.15}; 

    public static final double maxBranchGrowthPercentage = 0.01; 
    public static final double maxTreeGrowthPercentage = 3e-5; 

    public static final double[] plantPositionUniform = {0, worldSize[0]}; 

    public static final double[] treeSizeUniform = {10.0, 15.0}; 
    public static final double[] bushSizeUniform = {3.0, 5.0}; 

    public static double plantBranchGrowthChance = 1e-3; 

    public static int[] scalarMaxBranchUnifrom = {1, 3}; 

    public static double branchFruitGrowthChance = 0.075; 
    //#endregion 
}
