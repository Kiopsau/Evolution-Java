import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import javax.swing.*;

class LiveMetricsGraph extends JPanel {

    private final List<List<Double>> series = new ArrayList<>();
    private final String[] labels;
    private final Color[] colors; // = {Color.ORANGE, Color.BLUE, Color.RED, Color.YELLOW, Color.BLACK};
    private final float[] thicknesses = {2.5f, 2.5f, 2.5f, 2.5f, 2.5f};
    private final int padding = 70;

    public LiveMetricsGraph(String[] labels, Color[] colors) {
        // Initialize 4 series
        this.labels = labels; 
        this.colors = colors; 
        for (String label : labels) {
            series.add(new ArrayList<>());
        }
    }

    // Add a new step (4-element array)
    public synchronized void addStep(double[] stepData) {
        if (stepData.length != series.size()) {
            throw new IllegalArgumentException("Step data must have length " + series.size());
        }
        for (int i = 0; i < stepData.length; i++) {
            series.get(i).add(stepData[i]);
        }
        repaint(); // update graph
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int graphWidth = width - 2 * padding;
        int graphHeight = height - 2 * padding;

        // Determine X/Y ranges
        int steps = series.get(0).size();
        double xMin = 0;
        double xMax = steps > 1 ? steps - 1 : 1;

        double yMin = series.stream()
                .flatMap(List::stream)
                .min(Double::compare)
                .orElse(0.0);
        double yMax = series.stream()
                .flatMap(List::stream)
                .max(Double::compare)
                .orElse(1.0);

        // Axes
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2.drawLine(padding, padding, padding, height - padding); // Y-axis
        g2.drawString("Step", width / 2 - 20, height - 20);
        g2.drawString("Value", 20, height / 2);

        BiFunction<Double, Double, Point> scale = (x, y) -> {
            int px = (int) (padding + (x - xMin) / (xMax - xMin) * graphWidth);
            int py = (int) (height - padding - (y - yMin) / (yMax - yMin) * graphHeight);
            return new Point(px, py);
        }; 

        int yTicks = 5; // number of tick marks

        for (int i = 0; i <= yTicks; i++) {
            double value = yMin + (yMax - yMin) * i / yTicks;

            int y = (int) (height - padding - (value - yMin) / (yMax - yMin) * graphHeight);

            // Tick line
            g2.setColor(Color.BLACK);
            g2.drawLine(padding - 5, y, padding, y);

            // Label
            String label = String.format("%.2f", value);
            g2.drawString(label, padding - 60, y + 5);
        } 

        int xTicks = 5; // number of tick marks

        for (int i = 0; i <= xTicks; i++) {
            double value = xMin + (xMax - xMin) * i / xTicks;
            int x = (int) (padding + (value - xMin) / (xMax - xMin) * graphWidth);

            // Tick line (vertical)
            g2.setColor(Color.BLACK);
            g2.drawLine(x, height - padding, x, height - padding + 5);

            // Label
            String label = String.format("%.0f", value);
            g2.drawString(label, x - 10, height - padding + 20);
        }

        // Draw lines for each metric
        for (int i = 0; i < series.size(); i++) {
            g2.setColor(colors[i]);
            g2.setStroke(new BasicStroke(thicknesses[i], BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            List<Double> data = series.get(i);
            for (int j = 0; j < data.size() - 1; j++) {
                Point p1 = scale.apply((double) j, data.get(j));
                Point p2 = scale.apply((double) (j + 1), data.get(j + 1));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Draw legend
        int legendX = width - padding - 150;
        int legendY = padding;
        for (int i = 0; i < labels.length; i++) {
            g2.setColor(colors[i]);
            g2.fillRoundRect(legendX, legendY + i * 25, 15, 10, 5, 5);
            g2.setColor(Color.BLACK);
            g2.drawString(labels[i], legendX + 20, legendY + 10 + i * 25);
        }
    }
}

public class graph {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Live Metrics Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);

        LiveMetricsGraph graph = new LiveMetricsGraph(new String[]{"no"}, new Color[]{Color.BLACK});
        frame.add(graph);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Simulate a loop that produces new step data
        new Thread(() -> {
            for (int step = 1; step <= 20; step++) {
                double[] stepData = new double[5];
                stepData[0] = Math.random() * step;        // avgSpeed
                stepData[1] = Math.random() * 10;          // avgVision
                stepData[2] = Math.random() * 5 + 1;       // avgSize
                stepData[3] = Math.random() * 50;          // maxEnergy
                stepData[4] = Math.random() * 10; 
                graph.addStep(stepData);

                try {
                    Thread.sleep(500); // simulate time between steps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}