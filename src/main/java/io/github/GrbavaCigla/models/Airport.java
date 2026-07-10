package io.github.GrbavaCigla.models;

import io.github.GrbavaCigla.core.Observable;

public class Airport extends Observable<Airport> {
    private String name;
    private String code;
    private int x;
    private int y;
    private boolean visible = true;

    public Airport(String name, String code, int x, int y) {
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setName(String name) {
        if (this.name.equals(name))
            return;
        this.name = name;
        notifyObservers(this);
    }

    public void setCode(String code) {
        if (this.code.equals(code))
            return;
        this.code = code;
        notifyObservers(this);
    }

    public void setX(int x) {
        if (this.x == x)
            return;
        this.x = x;
        notifyObservers(this);
    }

    public void setY(int y) {
        if (this.y == y)
            return;
        this.y = y;
        notifyObservers(this);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        notifyObservers(this);
    }

    public void update(Airport airport) {
        if (this.name.equals(airport.name)
                && this.code.equals(airport.code)
                && this.x == airport.x
                && this.y == airport.y)
            return;
        this.name = airport.name;
        this.code = airport.code;
        this.x = airport.x;
        this.y = airport.y;
        notifyObservers(this);
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}
