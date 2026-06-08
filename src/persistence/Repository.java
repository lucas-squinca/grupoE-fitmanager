package persistence;

import java.util.ArrayList;

public abstract class Repository<T> {

    protected ArrayList<T> elements;

    public Repository() {
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

    public abstract void save(String filePath);
    public abstract void load(String filePath);
}