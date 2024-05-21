import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;
    private Field field = new Field();

    public MainFrame() {
        super("Programming and Thread Synchronization");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        setExtendedState(MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu ballMenu = new JMenu("Balls");
        menuBar.add(ballMenu);
        ballMenu.add(createMenuItem("Add Ball", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                addBall();
            }
        }));

        JMenu controlMenu = new JMenu("Control");
        menuBar.add(controlMenu);
        pauseMenuItem = createMenuItem("Pause Movement", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                togglePauseResume(true);
            }
        });
        controlMenu.add(pauseMenuItem);
        resumeMenuItem = createMenuItem("Resume Movement", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                togglePauseResume(false);
            }
        });
        controlMenu.add(resumeMenuItem);

        getContentPane().add(field, BorderLayout.CENTER);
    }

    private JMenuItem createMenuItem(String title, Action action) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setText(title);
        return menuItem;
    }

    private void addBall() {
        field.addBall();
        pauseMenuItem.setEnabled(true);
    }

    private void togglePauseResume(boolean pause) {
        if (pause) {
            field.pause();
            pauseMenuItem.setEnabled(false);
            resumeMenuItem.setEnabled(true);
        } else {
            field.resume();
            pauseMenuItem.setEnabled(true);
            resumeMenuItem.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}