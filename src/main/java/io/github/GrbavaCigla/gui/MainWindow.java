package io.github.GrbavaCigla.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.gui.components.MapCanvas;
import io.github.GrbavaCigla.gui.components.ModelPanel;
import io.github.GrbavaCigla.gui.dialogs.AirportDialog;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class MainWindow extends Frame {
    private MapCanvas map;
    private ModelPanel<Airport> airportsPanel;
    private ModelPanel<Flight> flightsPanel;

    public MainWindow() {
        Context ctx = Context.getInstance();
        Properties props = ctx.getProperties();

        String title = props.getProperty("project.name");
        Dimension size = new Dimension(
                Integer.parseInt(props.getProperty("window.width")),
                Integer.parseInt(props.getProperty("window.height")));

        setTitle(title);
        setSize(size);
        setLayout(new BorderLayout());

        map = new MapCanvas();
        airportsPanel = new ModelPanel<Airport>("Airport", ctx.getAirportModelList(),
                (e) -> new AirportDialog(this, e));
        flightsPanel = new ModelPanel<Flight>("Flights", ctx.getFlightModelList(),
                (e) -> new AirportDialog(this, null));

        add(map, BorderLayout.CENTER);
        add(airportsPanel, BorderLayout.WEST);
        add(flightsPanel, BorderLayout.EAST);

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent e) {
                // TODO: Reset timer
            }
        }, (1L << 20) - 1);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }
}
