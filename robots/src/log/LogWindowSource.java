package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class LogWindowSource
{
    private int queueLength;

    private LinkedList<LogEntry> messages;
    private final ArrayList<LogChangeListener> subscribers;

    public LogWindowSource(int iQueueLength)
    {
        queueLength = iQueueLength;
        messages = new LinkedList<>();
        subscribers = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(subscribers)
        {
            subscribers.add(listener);
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(subscribers)
        {
            subscribers.remove(listener);
        }
    }

    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);

        synchronized (subscribers) {
            if(size() == queueLength)
                messages.removeFirst();

            messages.addLast(entry);
        }

        for (LogChangeListener listener : subscribers)
            listener.onLogChanged();
    }

    public int size()
    {
        return messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.size());
        return messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return messages;
    }
}
