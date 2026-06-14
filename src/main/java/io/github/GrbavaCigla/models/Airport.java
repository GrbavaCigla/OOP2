package io.github.GrbavaCigla.models;

import io.github.GrbavaCigla.core.Observable;
import io.github.GrbavaCigla.core.interfaces.Tabulatable;

public class Airport extends Observable<Airport> implements Tabulatable {
    private String name;
    private String code;
    private float x;
    private float y;

    private boolean visible = true;

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

    public boolean getVisible() {
        return visible;
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

    public void update(String name, String code, float x, float y) {
        this.name = name;
        this.code = code;
        this.x = x;
        this.y = y;

        notifyObservers(this);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        notifyObservers(this);
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }

    public Object[] getColumns() {
        return new Object[] { "Name", "Code", "Visible" };
    }

    public Object[] getRow() {
        return new Object[] { name, code, visible };
    }

    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 2 ? Boolean.class : String.class;
    }

    @Override
    public void updateCell(Object value, int columnIndex) {
        if (columnIndex == 2) {
            setVisible((boolean) value);
        }
    }
}
