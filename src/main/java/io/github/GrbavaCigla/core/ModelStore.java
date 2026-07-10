package io.github.GrbavaCigla.core;

import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.AirportModelList;
import io.github.GrbavaCigla.models.Flight;
import io.github.GrbavaCigla.models.FlightModelList;

public class ModelStore {
    private static ModelStore instance;

    private ModelList<Airport> airportModelList;
    private ModelList<Flight> flightModelList;

    private ModelStore() {
        airportModelList = new AirportModelList();
        flightModelList = new FlightModelList(airportModelList);
    }

    public static ModelStore getInstance() {
        if (instance == null) {
            instance = new ModelStore();
        }
        return instance;
    }

    public static ModelList<Airport> getAirportModelList() {
        return getInstance().airportModelList;
    }

    public static ModelList<Flight> getFlightModelList() {
        return getInstance().flightModelList;
    }
}
