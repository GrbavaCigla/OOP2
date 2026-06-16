package io.github.GrbavaCigla.simulation;


import io.github.GrbavaCigla.core.Observable;

public class FlightScheduler extends Observable<FlightScheduler> {
    private static FlightScheduler instance;
    private SimulatorState state = SimulatorState.TERMINATED;
    // private LocalTime time = new LocalTime();

    public enum SimulatorState {
        RUNNING,
        PAUSED,
        TERMINATED,
    }

    private FlightScheduler() {
    }

    public static FlightScheduler getInstance() {
        if (instance == null) {
            instance = new FlightScheduler();
        }
        return instance;
    }

    public SimulatorState getState() {
        return state;
    }

    public void start() {
        state = SimulatorState.RUNNING;
        notifyObservers(this);
    }

    public void stop() {
        state = SimulatorState.TERMINATED;
        notifyObservers(this);
    }
}
