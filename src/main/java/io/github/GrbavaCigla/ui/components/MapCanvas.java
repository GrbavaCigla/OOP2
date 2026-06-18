package io.github.GrbavaCigla.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import io.github.GrbavaCigla.core.Constants;
import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.core.Observer;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.simulation.FlightScheduler;
import io.github.GrbavaCigla.simulation.ScheduledFlight;

public class MapCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private List<Airport> airports;
    private List<ScheduledFlight> flights;
    private Airport selectedAirport = null;
    private boolean selectedIsColored = false;
    private JLabel mousePositionLabel;
    private FlightScheduler scheduler;
    private Timer timer;

    public MapCanvas() {
        setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);

        mousePositionLabel = new JLabel("X: 0, Y: 0");
        add(mousePositionLabel, BorderLayout.NORTH);

        Context ctx = Context.getInstance();
        ctx.getAirportModelList().addObservers(itemObserver, listObserver);

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

    private List<Airport> getVisibleAirports() {
        return Context.getInstance().getAirportModelList().getModels().stream().filter(a -> a.getVisible()).toList();
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
        return Math.round((Constants.AIRPORT_X_LIMIT + x) / 2 / Constants.AIRPORT_X_LIMIT * getSize().width);
    }

    private int getPixelY(float y) {
        return Math.round((Constants.AIRPORT_Y_LIMIT - y) / (2.0f * Constants.AIRPORT_Y_LIMIT) * getHeight());
    }

    private float getCoordinateX(int x) {
        return ((x * 2.0f * Constants.AIRPORT_X_LIMIT) / getSize().width) - Constants.AIRPORT_X_LIMIT;
    }

    private float getCoordinateY(int y) {
        return Constants.AIRPORT_Y_LIMIT - ((y * 2.0f * Constants.AIRPORT_Y_LIMIT) / getHeight());
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
        Airport origin = flight.getFlight().getOrigin();
        Airport destination = flight.getFlight().getDestination();

        int x1 = getPixelX(origin.getX());
        int y1 = getPixelY(origin.getY());

        int x2 = getPixelX(destination.getX());
        int y2 = getPixelY(destination.getY());

        float[] pos = flight.getPosition(scheduler.getTime());

        int px = getPixelX(pos[0]);
        int py = getPixelY(pos[1]);

        double dx = x2 - x1;
        double dy = y2 - y1;

        double len = Math.sqrt(dx * dx + dy * dy);

        if (len == 0)
            return;

        dx /= len;
        dy /= len;

        int arrowLength = 10;
        int arrowWidth = 5;

        int leftX = (int) (px - dx * arrowLength - dy * arrowWidth);
        int leftY = (int) (py - dy * arrowLength + dx * arrowWidth);

        int rightX = (int) (px - dx * arrowLength + dy * arrowWidth);
        int rightY = (int) (py - dy * arrowLength - dx * arrowWidth);

        g.setColor(Color.BLUE);

        g.fillPolygon(
                new int[] { px, leftX, rightX },
                new int[] { py, leftY, rightY },
                3);
    }

    public void mouseClicked(MouseEvent e) {
        for (Airport a : airports) {
            int x = getPixelX(a.getX());
            int y = getPixelY(a.getY());

            if (Math.abs(e.getX() - x) < Constants.AIRPORT_MARKER_SIZE
                    && Math.abs(e.getY() - y) < Constants.AIRPORT_MARKER_SIZE) {
                if (selectedAirport == null) {
                    timer.stop();
                    selectedAirport = a;
                    selectedIsColored = true;
                    timer.restart();
                    Context.getInstance().getInactivityTimer().suspend();
                } else if (a == selectedAirport) {
                    timer.stop();
                    selectedAirport = null;
                    selectedIsColored = false;
                    Context.getInstance().getInactivityTimer().resume();
                } else {
                    timer.stop();
                    selectedAirport = a;
                    selectedIsColored = true;
                    timer.start();
                    Context.getInstance().getInactivityTimer().suspend();
                }
                repaint();
                break;
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        float coordX = getCoordinateX(e.getX());
        float coordY = getCoordinateY(e.getY());

        mousePositionLabel.setText(String.format("X: %.2f, Y: %.2f", coordX, coordY));
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }
}
