package gui.internal.frames;

import logic.state.StateSaveable;
import logic.state.WindowState;

import javax.swing.*;
import java.beans.PropertyVetoException;

/**
 * Абстрактный класс внутреннего окна, реализующий стандартную логику сохранения состояния
 */
abstract class AbstractInternalFrame extends JInternalFrame implements StateSaveable {

    /**
     * Конструктор, аналогичный конструктору класса JInternalFrame
     * В нем происходит обычное пробрасывание параметров в конструктор суперкласса
     */
    protected AbstractInternalFrame(String title, boolean resizeable, boolean closeable, boolean maximizable, boolean iconifiable) {
        super(title, resizeable, closeable, maximizable, iconifiable);
    }

    /**
     * Возращает объект типа WindowState с текущими шириной, высотой, координатами и видимостью окна
     */
    @Override
    public WindowState saveState() {
        return new WindowState(getWidth(), getHeight(), getX(), getY(), isDisplayable());
    }

    /**
     * Устанавливает окно в нужное состояние
     *
     * @param state объект состояния, хранящий в себе информацию о том, каких размеров и видимости
     * и по каким координатам должно быть окно
     */
    @Override
    public void restoreState(WindowState state) {
        setBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
        if (state.isVisible()) {
            setVisible(true);
        } else {
            try {
                setIcon(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }
}
