package logic.state;

import java.io.*;
import java.util.HashMap;

/**
 * Класс отвечающий за сохранение состояний окон на диск
 * и загрузку состояний с диска
 */
public class DiskStateSaver {

    /**
     * Текстовое поле, хранящее в себе адрес файла, в который и из которого будет происходить запись и чтение
     */
    private final String filename = System.getProperty("user.home") + "\\data";

    /**
     * Поле, хранящее в себе информацию о состояниях окон то соответвующим тегам
     */
    private HashMap<String, WindowState> states = new HashMap<>();

    /**
     * Чейн-метод, позволяющий добавлять состояние окна по указанному тегу в HashMap states
     *
     * @param tag тег окна, т.е. ключ в HashMap, по которому лежит значение соответсвующего WindowState
     * @param state состояние окна
     */
    public DiskStateSaver addState(String tag, WindowState state) {
        states.put(tag, state);
        return this;
    }

    /**
     * Возвращает состояние окна для окна с заданным тэгом
     *
     * @param tag тег окна, т.е. ключ в HashMap, по которому лежит значение соответсвующего WindowState
     */
    public WindowState getStateByTag(String tag) {
        if (states.containsKey(tag))
            return states.get(tag);
        return null;
    }

    /**
     * Сохраняет объект состояния окон из поля states на диск по пути filename
     */
    public void save() {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(states);
            System.out.println("Serialized data is saved in " + filename);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает HashMap состояний с диска в поле states
     * Если данные удалось загрузить - возращает true, иначе - false
     */
    @SuppressWarnings("unchecked")
    public boolean loadFromDisk() {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            states = (HashMap<String, WindowState>) in.readObject();

            return true;
        } catch (IOException | ClassNotFoundException i) {
            return false;
        }
    }
}
