package persistence;

import exceptions.PersistenceException;
import java.io.*;
import java.util.ArrayList;

public class Repository<T> {

    protected ArrayList<T> elements;
    private Class<T> type;

    public Repository(Class<T> type) {
        this.type = type;
        this.elements = new ArrayList<>();
    }

    public void add(T element) {
        this.elements.add(element);
    }

    public void remove(T element) {
        this.elements.remove(element);
    }

    public ArrayList<T> listAll() {
        return this.elements;
    }

    public void save(String filePath) throws PersistenceException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this.elements);
        } catch (IOException e) {
            throw new PersistenceException("Erro ao salvar os dados no arquivo: " + filePath, e);
        }
    }

    public void load(String filePath) throws PersistenceException {
        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object rawData = ois.readObject();

            if (rawData instanceof ArrayList<?>) {
                ArrayList<?> rawList = (ArrayList<?>) rawData;
                this.elements.clear();

                for (Object item : rawList) {
                    this.elements.add(type.cast(item));
                }
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenceException("Erro (Dados corrompidos) ao carregar: " + filePath, e);
        }
    }
}