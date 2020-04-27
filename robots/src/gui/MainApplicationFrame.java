package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import gui.internal.frames.GameWindow;
import gui.internal.frames.LogWindow;
import log.Logger;
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
    private final DiskStateSaver diskStateSaver;

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        logWindow = createLogWindow();
        gameWindow = createGameWindow();

        diskStateSaver = new DiskStateSaver();

        Map<String, WindowState> states = diskStateSaver.loadFromDisk();

        if(states != null) {
            WindowState logState = states.get("log");
            WindowState gameState = states.get("game");

            logWindow.restoreState(logState);
            gameWindow.restoreState(gameState);
        }

        addWindow(logWindow);
        addWindow(gameWindow);

        setJMenuBar(new MenuBuilder(this).generateMenuBar());
        addWindowListener(new DialogOnCloseAdapter(this));
    }

    /**
     * Вызывается при закрытии окна
     *
     * Состояния окно записываются в HashMap по соответвующим тэгам
     * Затем HashMap передается в DiskStateSaver для сохранения состояния на диск
     */
    public void onClose() {
        WindowState logState = logWindow.saveState();
        WindowState gameState = gameWindow.saveState();

        HashMap<String, WindowState> states = new HashMap<>();
        states.put("log", logState);
        states.put("game", gameState);

        diskStateSaver.saveOnDisk(states);
    }

    /**
     * Создание окна LogWindow с указанными параметрами по умолчанию
     */
    private LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Создание окна GameWindow с указанными параметрами по умолчанию
     */
    private GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
