package gui.internal.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

/**
 * При закрытии окна произойдет отписка от обновления данныхв логах
 */
public class LogWindow extends AbstractInternalFrame implements LogChangeListener {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(LogWindowSource logSource, String title) {
        super(title, true, true, true, true);

        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();

        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                m_logSource.unregisterListener(LogWindow.this);
            }
        });
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
