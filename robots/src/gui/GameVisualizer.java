package gui;

import logic.robot.Robot;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

import static utils.GraphicsUtils.drawOval;
import static utils.GraphicsUtils.fillOval;
import static utils.MathUtils.round;

/**
 * Панель визуализации игрового процесса
 */
public class GameVisualizer extends JPanel implements RobotObserver
{
    /**
     * Модель, по чьим координатам будет отрисовываться робот
     */
    private final Robot robot;

    /**
     * Координаты изначальной точки, к которой будет двигаться робот
     */
    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    /**
     * @param robot модель, по чьим координатам будет отрисовываться робот
     */
    public GameVisualizer(Robot robot)
    {
        this.robot = robot;
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
        robot.subscribe(this);
        robot.startMoving(m_targetPositionX, m_targetPositionY);
    }

    /**
     * @param p точка, на которую кликнул пользователь. К этой точке начнет движение робот.
     */
    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
        robot.startMoving(m_targetPositionX, m_targetPositionY);
    }

    /**
     * Реализация интерфейса RobotObserver. Вызывается роботом при изменении координат и направления.
     */
    @Override
    public void update(double x, double y, double direction) {
        EventQueue.invokeLater(this::repaint);
    }

    /**
     * Коллбэк отрисовки графического элемента
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, round(robot.getX()), round(robot.getY()), robot.getDirection());
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }

    /**
     * Метод отрисовки робота
     */
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(x);
        int robotCenterY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    /**
     * Метод отрисовки точки, к которой движется робот
     */
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
