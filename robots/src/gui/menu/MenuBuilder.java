package gui.menu;

import log.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MenuBuilder {

    private final JFrame jFrame;

    public MenuBuilder(JFrame frame) {
        jFrame = frame;
    }

    public JMenuBar generateMenuBar(ResourceBundle exampleBundle)
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createMenu(exampleBundle.getString("mode_title"), exampleBundle.getString("mode_desc"), KeyEvent.VK_V);
        JMenu testMenu = createMenu(exampleBundle.getString("test_title"), exampleBundle.getString("test_desc"), KeyEvent.VK_T);
        JMenu close = createMenu(exampleBundle.getString("close_title"), exampleBundle.getString("close_desc"), KeyEvent.VK_C);

        JMenuItem systemLookAndFeel = createMenuItem(exampleBundle.getString("system_title"), (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            jFrame.invalidate();
        });

        JMenuItem crossPlatformLookAndFeel = createMenuItem(exampleBundle.getString("universal_title"), (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            jFrame.invalidate();
        });

        JMenuItem addLogMessageItem = createMenuItem(exampleBundle.getString("log_message"), (event) ->
                Logger.debug(exampleBundle.getString("new_line"))
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
