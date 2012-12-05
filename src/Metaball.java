
/**
 *
 * @author Martin Doms
 */
public class Metaball {
    private int xPos, yPos;
    public double dx, dy, radius;
    private MetaballCanvas canvas;

    public Metaball(MetaballCanvas canvas) {
        this.canvas = canvas;
        xPos = (int)(Math.random() * canvas.getHorizSize());
        yPos = (int)(Math.random() * canvas.getVertSize());

        radius = canvas.getMaxRadius() * Math.random() + 3000;
        dx = canvas.getMaxVelocity() * Math.random() - canvas.getMaxVelocity() / 2;
        dy = canvas.getMaxVelocity() * Math.random() - canvas.getMaxVelocity() / 2;
    }
    public Metaball(MetaballCanvas canvas, int radius, int speed) {
        this.canvas = canvas;
        xPos = (int)(Math.random() * canvas.getHorizSize());
        yPos = (int)(Math.random() * canvas.getVertSize());

        this.radius = radius * Math.random() + 3000;
        dx = speed * Math.random() - speed / 2;
        dy = speed * Math.random() - speed / 2;
    }

    public float equation (float x, float y) {
        return (float)(radius / ((x-xPos)*(x-xPos) + (y-yPos)*(y-yPos)));
    }

    public void updatePosition() {
        if ((xPos <= 0 && dx < 0)
                || xPos >= canvas.getHorizSize() && dx > 0) {
            dx = -dx;
        }
        if (yPos <= 0 && dy < 0
                || yPos >= canvas.getVertSize() && dy > 0) {
            dy = -dy;
        }
        xPos += dx;
        yPos += dy;
    }
}