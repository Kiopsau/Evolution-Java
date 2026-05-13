
import java.util.Random;

public class perlinNoise {

    public static double[][] gen(int width, int height) {
        double[][] map = new double[width][height]; 

        int[] permutation = generatePermutation(); 

        double scale = 0.05; 

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double value = octaveNoise(
                    x * scale, 
                    y * scale, 
                    4, 
                    permutation
                ); 

                map[x][y] = value; 
            }
        }

        return map; 
    }

    private static double octaveNoise(double x, double y, int octaves, int[] permutation) {
        double value = 0.0;

        double amplitude = 1.0;
        double frequency = 1.0;

        double maxValue = 0.0;

        double persistence = 0.5;   // how quickly amplitude drops
        double lacunarity = 2.0;    // how quickly frequency increases

        for (int i = 0; i < octaves; i++) {
            value += noise(x * frequency, y * frequency, permutation) * amplitude;

            maxValue += amplitude;

            amplitude *= persistence;
            frequency *= lacunarity;
        }

        return value / maxValue;
    }

    private static double noise(double x, double y, int[] permutation) {
        int xi = ((int) Math.floor(x)) & 255; 
        int yi = ((int) Math.floor(y)) & 255; 

        x -= Math.floor(x); 
        y -= Math.floor(y); 

        double u = fade(x); 
        double v = fade(y); 

        int a = permutation[xi] + yi; 
        int b = permutation[xi + 1] + yi; 

        double result = lerp(v, 
            lerp(u, 
                grad(permutation[a], x, y), 
                grad(permutation[b], x - 1, y)), 
            lerp(u, 
                grad(permutation[a + 1], x, y - 1), 
                grad(permutation[b + 1], x - 1, y - 1))); 
        
        return (result + 1) / 2.0; 
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10); 
    } 

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a); 
    }

    private static double grad(int hash, double x, double y) {
        switch (hash & 3) {
            case 0: 
                return x + y; 
            case 1: 
                return -x + y; 
            case 2: 
                return x - y; 
            default: 
                return -x - y; 
        }
    } 

    private static int[] generatePermutation() {
        Random rand = new Random(); 

        int[] p = new int[512]; 
        int[] temp = new int[256]; 

        for (int i = 0; i < 256; i++) {
            temp[i] = i; 
        }

        for (int i = 255; i > 0; i--) {
            int index = rand.nextInt(i + 1); 

            int swap = temp[i]; 
            temp[i] = temp[index]; 
            temp[index] = swap; 
        }

        for (int i = 0; i < 512; i++) {
            p[i] = temp[i & 255]; 
        }

        return p; 
    }







    public static void diffuse(double[][] map, double diffusionRate) {
        int w = map.length;
        int h = map[0].length;

        double[][] newMap = new double[w][h];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                double current = map[x][y];

                double sum = current;
                int count = 1;

                // left
                if (x > 0) {
                    sum += map[x - 1][y];
                    count++;
                }

                // right
                if (x < w - 1) {
                    sum += map[x + 1][y];
                    count++;
                }

                // up
                if (y > 0) {
                    sum += map[x][y - 1];
                    count++;
                }

                // down
                if (y < h - 1) {
                    sum += map[x][y + 1];
                    count++;
                }

                double average = sum / count;

                // blend current with average
                newMap[x][y] =
                    current * (1 - diffusionRate)
                    + average * diffusionRate;
            }
        }

        // copy back
        for (int x = 0; x < w; x++) {
            System.arraycopy(newMap[x], 0, map[x], 0, h);
        }
    }
} 