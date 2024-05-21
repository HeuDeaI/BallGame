import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    private static final int MAX_RADIUS = 70;
    private static final int MIN_RADIUS = 20;
    private static final int MAX_SPEED = 10;
    private Field field;
    private int radius;
    private Color color;
    private double x;
    private double y;
    private int speed;
    private double speedX;
    private double speedY;
    private double angle;

    public BouncingBall(Field field) {
        this.field = field;
        radius = (int)(Math.random() * (MAX_RADIUS - MIN_RADIUS)) + MIN_RADIUS;
        speed = Math.min((int)(7 * MAX_SPEED / radius), MAX_SPEED);
        angle = Math.random() * 2 * Math.PI;
        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;
        new Thread(this).start();
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                field.canMove(this);
                if (x + speedX <= radius || x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                }
                if (y + speedY <= radius || y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                }
                x += speedX;
                y += speedY;
                Thread.sleep(MAX_SPEED + 1 - speed);
            }
        } catch (InterruptedException ex) {
            // Thread was interrupted
        }
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        canvas.fill(ball);
    }

    public boolean isInside(Point point) {
        return Point.distance(x, y, point.x, point.y) <= radius;
    }

    public void bowMode(Point initialPoint, Point finalPoint, long elapsedTime) {
        int dx = finalPoint.x - initialPoint.x;
        int dy = finalPoint.y - initialPoint.y;
        angle = Math.atan2(dy, dx);
        long distance = Math.round(Point.distance(initialPoint.x, initialPoint.y, finalPoint.x, finalPoint.y));
        long speedBoost = Math.min(Math.max(distance * 10 / elapsedTime, 1), MAX_SPEED);
        speedX = speedBoost * Math.cos(angle);
        speedY = speedBoost * Math.sin(angle);
    }
}