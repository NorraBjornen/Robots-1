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
     * Сохраняет переданный объект на диск по пути filename
     *
     * @param states сохраняемые состояния. В HashMap ключ - тэг окна, значение - WindowState соответствующего окна.
     */
    public void saveOnDisk(HashMap<String, WindowState> states) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(states);
            System.out.println("Serialized data is saved in " + filename);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает HashMap состояний с диска
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, WindowState> loadFromDisk() {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            return (HashMap<String, WindowState>) in.readObject();

        } catch (IOException | ClassNotFoundException i) {
            return null;
        }
    }
}
