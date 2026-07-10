package io.github.GrbavaCigla.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.io.CSV;
import io.github.GrbavaCigla.io.Format;
import io.github.GrbavaCigla.io.JSON;

public class FlightModelList extends ModelList<Flight> {
    private final ModelList<Airport> airportModelList;

    public FlightModelList(ModelList<Airport> airportModelList) {
        this.airportModelList = airportModelList;
        airportModelList.addObserver((obs, airports) -> unlink());
        addFormat(Format.CSV, createCsvFormat());
        addFormat(Format.JSON, createJsonFormat());
    }

    private void unlink() {
        List<Airport> airports = airportModelList.getModels();
        for (Flight flight : getModels()) {
            if (flight.getOrigin() != null && !airports.contains(flight.getOrigin())) {
                flight.setOrigin(null);
            }
            if (flight.getDestination() != null && !airports.contains(flight.getDestination())) {
                flight.setDestination(null);
            }
        }
    }

    private CSV<Flight> createCsvFormat() {
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
                        f.getOrigin() == null ? "null" : f.getOrigin().getCode(),
                        f.getDestination() == null ? "null" : f.getDestination().getCode(),
                        f.getStart().toString(),
                        f.getFormattedDuration()
                };
            }

            @Override
            protected Flight fromRow(String[] p) {
                Airport origin = airportLookup.get(p[0]);
                if (origin == null && !"null".equals(p[0])) throw new IllegalArgumentException("Unknown airport code: " + p[0]);
                Airport destination = airportLookup.get(p[1]);
                if (destination == null && !"null".equals(p[1])) throw new IllegalArgumentException("Unknown airport code: " + p[1]);
                LocalTime start = LocalTime.parse(p[2]);
                String[] durationParts = p[3].split(":");
                Duration duration = Duration.ofHours(Long.parseLong(durationParts[0]))
                        .plusMinutes(Long.parseLong(durationParts[1]));
                return new Flight(origin, destination, start, duration);
            }
        };
    }

    private JSON<Flight> createJsonFormat() {
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
                map.put("Origin", f.getOrigin() == null ? null : f.getOrigin().getCode());
                map.put("Destination", f.getDestination() == null ? null : f.getDestination().getCode());
                map.put("Start", f.getStart().toString());
                map.put("Duration", f.getFormattedDuration());
                return map;
            }

            @Override
            protected Flight fromObject(Map<String, String> f) {
                Airport origin = airportLookup.get(f.get("Origin"));
                if (origin == null && !"null".equals(f.get("Origin"))) throw new IllegalArgumentException("Unknown airport code: " + f.get("Origin"));
                Airport destination = airportLookup.get(f.get("Destination"));
                if (destination == null && !"null".equals(f.get("Destination"))) throw new IllegalArgumentException("Unknown airport code: " + f.get("Destination"));
                LocalTime start = LocalTime.parse(f.get("Start"));
                String[] durationParts = f.get("Duration").split(":");
                Duration duration = Duration.ofHours(Long.parseLong(durationParts[0]))
                        .plusMinutes(Long.parseLong(durationParts[1]));
                return new Flight(origin, destination, start, duration);
            }
        };
    }
}
