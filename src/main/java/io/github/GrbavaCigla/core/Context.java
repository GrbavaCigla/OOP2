package io.github.GrbavaCigla.core;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.AirportModelList;
import io.github.GrbavaCigla.models.Flight;
import io.github.GrbavaCigla.models.FlightModelList;

public class Context {
    private static Context instance;

    private ModelList<Airport> airportModelList;
    private ModelList<Flight> flightModelList;
    private final Set<Consumer<Boolean>> simulationObservers = new HashSet<>();

    private Context() {
        airportModelList = new AirportModelList();
        flightModelList = new FlightModelList(airportModelList);
    }

    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public ModelList<Airport> getAirportModelList() {
        return airportModelList;
    }

    public ModelList<Flight> getFlightModelList() {
        return flightModelList;
    }

    public void addSimulationObserver(Consumer<Boolean> observer) {
        simulationObservers.add(observer);
    }

    public void setSimulationRunning(boolean running) {
        simulationObservers.forEach(obs -> obs.accept(running));
    }
}
