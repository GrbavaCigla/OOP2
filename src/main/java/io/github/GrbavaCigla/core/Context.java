package io.github.GrbavaCigla.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class Context {
    private static Context instance;

    private ModelList<Airport> airportModelList;
    private ModelList<Flight> flightModelList;
    private Properties properties;

    private Context() {
        airportModelList = new ModelList<Airport>();
        flightModelList = new ModelList<Flight>();
        properties = new Properties();
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

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void loadProperties(InputStream input) throws IOException {
        properties.load(input);
    }
}
