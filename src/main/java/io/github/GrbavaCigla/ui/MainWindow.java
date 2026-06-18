package io.github.GrbavaCigla.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import io.github.GrbavaCigla.core.Constants;
import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;
import io.github.GrbavaCigla.ui.components.MapCanvas;
import io.github.GrbavaCigla.ui.components.ModelPanel;
import io.github.GrbavaCigla.ui.components.SimulationControls;
import io.github.GrbavaCigla.ui.dialogs.AirportDialog;
import io.github.GrbavaCigla.ui.dialogs.FlightDialog;
import io.github.GrbavaCigla.ui.models.AirportTableModel;
import io.github.GrbavaCigla.ui.models.FlightTableModel;

public class MainWindow extends JFrame {
    public MainWindow(String title) {
        Context ctx = Context.getInstance();
        Dimension size = new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        setTitle(title);
        setSize(size);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        MapCanvas map = new MapCanvas();
        SimulationControls simulationPanel = new SimulationControls();

        AirportTableModel airportTableModel = new AirportTableModel(ctx.getAirportModelList());
        FlightTableModel flightTableModel = new FlightTableModel(ctx.getFlightModelList());

        ModelPanel<Airport> airportsPanel = new ModelPanel<>(
                "Airport",
                ctx.getAirportModelList(),
                airportTableModel,
                (e) -> new AirportDialog(this, e));

        ModelPanel<Flight> flightsPanel = new ModelPanel<>(
                "Flights",
                ctx.getFlightModelList(),
                flightTableModel,
                (e) -> new FlightDialog(this, e));

        mainPanel.add(map, BorderLayout.CENTER);
        mainPanel.add(simulationPanel, BorderLayout.NORTH);

        add(airportsPanel, BorderLayout.WEST);
        add(flightsPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent e) -> {
            ctx.getInactivityTimer().reset();
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

        ctx.getInactivityTimer().start();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
