package io.github.GrbavaCigla.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.gui.components.Map;
import io.github.GrbavaCigla.gui.components.ModelPanel;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class MainWindow extends Frame {
    private Canvas map;
    private ModelPanel<Airport> airportsPanel;
    private ModelPanel<Flight> flightsPanel;

    public MainWindow(Properties props) {
        String title = props.getProperty("project.name");
        Dimension size = new Dimension(
                Integer.parseInt(props.getProperty("window.width")),
                Integer.parseInt(props.getProperty("window.height")));

        setTitle(title);
        setSize(size);
        setLayout(new BorderLayout());

        map = new Map();
        airportsPanel = new ModelPanel<Airport>("Airport", Context.getInstance().getAirportModelList());
        flightsPanel = new ModelPanel<Flight>("Flights", Context.getInstance().getFlightModelList());

        add(map, BorderLayout.CENTER);
        add(airportsPanel, BorderLayout.WEST);
        add(flightsPanel, BorderLayout.EAST);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }
}
