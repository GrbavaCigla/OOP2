package io.github.GrbavaCigla.core;

public interface Observer<T> {
    void update(Observable<T> observable, T model);
}
