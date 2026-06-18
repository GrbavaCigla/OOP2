package io.github.GrbavaCigla.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ModelList<T extends Observable<T>> extends Observable<List<T>> {
    private List<T> data = new ArrayList<>();
    private List<Function<T, Object>> uniqueConstraintGetters = new ArrayList<>();

    public void add(T item) {
        validate(item);
        data.add(item);
        notifyObservers(getModels());
    }

    public void remove(int index) {
        if (data.remove(index) != null) {
            notifyObservers(getModels());
        }
    }

    public void clear() {
        if (!data.isEmpty()) {
            data.clear();
            notifyObservers(getModels());
        }
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

    public void addUniqueConstraint(Function<T, Object> uniqueConstraint) {
        uniqueConstraintGetters.add(uniqueConstraint);
    }

    public void validate(T item) {
        validate(item, null);
    }

    public void validate(T item, T old) {
        for (Function<T, Object> constraint : uniqueConstraintGetters) {
            Object field = constraint.apply(item);
            for(T d : data) {
                if (d == old) continue;
                if (constraint.apply(d).equals(field)) {
                    throw new IllegalStateException("Unique constraint violated for value: " + field);
                }
            }
        }
    }
}