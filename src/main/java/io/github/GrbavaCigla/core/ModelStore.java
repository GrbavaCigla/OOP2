package io.github.GrbavaCigla.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.GrbavaCigla.io.Format;
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

    public static void loadCsv(Path path) throws IOException {
        Map<String, List<String>> sections = splitSections(path);
        loadSection(getAirportModelList(), sections.get("AIRPORTS"));
        loadSection(getFlightModelList(), sections.get("FLIGHTS"));
    }

    public static void dumpCsv(Path path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write("# AIRPORTS");
            bw.newLine();
            getAirportModelList().dump(Format.CSV, bw);
            bw.newLine();
            bw.write("# FLIGHTS");
            bw.newLine();
            getFlightModelList().dump(Format.CSV, bw);
        }
    }

    private static Map<String, List<String>> splitSections(Path path) throws IOException {
        Map<String, List<String>> sections = new HashMap<>();
        List<String> current = null;
        for (String line : Files.readAllLines(path)) {
            if (line.startsWith("#")) {
                current = new ArrayList<>();
                sections.put(line.substring(1).trim(), current);
            } else if (current != null) {
                current.add(line);
            }
        }
        return sections;
    }

    private static <T extends Observable<T>> void loadSection(ModelList<T> model, List<String> lines) throws IOException {
        if (lines == null) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new StringReader(String.join("\n", lines)))) {
            model.load(Format.CSV, br);
        }
    }
}
