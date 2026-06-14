package io.github.GrbavaCigla.core;

import java.util.HashSet;

import io.github.GrbavaCigla.core.interfaces.Observer;

public abstract class Observable<T> {
    private HashSet<Observer<T>> observers = new HashSet<>();

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(T model) {
        for (Observer<T> observer : observers) {
            observer.update(this, model);
        }
    }
}
