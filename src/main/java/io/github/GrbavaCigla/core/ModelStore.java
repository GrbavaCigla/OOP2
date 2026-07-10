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

    public static void load(Format format, Path path) throws IOException {
        switch (format) {
            case CSV -> loadCsv(path);
            case JSON -> loadJson(path);
        }
    }

    private static void loadCsv(Path path) throws IOException {
        Map<String, List<String>> sections = splitSections(path);
        loadSection(getAirportModelList(), sections.get("AIRPORTS"));
        loadSection(getFlightModelList(), sections.get("FLIGHTS"));
    }

    private static void loadJson(Path path) throws IOException {
        String content = Files.readString(path);
        loadJsonSection(getAirportModelList(), content, "airports");
        loadJsonSection(getFlightModelList(), content, "flights");
    }

    public static void dump(Format format, Path path) throws IOException {
        switch (format) {
            case CSV -> dumpCsv(path);
            case JSON -> dumpJson(path);
        }
    }

    private static void dumpCsv(Path path) throws IOException {
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

    private static void dumpJson(Path path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write("{");
            bw.newLine();
            bw.write("\"airports\":");
            getAirportModelList().dump(Format.JSON, bw);
            bw.write(",");
            bw.newLine();
            bw.write("\"flights\":");
            getFlightModelList().dump(Format.JSON, bw);
            bw.newLine();
            bw.write("}");
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

    private static <T extends Observable<T>> void loadSection(ModelList<T> model, List<String> lines)
            throws IOException {
        if (lines == null) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new StringReader(String.join("\n", lines)))) {
            model.load(Format.CSV, br);
        }
    }

    private static <T extends Observable<T>> void loadJsonSection(ModelList<T> model, String content, String key)
            throws IOException {
        String array = extractArray(content, key);
        if (array == null) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new StringReader(array))) {
            model.load(Format.JSON, br);
        }
    }

    private static String extractArray(String content, String key) {
        int keyIndex = content.indexOf("\"" + key + "\"");
        if (keyIndex == -1) {
            return null;
        }
        int start = content.indexOf('[', keyIndex);
        int end = content.indexOf(']', start);
        if (start == -1 || end == -1) {
            return null;
        }
        return content.substring(start, end + 1);
    }
}
