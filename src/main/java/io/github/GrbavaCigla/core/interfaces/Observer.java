package io.github.GrbavaCigla.core.interfaces;

import io.github.GrbavaCigla.core.Observable;

public interface Observer<T> {
    void update(Observable<T> observable, T model);
}
