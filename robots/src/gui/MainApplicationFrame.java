package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import gui.internal.frames.CoordinatesWindow;
import gui.internal.frames.GameWindow;
import gui.internal.frames.LogWindow;
import log.Logger;
import logic.robot.Robot;
import logic.state.DiskStateSaver;
import gui.menu.MenuBuilder;
import logic.state.WindowState;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    private final LogWindow logWindow;
    private final GameWindow gameWindow;
    private final CoordinatesWindow coordinatesWindow;
    private final DiskStateSaver diskStateSaver;

    public MainApplicationFrame(Robot robot, Locale locale) {
        ResourceBundle exampleBundle = PropertyResourceBundle.getBundle("resources", locale);

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        gameWindow = createGameWindow(robot, exampleBundle.getString("game_header"));
        logWindow = createLogWindow(exampleBundle.getString("log_header"), exampleBundle.getString("log_start_message"));
        coordinatesWindow = createCoordinatesWindow(exampleBundle.getString("coords_header"));

        robot.subscribe(coordinatesWindow);

        diskStateSaver = new DiskStateSaver();
        boolean hasSavedData = diskStateSaver.loadFromDisk();

        if (hasSavedData) {
            WindowState logState = diskStateSaver.getStateByTag("log");
            WindowState gameState = diskStateSaver.getStateByTag("game");
            WindowState coordinatesState = diskStateSaver.getStateByTag("coord");

            logWindow.setState(logState);
            gameWindow.setState(gameState);
            coordinatesWindow.setState(coordinatesState);
        }

        addWindow(logWindow);
        addWindow(gameWindow);
        addWindow(coordinatesWindow);

        setJMenuBar(new MenuBuilder(this).generateMenuBar(exampleBundle));
        addWindowListener(new DialogOnCloseAdapter(
                this,
                exampleBundle.getString("d_yes"),
                exampleBundle.getString("d_no"),
                exampleBundle.getString("d_sure"),
                exampleBundle.getString("d_exit")
        ));
    }

    /**
     * Вызывается при закрытии окна
     * <p>
     * Состояния окон записываются в HashMap по соответвующим тегам
     * Затем HashMap передается в DiskStateSaver для сохранения состояния на диск
     */
    public void onClose() {
        diskStateSaver.addState("log", logWindow.getState())
                .addState("game", gameWindow.getState())
                .addState("coord", coordinatesWindow.getState())
                .save();
    }

    /**
     * Создание окна LogWindow с указанными параметрами по умолчанию
     */
    private LogWindow createLogWindow(String title, String startMessage) {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), title);
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(startMessage);
        return logWindow;
    }

    /**
     * Создание окна GameWindow с указанными параметрами по умолчанию
     */
    private static GameWindow createGameWindow(Robot robot, String title) {
        GameWindow gameWindow = new GameWindow(robot, title);
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    /**
     * Создание окна CoordinatesWindow с указанными параметрами по умолчанию
     */
    private static CoordinatesWindow createCoordinatesWindow(String title) {
        CoordinatesWindow coordinatesWindow = new CoordinatesWindow(title);
        coordinatesWindow.setLocation(10, 100);
        coordinatesWindow.setSize(400, 400);
        return coordinatesWindow;
    }

    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
