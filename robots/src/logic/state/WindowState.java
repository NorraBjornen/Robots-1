package logic.state;

import java.io.Serializable;

/**
 * Класс, хранящий состояние окна
 */
public class WindowState implements Serializable {

    private final int width;
    private final int height;
    private final int x;
    private final int y;
    private final boolean visible;

    /**
     * @param width ширина окна
     * @param height высота окна
     * @param x позиция по координате x
     * @param y позиция по координате y
     * @param visible параметр, указывающий на то, не является ли окно в свернутом состоянии
     */
    public WindowState(int width, int height, int x, int y, boolean visible) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }
}
