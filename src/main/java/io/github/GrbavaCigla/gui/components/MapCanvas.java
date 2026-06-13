package io.github.GrbavaCigla.gui.components;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.Timer;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.core.Observer;
import io.github.GrbavaCigla.models.Airport;

public class MapCanvas extends Canvas implements MouseListener, MouseMotionListener {
    private Airport selectedAirport = null;
    private boolean selectedIsColored = false;
    Timer timer;

    public MapCanvas() {
        setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);

        String markerSizeString = Context.getInstance().getProperties().getProperty("airport.marker.size");
        int markerSize = Integer.parseInt(markerSizeString);

        timer = new Timer(200, e -> {
            drawAirport(selectedAirport, markerSize, selectedIsColored ? Color.red : Color.lightGray);
            selectedIsColored = !selectedIsColored;
        });
    }

    private List<Airport> getVisibleAirports() {
        return Context.getInstance().getAirportModelList().getModels().stream().filter(a -> a.getVisible()).toList();
    }

    public void paint(Graphics g) {
        redraw(getVisibleAirports());
    }

    public final Observer<Airport> itemObserver = (observable, model) -> {
        redraw(getVisibleAirports());
    };

    public final Observer<List<Airport>> listObserver = (observable, modelList) -> {
        redraw(getVisibleAirports());
    };

    private int getPixelX(float x) {
        String limitString = Context.getInstance().getProperties().getProperty("airport.x.limit");
        float limit = Integer.parseInt(limitString);
        return Math.round((limit + x) / 2 / (float) limit * getSize().width);
    }

    private int getPixelY(float y) {
        String limitString = Context.getInstance().getProperties().getProperty("airport.y.limit");
        float limit = Integer.parseInt(limitString);
        return Math.round((limit + y) / 2 / (float) limit * getSize().height);
    }

    private void clear() {
        Graphics g = getGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    private void drawAirport(Airport airport, int markerSize, Color color) {
        drawAirport(airport, markerSize, color, false);
    }

    private void drawAirport(Airport airport, int markerSize, Color color, boolean withText) {
        Graphics g = getGraphics();
        int x = getPixelX(airport.getX());
        int y = getPixelY(airport.getY());

        g.setColor(color);
        g.fillRect(x - markerSize / 2, y - markerSize / 2, markerSize, markerSize);

        if (withText) {
            g.setColor(Color.black);
            g.setFont(new Font(g.getFont().getFontName(), 400, 10));
            g.drawString(airport.getCode(), x + markerSize / 2 + 2, y);
        }
    }

    private void drawAirports() {
        Context ctx = Context.getInstance();
        ctx.getAirportModelList().addObservers(itemObserver, listObserver);

        String markerSizeString = ctx.getProperties().getProperty("airport.marker.size");
        int markerSize = Integer.parseInt(markerSizeString);

        for (Airport a : getVisibleAirports()) {
            drawAirport(a, markerSize, Color.lightGray, true);
        }

        if (!getVisibleAirports().contains(selectedAirport)) {
            timer.stop();
            selectedAirport = null;
            selectedIsColored = false;
        } 
    }

    public void redraw(List<Airport> airportList) {
        clear();

        Graphics g = getGraphics();

        g.setColor(Color.red);
        g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);

        drawAirports();
    }

    public void mouseClicked(MouseEvent e) {
        String markerSizeString = Context.getInstance().getProperties().getProperty("airport.marker.size");
        int markerSize = Integer.parseInt(markerSizeString);

        for (Airport a : getVisibleAirports()) {
            int x = getPixelX(a.getX());
            int y = getPixelY(a.getY());

            if (Math.abs(e.getX() - x) < markerSize && Math.abs(e.getY() - y) < markerSize) {
                if (selectedAirport == null) {
                    timer.stop();
                    selectedAirport = a;
                    selectedIsColored = true;
                    timer.restart();
                } else if (a == selectedAirport) {
                    timer.stop();
                    drawAirport(a, markerSize, Color.lightGray);
                    selectedAirport = null;
                    selectedIsColored = false;
                } else {
                    timer.stop();
                    drawAirport(selectedAirport, markerSize, Color.lightGray);
                    selectedAirport = a;
                    selectedIsColored = true;
                    timer.start();
                }
                break;
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
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
