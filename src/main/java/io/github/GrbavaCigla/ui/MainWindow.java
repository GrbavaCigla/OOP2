package io.github.GrbavaCigla.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import io.github.GrbavaCigla.core.Constants;
import io.github.GrbavaCigla.core.ModelStore;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;
import io.github.GrbavaCigla.simulation.Simulation;
import io.github.GrbavaCigla.ui.components.MapCanvas;
import io.github.GrbavaCigla.ui.components.ModelPanel;
import io.github.GrbavaCigla.ui.components.SimulationControls;
import io.github.GrbavaCigla.ui.dialogs.AirportDialog;
import io.github.GrbavaCigla.ui.dialogs.FlightDialog;
import io.github.GrbavaCigla.ui.models.AirportTableModel;
import io.github.GrbavaCigla.ui.models.FlightTableModel;

public class MainWindow extends JFrame {
    private JMenu importMenu;

    public MainWindow(String title) {
        Dimension size = new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        setTitle(title);
        setSize(size);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());

        Simulation.getInstance().addObserver((o, running) -> importMenu.setEnabled(!running));

        JPanel mainPanel = new JPanel(new BorderLayout());

        MapCanvas map = new MapCanvas();
        SimulationControls simulationPanel = new SimulationControls();

        AirportTableModel airportTableModel = new AirportTableModel(ModelStore.getAirportModelList());
        FlightTableModel flightTableModel = new FlightTableModel(ModelStore.getFlightModelList());

        ModelPanel<Airport> airportsPanel = new ModelPanel<>(
                "Airport",
                ModelStore.getAirportModelList(),
                airportTableModel,
                (e) -> new AirportDialog(this, e));

        ModelPanel<Flight> flightsPanel = new ModelPanel<>(
                "Flights",
                ModelStore.getFlightModelList(),
                flightTableModel,
                (e) -> new FlightDialog(this, e));

        mainPanel.add(map, BorderLayout.CENTER);
        mainPanel.add(simulationPanel, BorderLayout.NORTH);

        add(airportsPanel, BorderLayout.WEST);
        add(flightsPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent e) -> {
            InactivityTimer.getInstance().reset();
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

        InactivityTimer.getInstance().start();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        importMenu = new JMenu("Import");
        JMenuItem importCsvItem = new JMenuItem("CSV");
        importCsvItem.addActionListener(e -> importCsv());
        JMenuItem importJsonItem = new JMenuItem("JSON");
        importJsonItem.addActionListener(e -> {});
        importMenu.add(importCsvItem);
        importMenu.add(importJsonItem);

        JMenu exportMenu = new JMenu("Export");
        JMenuItem exportCsvItem = new JMenuItem("CSV");
        exportCsvItem.addActionListener(e -> {});
        JMenuItem exportJsonItem = new JMenuItem("JSON");
        exportJsonItem.addActionListener(e -> {});
        exportMenu.add(exportCsvItem);
        exportMenu.add(exportJsonItem);

        fileMenu.add(importMenu);
        fileMenu.add(exportMenu);

        menuBar.add(fileMenu);
        return menuBar;
    }

    private void importCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = chooser.getSelectedFile().toPath();
        try {
            ModelStore.loadCsv(path);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Import failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
