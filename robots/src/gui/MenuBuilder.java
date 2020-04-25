package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuBuilder {

    private final JFrame jFrame;

    public MenuBuilder(JFrame frame) {
        jFrame = frame;
    }

    public JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createMenu("Режим отображение", "Управление режимом отображения приложения", KeyEvent.VK_V);
        JMenu testMenu = createMenu("Тесты", "Тестовые команды", KeyEvent.VK_T);
        JMenu close = createMenu("Закрыть", "Закрыть приложение", KeyEvent.VK_C);

        JMenuItem systemLookAndFeel = createMenuItem("Системная схема", (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            jFrame.invalidate();
        });

        JMenuItem crossPlatformLookAndFeel = createMenuItem("Универсальная схема", (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            jFrame.invalidate();
        });

        JMenuItem addLogMessageItem = createMenuItem("Сообщение в лог", (event) ->
                Logger.debug("Новая строка")
        );

        lookAndFeelMenu.add(systemLookAndFeel);
        lookAndFeelMenu.add(crossPlatformLookAndFeel);

        close.addMenuListener(new CloseOnClickMenuListener(jFrame));


        testMenu.add(addLogMessageItem);

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(close);
        return menuBar;
    }

    private JMenuItem createMenuItem(String title, ActionListener l){
        JMenuItem menuItem = new JMenuItem(title, KeyEvent.VK_S);
        menuItem.addActionListener(l);
        return menuItem;
    }

    private JMenu createMenu(String title, String description, int mnemonic){
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(jFrame);
        }
        catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }
}
