package io.github.GrbavaCigla.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.gui.components.MapCanvas;
import io.github.GrbavaCigla.gui.components.ModelPanel;
import io.github.GrbavaCigla.gui.components.SimulationControls;
import io.github.GrbavaCigla.gui.dialogs.AirportDialog;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class MainWindow extends JFrame {
    private MapCanvas map;
    private ModelPanel<Airport> airportsPanel;
    private ModelPanel<Flight> flightsPanel;
    private SimulationControls simulationPanel;

    public MainWindow() {
        Context ctx = Context.getInstance();
        String title = ctx.getProperty("project.name");
        Dimension size = new Dimension(
                Integer.parseInt(ctx.getProperty("window.width")),
                Integer.parseInt(ctx.getProperty("window.height")));

        setTitle(title);
        setSize(size);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        map = new MapCanvas();
        airportsPanel = new ModelPanel<>("Airport", ctx.getAirportModelList(),
                (e) -> new AirportDialog(this, e));
        flightsPanel = new ModelPanel<>("Flights", ctx.getFlightModelList(),
                (e) -> new AirportDialog(this, null));
        simulationPanel = new SimulationControls();

        mainPanel.add(map, BorderLayout.CENTER);
        mainPanel.add(simulationPanel, BorderLayout.NORTH);

        add(airportsPanel, BorderLayout.WEST);
        add(flightsPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent e) -> {
            // TODO: Reset timer
        }, (1L << 20) - 1);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);
    }
}