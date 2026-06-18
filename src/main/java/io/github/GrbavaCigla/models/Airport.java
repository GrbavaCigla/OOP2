package io.github.GrbavaCigla.models;

import io.github.GrbavaCigla.core.Observable;
import io.github.GrbavaCigla.core.Tabulatable;

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
        if (this.name == name)
            return;
        this.name = name;
        notifyObservers(this);
    }

    public void setCode(String code) {
        if (this.code == code)
            return;
        this.code = code;
        notifyObservers(this);
    }

    public void setX(float x) {
        if (this.x == x)
            return;
        this.x = x;
        notifyObservers(this);
    }

    public void setY(float y) {
        if (this.y == y)
            return;
        this.y = y;
        notifyObservers(this);
    }

    public void update(Airport airport) {
        if (this.name == airport.name
                && this.code == airport.code
                && this.x == airport.x
                && this.y == airport.y)
            return;

        this.name = airport.name;
        this.code = airport.code;
        this.x = airport.x;
        this.y = airport.y;

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

    @Override
    public String[] getColumns() {
        return new String[] { "Name", "Code", "X", "Y", "Visible" };
    }

    @Override
    public Object[] getRow() {
        return new Object[] { name, code, x, y, visible };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 4 ? Boolean.class : String.class;
    }

    @Override
    public boolean isColumnEditable(int column) {
        return column == 4;
    }

    @Override
    public void updateCell(Object value, int columnIndex) {
        if (columnIndex == 4) {
            setVisible((boolean) value);
        }
    }
}
