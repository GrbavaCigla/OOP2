package io.github.GrbavaCigla.simulation;

import io.github.GrbavaCigla.core.Constants;
import io.github.GrbavaCigla.core.Observable;

public class Simulation extends Observable<Boolean> {
    private static Simulation instance;

    private final FlightScheduler scheduler = FlightScheduler.getInstance();
    private final Thread loop;
    private volatile boolean running = false;

    private Simulation() {
        loop = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (running) {
                    scheduler.step();
                }
                try {
                    Thread.sleep(Constants.SIM_REAL_MICROSTEP_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        loop.setDaemon(true);
        loop.start();
    }

    public static synchronized Simulation getInstance() {
        if (instance == null) {
            instance = new Simulation();
        }
        return instance;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (running) return;
        scheduler.recalculate();
        running = true;
        notifyObservers(running);
    }

    public void stop() {
        if (!running) return;
        running = false;
        notifyObservers(running);
    }

    public void toggle() {
        if (running) stop();
        else start();
    }

    public void reset() {
        stop();
        scheduler.reset();
    }
}
