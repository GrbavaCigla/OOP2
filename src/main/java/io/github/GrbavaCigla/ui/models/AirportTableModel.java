package io.github.GrbavaCigla.ui.models;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.core.Observer;
import io.github.GrbavaCigla.models.Airport;

public class AirportTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = { "Name", "Code", "X", "Y", "Visible" };

    private final ModelList<Airport> modelList;
    private Observer<Airport> itemObserver;
    private Observer<List<Airport>> listObserver;

    public AirportTableModel(ModelList<Airport> modelList) {
        super(COLUMNS, 0);
        this.modelList = modelList;
        this.itemObserver = (obs, airport) -> refresh(modelList.getModels());
        this.listObserver = (obs, airports) -> {
            modelList.addObservers(this.itemObserver, this.listObserver);
            refresh(airports);
        };
        modelList.addObservers(itemObserver, listObserver);
        refresh(modelList.getModels());
    }

    private void refresh(List<Airport> airports) {
        setRowCount(0);
        for (Airport a : airports) {
            addRow(new Object[] { a.getName(), a.getCode(), a.getX(), a.getY(), a.getVisible() });
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return col == 4 ? Boolean.class : String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 4;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        super.setValueAt(value, row, col);
        if (col == 4) {
            modelList.getModels().get(row).setVisible((boolean) value);
        }
    }
}
