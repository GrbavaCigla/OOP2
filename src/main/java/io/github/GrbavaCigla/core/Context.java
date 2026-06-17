package io.github.GrbavaCigla.core;

import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class Context {
    private static Context instance;

    private ModelList<Airport> airportModelList;
    private ModelList<Flight> flightModelList;

    private Context() {
        airportModelList = new ModelList<>();
        flightModelList = new ModelList<>();
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
}
