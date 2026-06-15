package io.github.GrbavaCigla.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.core.interfaces.Observer;
import io.github.GrbavaCigla.models.Airport;

public class MapCanvas extends JPanel implements MouseListener, MouseMotionListener {
    List<Airport> airports;
    private Airport selectedAirport = null;
    private boolean selectedIsColored = false;
    private int markerSize;
    Timer timer;

    public MapCanvas() {
        setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);

        Context.getInstance().getAirportModelList().addObservers(itemObserver, listObserver);

        airports = getVisibleAirports();

        String markerSizeString = Context.getInstance().getProperty("airport.marker.size");
        markerSize = Integer.parseInt(markerSizeString);

        timer = new Timer(200, e -> {
            int x = getPixelX(selectedAirport.getX());
            int y = getPixelY(selectedAirport.getY());

            paintImmediately(x - markerSize / 2, y - markerSize / 2, markerSize, markerSize);
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
            drawAirport(g, a, markerSize);
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

    private int getPixelX(float x) {
        String limitString = Context.getInstance().getProperty("airport.x.limit");
        float limit = Integer.parseInt(limitString);
        return Math.round((limit + x) / 2 / (float) limit * getSize().width);
    }

    private int getPixelY(float y) {
        String limitString = Context.getInstance().getProperty("airport.y.limit");
        float limit = Integer.parseInt(limitString);
        return Math.round((limit + y) / 2 / (float) limit * getSize().height);
    }

    private void drawAirport(Graphics g, Airport airport, int markerSize) {
        int x = getPixelX(airport.getX());
        int y = getPixelY(airport.getY());

        g.setColor(airport == selectedAirport && selectedIsColored ? Color.RED : Color.LIGHT_GRAY);
        g.fillRect(x - markerSize / 2, y - markerSize / 2, markerSize, markerSize);

        g.setColor(Color.black);
        g.setFont(new Font(g.getFont().getFontName(), 400, 10));
        g.drawString(airport.getCode(), x + markerSize / 2 + 2, y);
    }

    public void mouseClicked(MouseEvent e) {
        for (Airport a : airports) {
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
                    selectedAirport = null;
                    selectedIsColored = false;
                } else {
                    timer.stop();
                    selectedAirport = a;
                    selectedIsColored = true;
                    timer.start();
                }
                repaint();
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
