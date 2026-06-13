package io.github.GrbavaCigla.core.interfaces;

public interface Tabulatable {
    public Object[] getRow();

    public Object[] getColumns();

    public default Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public default void updateCell(Object value, int columnIndex) {

    }
}