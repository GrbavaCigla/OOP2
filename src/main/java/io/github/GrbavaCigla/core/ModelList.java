package io.github.GrbavaCigla.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.GrbavaCigla.core.interfaces.Observer;

public class ModelList<T extends Observable<T>> extends Observable<List<T>> {
    private List<T> data = new ArrayList<>();

    public void add(T item) {
        data.add(item);
        notifyObservers(getModels());
    }

    public void remove(int index) {
        if (data.remove(index) != null) {
            notifyObservers(getModels());
        }
    }

    public void clear() {
        data.clear();
        notifyObservers(getModels());
    }

    public List<T> getModels() {
        return Collections.unmodifiableList(data);
    }

    public void addObservers(Observer<T> o, Observer<List<T>> os) {
        addObserver(os);
        for(T d : data) {
            d.addObserver(o);
        }
    }
}