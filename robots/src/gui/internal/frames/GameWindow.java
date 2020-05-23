package gui.internal.frames;

import gui.GameVisualizer;
import logic.robot.Robot;
import javax.swing.JPanel;
import java.awt.*;

/**
 * Окно в котором происходит игровой процесс (движение робота)
 */
public class GameWindow extends AbstractInternalFrame
{
    /**
     * @param robot по координатам и направлению этой модели будет происходить открисовка робота в окне
     */
    public GameWindow(Robot robot)
    {
        super("Игровое поле", true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer(robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
