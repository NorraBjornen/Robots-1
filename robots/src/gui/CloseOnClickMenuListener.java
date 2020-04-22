package gui;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.WindowEvent;

/**
 * Обрабатывает нажатие на пункт меню
 * При нажатии вызывает у окна событие WindowEvent.WINDOW_CLOSING
 */
public class CloseOnClickMenuListener implements MenuListener {

    /**
     * Непосредственно само окно
     */
    private final JFrame frame;

    /**
     * @param frame окно, событие закрытия которого будет обрабатываться
     */
    public CloseOnClickMenuListener(JFrame frame){
        this.frame = frame;
    }

    /**
     * При нажатии вызывает у окна событие WindowEvent.WINDOW_CLOSING
     *
     * @param e событие с которым будет выбираться пункт меню
     */
    @Override
    public void menuSelected(MenuEvent e) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
