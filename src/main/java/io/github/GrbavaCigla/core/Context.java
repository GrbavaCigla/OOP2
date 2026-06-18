package io.github.GrbavaCigla.core;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import io.github.GrbavaCigla.io.CSV;
import io.github.GrbavaCigla.io.Format;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class Context {
    private static Context instance;

    private ModelList<Airport> airportModelList;
    private ModelList<Flight> flightModelList;

    private Context() {
        airportModelList = new ModelList<>();
        airportModelList.addUniqueConstraint(Airport::getCode);
        airportModelList.addFormat(Format.CSV, new CSV<Airport>() {
            @Override
            protected String[] getColumns() {
                return new String[] { "Name", "Code", "X", "Y" };
            }

            @Override
            protected String[] toRow(Airport a) {
                return new String[] {
                        a.getName(), a.getCode(),
                        String.valueOf(a.getX()), String.valueOf(a.getY()),
                };
            }

            @Override
            protected Airport fromRow(String[] p) {
                Airport a = new Airport(p[0], p[1], Float.parseFloat(p[2]), Float.parseFloat(p[3]));
                if (p.length > 4)
                    a.setVisible(Boolean.parseBoolean(p[4]));
                return a;
            }
        });

        flightModelList = new ModelList<>();
        flightModelList.addFormat(Format.CSV, new CSV<Flight>() {
            @Override
            protected String[] getColumns() {
                return new String[] { "Origin", "Destination", "Start", "Duration" };
            }

            @Override
            protected String[] toRow(Flight f) {
                return new String[] {
                        f.getOrigin().getCode(),
                        f.getDestination().getCode(),
                        f.getStart().toString(),
                        f.getFormattedDuration()
                };
            }

            @Override
            protected Flight fromRow(String[] p) {
                List<Airport> airports = airportModelList.getModels();
                Airport origin = airports.stream()
                        .filter(a -> a.getCode().equals(p[0])).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown airport code: " + p[0]));
                Airport destination = airports.stream()
                        .filter(a -> a.getCode().equals(p[1])).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown airport code: " + p[1]));
                LocalTime start = LocalTime.parse(p[2]);
                String[] durationParts = p[3].split(":");
                Duration duration = Duration.ofHours(Long.parseLong(durationParts[0]))
                        .plusMinutes(Long.parseLong(durationParts[1]));
                return new Flight(origin, destination, start, duration);
            }
        });
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
