package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Класс, отвечающий за данные для логов
 */
public class LogWindowSource
{
    /**
     * Количество сообщений в логах
     */
    private int queueLength;

    /**
     * Структура данных, отвечающая за хранение сообщений
     */
    private LinkedList<LogEntry> messages;
    /**
     * Подписчики на событие изменения данных
     */
    private final ArrayList<LogChangeListener> subscribers;

    /**
     * @param iQueueLength количество сообщений в логах
     */
    public LogWindowSource(int iQueueLength)
    {
        queueLength = iQueueLength;
        messages = new LinkedList<>();
        subscribers = new ArrayList<>();
    }

    /**
     * @param listener объект, с реализацией LogChangeListener, желающий подписаться на обновление
     */
    public void registerListener(LogChangeListener listener)
    {
        synchronized(subscribers)
        {
            subscribers.add(listener);
        }
    }

    /**
     * @param listener объект, с реализацией LogChangeListener, желающий отписаться от обновления
     */
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(subscribers)
        {
            subscribers.remove(listener);
        }
    }

    /**
     * Добавление сообщения в лог
     *
     * @param logLevel уровень сообщения
     * @param strMessage содержание сообщения
     */
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

    /**
     * Возвращает количество хранимых сообщений
     */
    public int size()
    {
        return messages.size();
    }

    /**
     * Возвращает итератор по сообщениям в заданном диапазоне
     *
     * @param startFrom индекс начала сообщений
     * @param count количество сообщений для отображения
     */
    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.size());
        return messages.subList(startFrom, indexTo);
    }

    /**
     * Возвращает итератор по всем хранимым сообщениям
     */
    public Iterable<LogEntry> all()
    {
        return messages;
    }
}
