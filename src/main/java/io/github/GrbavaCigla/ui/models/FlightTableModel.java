package io.github.GrbavaCigla.ui.models;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.core.Observer;
import io.github.GrbavaCigla.models.Flight;

public class FlightTableModel extends DefaultTableModel {
    private static final String[] COLUMNS = { "Origin", "Destination", "Start", "Duration" };

    private Observer<Flight> itemObserver;
    private Observer<List<Flight>> listObserver;

    public FlightTableModel(ModelList<Flight> modelList) {
        super(COLUMNS, 0);
        this.itemObserver = (obs, flight) -> refresh(modelList.getModels());
        this.listObserver = (obs, flights) -> {
            modelList.addObservers(this.itemObserver, this.listObserver);
            refresh(flights);
        };
        modelList.addObservers(itemObserver, listObserver);
        refresh(modelList.getModels());
    }

    private void refresh(List<Flight> flights) {
        setRowCount(0);
        for (Flight f : flights) {
            addRow(new Object[] {
                    f.getOrigin() == null ? "-" : f.getOrigin().getCode(),
                    f.getDestination() == null ? "-" : f.getDestination().getCode(),
                    f.getStart().toString(),
                    f.getFormattedDuration()
            });
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
