package io.github.GrbavaCigla.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import io.github.GrbavaCigla.core.Constants;
import io.github.GrbavaCigla.core.ModelStore;
import io.github.GrbavaCigla.core.Observer;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.simulation.FlightScheduler;
import io.github.GrbavaCigla.simulation.ScheduledFlight;
import io.github.GrbavaCigla.ui.InactivityTimer;

public class MapCanvas extends JPanel {
    private List<Airport> airports;
    private List<ScheduledFlight> flights;
    private Airport selectedAirport = null;
    private boolean selectedIsColored = false;
    private JLabel mousePositionLabel;
    private FlightScheduler scheduler;
    private Timer timer;
    private float zoom = 1.0f;

    public MapCanvas() {
        setBackground(Color.white);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                onMouseClicked(e);
            }

            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        mousePositionLabel = new JLabel("X: 0, Y: 0");
        add(mousePositionLabel, BorderLayout.NORTH);

        ModelStore.getAirportModelList().addObservers(itemObserver, listObserver);

        scheduler = FlightScheduler.getInstance();
        scheduler.addObserver(simObserver);

        airports = getVisibleAirports();
        flights = scheduler.getActiveFlights();

        timer = new Timer(200, e -> {
            int x = getPixelX(selectedAirport.getX());
            int y = getPixelY(selectedAirport.getY());

            paintImmediately(
                    x - Constants.AIRPORT_MARKER_SIZE / 2,
                    y - Constants.AIRPORT_MARKER_SIZE / 2,
                    Constants.AIRPORT_MARKER_SIZE,
                    Constants.AIRPORT_MARKER_SIZE);
            Toolkit.getDefaultToolkit().sync();
            selectedIsColored = !selectedIsColored;
        });
    }

    public void zoomIn() {
        zoom = Math.min(1.0f, zoom + 0.1f);
        repaint();
    }

    public void zoomOut() {
        zoom = Math.max(0.1f, zoom - 0.1f);
        repaint();
    }

    private List<Airport> getVisibleAirports() {
        return ModelStore.getAirportModelList().getModels().stream().filter(a -> a.getVisible()).toList();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.red);
        g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

        for (Airport a : airports) {
            drawAirport(g, a);
        }

        for (ScheduledFlight sf : flights) {
            drawFlight(g, sf);
        }
    }

    public final Observer<Airport> itemObserver = (observable, model) -> {
        airports = getVisibleAirports();
        repaint();
    };

    public final Observer<List<Airport>> listObserver = (observable, modelList) -> {
        airports = getVisibleAirports();
        repaint();
    };

    public final Observer<FlightScheduler> simObserver = (observable, scheduler) -> {
        flights = scheduler.getActiveFlights();
        repaint();
    };

    private int getPixelX(float x) {
        return Math.round((Constants.AIRPORT_X_LIMIT + x * zoom) / 2 / Constants.AIRPORT_X_LIMIT * getWidth());
    }

    private int getPixelY(float y) {
        return Math.round((Constants.AIRPORT_Y_LIMIT - y * zoom) / 2 / Constants.AIRPORT_Y_LIMIT * getHeight());
    }

    private float getCoordinateX(int x) {
        return (((x * 2.0f * Constants.AIRPORT_X_LIMIT) / getWidth()) - Constants.AIRPORT_X_LIMIT) / zoom;
    }

    private float getCoordinateY(int y) {
        return (Constants.AIRPORT_Y_LIMIT - (y * 2.0f * Constants.AIRPORT_Y_LIMIT) / getHeight()) / zoom;
    }

    private void drawAirport(Graphics g, Airport airport) {
        int x = getPixelX(airport.getX());
        int y = getPixelY(airport.getY());

        g.setColor(airport == selectedAirport && selectedIsColored ? Color.RED : Color.LIGHT_GRAY);
        g.fillRect(
                x - Constants.AIRPORT_MARKER_SIZE / 2,
                y - Constants.AIRPORT_MARKER_SIZE / 2,
                Constants.AIRPORT_MARKER_SIZE,
                Constants.AIRPORT_MARKER_SIZE);

        g.setColor(Color.black);
        g.setFont(new Font(g.getFont().getFontName(), 400, 10));
        g.drawString(airport.getCode(), x + Constants.AIRPORT_MARKER_SIZE / 2 + 2, y);
    }

    private void drawFlight(Graphics g, ScheduledFlight flight) {
        float[] pos = flight.getPosition(scheduler.getTime());

        int px = getPixelX(pos[0]);
        int py = getPixelY(pos[1]);

        g.setColor(Color.BLUE);

        g.fillOval(
                px - Constants.FLIGHT_MARKER_SIZE / 2,
                py - Constants.FLIGHT_MARKER_SIZE / 2,
                Constants.FLIGHT_MARKER_SIZE,
                Constants.FLIGHT_MARKER_SIZE);
    }

    protected void onAirportClicked(Airport airport) {
        if (selectedAirport == null) {
            timer.stop();
            selectedAirport = airport;
            selectedIsColored = true;
            timer.restart();
            InactivityTimer.getInstance().suspend();
        } else if (airport == selectedAirport) {
            timer.stop();
            selectedAirport = null;
            selectedIsColored = false;
            InactivityTimer.getInstance().resume();
        } else {
            timer.stop();
            selectedAirport = airport;
            selectedIsColored = true;
            timer.start();
        }
        repaint();
    }

    protected void onFlightClicked(ScheduledFlight flight) {
    }

    private void onMouseClicked(MouseEvent e) {
        for (Airport a : airports) {
            int x = getPixelX(a.getX());
            int y = getPixelY(a.getY());

            if (Math.abs(e.getX() - x) < Constants.AIRPORT_MARKER_SIZE / 2
                    && Math.abs(e.getY() - y) < Constants.AIRPORT_MARKER_SIZE / 2) {
                onAirportClicked(a);
                return;
            }
        }

        for (ScheduledFlight sf : flights) {
            float[] pos = sf.getPosition(scheduler.getTime());
            int px = getPixelX(pos[0]);
            int py = getPixelY(pos[1]);

            int dx = e.getX() - px;
            int dy = e.getY() - py;
            int r = Constants.FLIGHT_MARKER_SIZE / 2;
            if (dx * dx + dy * dy < r * r) {
                onFlightClicked(sf);
                return;
            }
        }
    }

    private void onMouseMoved(MouseEvent e) {
        float coordX = getCoordinateX(e.getX());
        float coordY = getCoordinateY(e.getY());

        mousePositionLabel.setText(String.format("X: %.2f, Y: %.2f", coordX, coordY));
    }
}
