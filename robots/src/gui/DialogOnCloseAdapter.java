package gui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

/**
 * При закрытии JFrame, переданного в качестве параметра конструктора
 * выводит диалоговое окно на русском языке с проверкой на то
 * что пользователь действительно хочет выйти из программы
 */
public class DialogOnCloseAdapter extends WindowAdapter {

    /**
     * Непосредственно само окно
     */
    private final MainApplicationFrame frame;

    private final String yes, no, sure, exit;

    /**
     * @param jFrame окно, событие закрытия которого будет обрабатываться
     */
    public DialogOnCloseAdapter(MainApplicationFrame jFrame, String yes, String no, String sure, String exit) {
        this.frame = jFrame;
        this.yes = yes;
        this.no = no;
        this.sure = sure;
        this.exit = exit;
    }

    /**
     * Переопределение коллбэка закрытия окна
     * <p>
     * Выводит на экрна диалоговое окно с двумя вариантами ответа: "Да" и "Нет"
     * При выборе "Да" программа закрывается
     * При выборе "Нет" программа продолжает работу
     *
     * @param e событие окна с которым был вызван коллбэк
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int result = JOptionPane.showOptionDialog(
                null,
                sure,
                exit,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{yes, no},
                yes);
        if (result == JOptionPane.YES_OPTION) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.onClose();
        } else if (result == JOptionPane.NO_OPTION) {
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
}
