package gui.menu;

import gui.MainApplicationFrame;
import log.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MenuBuilder {

    private final MainApplicationFrame jFrame;

    public MenuBuilder(MainApplicationFrame frame) {
        jFrame = frame;
    }

    public JMenuBar generateMenuBar(ResourceBundle exampleBundle)
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu design = createMenu(exampleBundle.getString("mode_title"), exampleBundle.getString("mode_desc"), KeyEvent.VK_V);
        JMenu test = createMenu(exampleBundle.getString("test_title"), exampleBundle.getString("test_desc"), KeyEvent.VK_T);
        JMenu close = createMenu(exampleBundle.getString("close_title"), exampleBundle.getString("close_desc"), KeyEvent.VK_C);
        JMenu language = createMenu(exampleBundle.getString("lang_title"), exampleBundle.getString("lang"), KeyEvent.VK_L);

        JMenuItem system = createMenuItem(exampleBundle.getString("system_title"), (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            jFrame.invalidate();
        });

        JMenuItem cross = createMenuItem(exampleBundle.getString("universal_title"), (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            jFrame.invalidate();
        });

        ActionListener l = (event) -> Logger.debug(exampleBundle.getString("new_line"));
        JMenuItem add = createMenuItem(exampleBundle.getString("log_message"), l);

        JMenuItem ru = new JMenuItem(exampleBundle.getString("ru"), KeyEvent.VK_S);
        JMenuItem tr = new JMenuItem(exampleBundle.getString("en"), KeyEvent.VK_S);

        ActionListener ruL = (event) -> {
            Locale locale = new Locale("ru", "RU");
            ResourceBundle bundle = PropertyResourceBundle.getBundle("resources", locale);
            updateMenus(bundle, design, test, close, language);
            updateMenuItems(bundle, system, cross, add, ru, tr, l);

            jFrame.updateLanguage(locale);
        };
        ru.addActionListener(ruL);

        ActionListener trL = (event) -> {
            Locale locale = new Locale("en", "RU");
            ResourceBundle bundle = PropertyResourceBundle.getBundle("resources", locale);
            updateMenus(bundle, design, test, close, language);
            updateMenuItems(bundle, system, cross, add, ru, tr, l);

            jFrame.updateLanguage(locale);
        };
        tr.addActionListener(trL);

        design.add(system);
        design.add(cross);

        close.addMenuListener(new CloseOnClickMenuListener(jFrame));

        test.add(add);

        language.add(ru);
        language.add(tr);

        menuBar.add(design);
        menuBar.add(test);
        menuBar.add(language);
        menuBar.add(close);
        return menuBar;
    }

    private void updateMenus(ResourceBundle bundle, JMenu design, JMenu test, JMenu close, JMenu language) {
        design.setText(bundle.getString("mode_title"));
        design.getAccessibleContext().setAccessibleDescription(bundle.getString("mode_desc"));

        test.setText(bundle.getString("test_title"));
        test.getAccessibleContext().setAccessibleDescription(bundle.getString("test_desc"));

        close.setText(bundle.getString("close_title"));
        close.getAccessibleContext().setAccessibleDescription(bundle.getString("close_desc"));

        language.setText(bundle.getString("lang_title"));
        language.getAccessibleContext().setAccessibleDescription(bundle.getString("lang"));
    }

    private void updateMenuItems(ResourceBundle exampleBundle, JMenuItem system, JMenuItem cross, JMenuItem add, JMenuItem ru, JMenuItem tr, ActionListener a) {
        system.setText(exampleBundle.getString("system_title"));
        cross.setText(exampleBundle.getString("universal_title"));
        add.setText(exampleBundle.getString("log_message"));
        ru.setText(exampleBundle.getString("ru"));
        tr.setText(exampleBundle.getString("en"));

        add.removeActionListener(a);
        add.addActionListener((event) -> {
            Logger.debug(exampleBundle.getString("new_line"));
        });
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
