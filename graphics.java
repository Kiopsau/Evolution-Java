import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class graphics extends JPanel implements KeyListener {

    public world world; 
    public boolean paused = false;
    private int mouseX = 0;
    private int mouseY = 0;

    // Constructor accepts the world created in Simulation.java
    public graphics(world world) {
        this.world = world;

        setPreferredSize(new Dimension((int) world.width, (int) world.height));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (world == null) return;

        try {
            // Draw Food
            g.setColor(Color.GREEN);
            for (food f : new ArrayList<>(world.foods)) {
                int x = (int) f.position.getX();
                int y = (int) f.position.getY();
                g.fillOval(x, y, 4, 4);
            }

            // Draw Creatures
            for (creature c : new ArrayList<>(world.creatures)) {
                int size = (int) (c.dna.size * 4); 
                int x = (int) c.position.getX(); 
                int y = (int) c.position.getY(); 

                g.setColor(Color.RED); 
                if (c.diseased) {
                    g.setColor(Color.YELLOW); 
                }
                
                g.fillOval(x - size, y - size, size * 2, size * 2); 
                




                //DRAW EYES 
                // radius from center to eye tip 
                double eyeLength = size * 0.6;

                // forward direction
                double dx = Math.cos(c.direction);
                double dy = Math.sin(c.direction);

                // perpendicular (sideways) direction
                double px = -dy;
                double py = dx;

                // spacing between the two eyes
                double eyeSpacing = size * 0.4;

                // base eye position (forward)
                double baseX = x + dx * eyeLength;
                double baseY = y + dy * eyeLength;

                // left eye
                int eye1X = (int) (baseX + px * eyeSpacing);
                int eye1Y = (int) (baseY + py * eyeSpacing);

                // right eye
                int eye2X = (int) (baseX - px * eyeSpacing);
                int eye2Y = (int) (baseY - py * eyeSpacing);

                g.setColor(Color.WHITE); 


                int eyeRadius = 2; 

                // draw both eyes
                g.fillOval(eye1X - eyeRadius, eye1Y - eyeRadius, 3, 3);
                g.fillOval(eye2X - eyeRadius, eye2Y - eyeRadius, 3, 3); 





                // Mouse hover logic
                double dist = Math.hypot(x - mouseX, y - mouseY);
                if (dist <= size) {
                    g.setColor(Color.YELLOW);
                    g.drawOval(x - size - 2, y - size - 2, (size + 2) * 2, (size + 2) * 2);
                    g.setColor(Color.WHITE);
                    g.drawString(c.name + " (Energy: " + (int) c.energy + ")", x + 10, y - 10);
                    
                    for (int i = 0; i < c.diseases.size(); i++) {
                        g.drawString(c.diseases.get(i), x + 10, y - 20 - 10 * i); 
                    }
                } 
            }

            //draw total pop number 
            g.setColor(Color.WHITE); 
            g.drawString("Total Population: " + world.creatures.size(), 10, 10); 

            g.drawString((int) (config.step / 365) + ", " + config.step, (int) ((config.worldSize[0] - 40) / 2), 10); 

            //draw time per step 
            g.drawString(config.deltaTimeMs + "", (int) (config.worldSize[0] - 50), 10); 
        } catch (Exception e) {
            // We catch everything (NullPointerException, ConcurrentModificationException)
            // and do absolutely nothing. The screen will just refresh in 16ms anyway.
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = !paused; // Toggles the logic in Simulation.java
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}