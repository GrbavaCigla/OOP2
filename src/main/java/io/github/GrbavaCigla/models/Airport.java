package io.github.GrbavaCigla.models;

import io.github.GrbavaCigla.core.Observable;

public class Airport extends Observable<Airport> {
    private String name;
    private String code;
    private float x;
    private float y;

    public Airport(String name, String code, float x, float y) {
        this.name = name;
        this.code = code;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
        notifyObservers(this);
    }

    public void setCode(String code) {
        this.code = code;
        notifyObservers(this);
    }

    public void setX(float x) {
        this.x = x;
        notifyObservers(this);
    }

    public void setY(float y) {
        this.y = y;
        notifyObservers(this);
    }
}
