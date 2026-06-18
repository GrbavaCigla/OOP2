package io.github.GrbavaCigla.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.GrbavaCigla.io.CSV;
import io.github.GrbavaCigla.io.Format;
import io.github.GrbavaCigla.io.JSON;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class Context {
    private static Context instance;

    private ModelList<Airport> airportModelList;
    private ModelList<Flight> flightModelList;
    private InactivityTimer inactivityTimer = new InactivityTimer();

    private Context() {
        airportModelList = new ModelList<>();
        airportModelList.addUniqueConstraint(Airport::getCode);
        airportModelList.addFormat(Format.CSV, createAirportCsvFormat());
        airportModelList.addFormat(Format.JSON, createAirportJsonFormat());

        flightModelList = new ModelList<>();
        flightModelList.addFormat(Format.CSV, createFlightCsvFormat());
        flightModelList.addFormat(Format.JSON, createFlightJsonFormat());
    }

    private CSV<Airport> createAirportCsvFormat() {
        return new CSV<Airport>() {
            @Override
            protected String[] getColumns() {
                return new String[] { "Name", "Code", "X", "Y" };
            }

            @Override
            protected String[] toRow(Airport a) {
                return new String[] {
                        a.getName(), a.getCode(),
                        String.valueOf(a.getX()), String.valueOf(a.getY())
                };
            }

            @Override
            protected Airport fromRow(String[] p) {
                Airport a = new Airport(p[0], p[1], Float.parseFloat(p[2]), Float.parseFloat(p[3]));
                if (p.length > 4) a.setVisible(Boolean.parseBoolean(p[4]));
                return a;
            }
        };
    }

    private JSON<Airport> createAirportJsonFormat() {
        return new JSON<Airport>() {
            @Override
            protected Map<String, Object> toObject(Airport a) {
                Map<String, Object> map = new HashMap<>();
                map.put("Name", a.getName());
                map.put("Code", a.getCode());
                map.put("X", a.getX());
                map.put("Y", a.getY());
                return map;
            }

            @Override
            protected Airport fromObject(Map<String, String> f) {
                return new Airport(f.get("Name"), f.get("Code"),
                        Float.parseFloat(f.get("X")), Float.parseFloat(f.get("Y")));
            }
        };
    }

    private CSV<Flight> createFlightCsvFormat() {
        return new CSV<Flight>() {
            private Map<String, Airport> airportLookup;

            @Override
            public List<Flight> load(BufferedReader rd) throws IOException {
                airportLookup = airportModelList.getModels().stream()
                        .collect(Collectors.toMap(Airport::getCode, a -> a));
                try {
                    return super.load(rd);
                } finally {
                    airportLookup = null;
                }
            }

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
                Airport origin = airportLookup.get(p[0]);
                if (origin == null) throw new IllegalArgumentException("Unknown airport code: " + p[0]);
                Airport destination = airportLookup.get(p[1]);
                if (destination == null) throw new IllegalArgumentException("Unknown airport code: " + p[1]);
                LocalTime start = LocalTime.parse(p[2]);
                String[] durationParts = p[3].split(":");
                Duration duration = Duration.ofHours(Long.parseLong(durationParts[0]))
                        .plusMinutes(Long.parseLong(durationParts[1]));
                return new Flight(origin, destination, start, duration);
            }
        };
    }

    private JSON<Flight> createFlightJsonFormat() {
        return new JSON<Flight>() {
            private Map<String, Airport> airportLookup;

            @Override
            public List<Flight> load(BufferedReader rd) throws IOException {
                airportLookup = airportModelList.getModels().stream()
                        .collect(Collectors.toMap(Airport::getCode, a -> a));
                try {
                    return super.load(rd);
                } finally {
                    airportLookup = null;
                }
            }

            @Override
            protected Map<String, Object> toObject(Flight f) {
                Map<String, Object> map = new HashMap<>();
                map.put("Origin", f.getOrigin().getCode());
                map.put("Destination", f.getDestination().getCode());
                map.put("Start", f.getStart().toString());
                map.put("Duration", f.getFormattedDuration());
                return map;
            }

            @Override
            protected Flight fromObject(Map<String, String> f) {
                Airport origin = airportLookup.get(f.get("Origin"));
                if (origin == null) throw new IllegalArgumentException("Unknown airport code: " + f.get("Origin"));
                Airport destination = airportLookup.get(f.get("Destination"));
                if (destination == null) throw new IllegalArgumentException("Unknown airport code: " + f.get("Destination"));
                LocalTime start = LocalTime.parse(f.get("Start"));
                String[] durationParts = f.get("Duration").split(":");
                Duration duration = Duration.ofHours(Long.parseLong(durationParts[0]))
                        .plusMinutes(Long.parseLong(durationParts[1]));
                return new Flight(origin, destination, start, duration);
            }
        };
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

    public InactivityTimer getInactivityTimer() {
        return inactivityTimer;
    }
}
