import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;

public class Field extends JPanel {
    private volatile boolean paused;
    private CopyOnWriteArrayList<BouncingBall> balls = new CopyOnWriteArrayList<>();
    private Point initialPoint;
    private Point finalPoint;
    private BouncingBall arrowBall;

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field() {
        setBackground(Color.WHITE);
        repaintTimer.start();

        addMouseListener(new MouseAdapter() {
            private long pressTime;
        
            public void mousePressed(MouseEvent e) {
                initialPoint = e.getPoint();
                pressTime = System.currentTimeMillis(); 
                for(BouncingBall ball: balls){
                    if (ball.isInside(initialPoint)){
                        arrowBall = ball;
                        pause();
                        break;
                    }
                }
            }
        
            public void mouseReleased(MouseEvent e) {
                finalPoint = e.getPoint();
                long releaseTime = System.currentTimeMillis(); 
                long elapsedTime = releaseTime - pressTime; 
                if (arrowBall != null) {
                    arrowBall.bowMode(initialPoint, finalPoint, elapsedTime);
                }
                resume();
                arrowBall = null;
            }
        });
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball : balls) {
            ball.paint(canvas);
        }
    }

    public void addBall() {
        balls.add(new BouncingBall(this));
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (paused) {
            wait();
        }
    }
}