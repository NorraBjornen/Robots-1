package gui.internal.frames;

import gui.RobotObserver;

import javax.swing.*;
import java.awt.*;

/**
 * Окно, отображающее в себе координаты и направление робота
 */
public class CoordinatesWindow extends AbstractInternalFrame implements RobotObserver {

    /**
     * Тектовое поле, в котором будет отображаться информация
     */
    private final TextArea area;

    public CoordinatesWindow() {
        super("Координаты робота", true, true, true, true);

        area = new TextArea("");
        area.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(area, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    /**
     * Реализация интерфейса RobotObserver. Вызывается роботом при изменении координат и направления.
     */
    @Override
    public void update(double x, double y, double direction) {
        String text = "X:\t" + x + "\nY:\t" + y + "\nD:\t" + direction;
        area.setText(text);
    }
}
