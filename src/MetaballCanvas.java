
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Martin Doms
 */
public class MetaballCanvas extends JPanel{
    // Variables that control metaball behaviour
    private float maxVelocity, maxRadius;
    private Metaball[] balls;

    // Variables that control draw condition
    private float minDrawThreshold, maxDrawThreshold, minThreshold, maxThreshold;
    private float squareSize;
    private boolean showSquares;
    private int xSize, ySize;

    // Variables that control draw surface
    private BufferedImage canvas;

    public MetaballCanvas() {
        balls = new Metaball[10];
        maxRadius = 8000;
        maxVelocity = 30;
        xSize = 800; ySize = 800;
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new Metaball(this);
        }
        squareSize = 100;

        minThreshold = 1f;
        maxThreshold = 1.2f;
        minDrawThreshold = minThreshold/1.5f;
        maxDrawThreshold = maxThreshold*1.3f;
        showSquares = true;

        canvas = new BufferedImage (xSize, ySize,
        BufferedImage.TYPE_INT_RGB);

        JFrame container = new JFrame("Metaballs");
        JPanel panel = (JPanel)container.getContentPane();

        panel.setPreferredSize(new Dimension (xSize, ySize));
        panel.setLayout(null);

        setBounds(0,0,xSize,ySize);
        panel.add(this);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        container.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        while(true) {
            try {
                repaint();
                Thread.sleep(5);
                canvas = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
                drawLoop();
            } catch (InterruptedException ex) {
                Logger.getLogger(MetaballCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void drawLoop() {
        for(int i = 0; i < balls.length; i++) {
            balls[i].updatePosition();
        }
        draw(canvas);
    }

    private void draw(BufferedImage g) {
        for (int y = 0; y < ySize; y+=squareSize) {
            for (int x = 0; x < xSize; x+=squareSize) {
                float sum = 0;
                for (int i = 0; i < balls.length; i++) {
                    sum += balls[i].equation(x+squareSize/2, y+squareSize/2);
                    if (sum > minDrawThreshold && sum < maxDrawThreshold) {
                        drawSquare(x,y, g);
                    }
                }
            }
        }
    }
    private void drawSquare(int x, int y, BufferedImage g) {
        for (int internalY = 0; internalY < squareSize; internalY++) {
            for (int internalX = 0; internalX < squareSize; internalX++) {
                float sum = 0;
                for (int j = 0; j < balls.length; j+=5) {
                    sum += balls[j].equation(internalX + x, internalY + y);
                    if (sum >= maxThreshold) {
                        break;
                    }
                }
                if (sum > minThreshold && sum < maxThreshold
                        && internalX+x < xSize && internalY+y < ySize) {
                    g.setRGB(internalX+x, internalY+y, 100100100);
                }
                if (showSquares && (internalX == 1 || internalY == 1 ||
                        internalX == squareSize-1 || internalY == squareSize-1)
                        && internalX < xSize-x && internalY < ySize-y) {
                    g.setRGB(internalX+x, internalY+y, 200200200);
                }
            }
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(canvas, null, 0,0);
    }

    public int getHorizSize() {
        return xSize;
    }
    public int getVertSize() {
        return ySize;
    }
    public float getMaxVelocity() {
        return maxVelocity;
    }
    public float getMaxRadius() {
        return maxRadius;
    }

    public void addBall(int radius, int speed) {
        Metaball[] temp = new Metaball[balls.length+1];
        for (int i = 0; i < balls.length; i++) {
            temp[i] = balls[i];
        }
        temp[temp.length-1] = new Metaball(this, radius, speed);
        balls = temp;
    }
    public void removeBall() {
        Metaball[] temp = new Metaball[balls.length-1];
        for (int i = 0; i < balls.length-1; i++) {
            temp[i] = balls[i];
        }
        balls = temp;
    }
    public void toggleSquares() {
        showSquares = !showSquares;
    }
    public void setResolution(int x, int y) {
        xSize = x;
        ySize = y;
    }
}