package io.github.GrbavaCigla.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelList<T> extends Observable<List<T>> {
    private List<T> data = new ArrayList<>();

    public void add(T item) {
        data.add(item);
        notifyObservers(getModels());
    }

    public void remove(T item) {
        if (data.remove(item)) {
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
}