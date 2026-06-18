package io.github.GrbavaCigla.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.github.GrbavaCigla.io.Exporter;
import io.github.GrbavaCigla.io.Format;
import io.github.GrbavaCigla.io.Importer;

public class ModelList<T extends Observable<T>> extends Observable<List<T>> {
    private List<T> data = new ArrayList<>();
    private List<Function<T, Object>> uniqueConstraintGetters = new ArrayList<>();
    private Map<Format, Importer<T>> importers = new EnumMap<>(Format.class);
    private Map<Format, Exporter<T>> exporters = new EnumMap<>(Format.class);

    public void addImporter(Format format, Importer<T> importer) {
        importers.put(format, importer);
    }

    public void addExporter(Format format, Exporter<T> exporter) {
        exporters.put(format, exporter);
    }

    public <U extends Importer<T> & Exporter<T>> void addFormat(Format format, U handler) {
        importers.put(format, handler);
        exporters.put(format, handler);
    }

    public void add(T item) {
        validate(item);
        data.add(item);
        notifyObservers(getModels());
    }

    public void remove(int index) {
        if (data.remove(index) != null) {
            notifyObservers(getModels());
        }
    }

    public void clear() {
        if (!data.isEmpty()) {
            data.clear();
            notifyObservers(getModels());
        }
    }

    public List<T> getModels() {
        return Collections.unmodifiableList(data);
    }

    public void addObservers(Observer<T> o, Observer<List<T>> os) {
        addObserver(os);
        for (T d : data) {
            d.addObserver(o);
        }
    }

    public void addUniqueConstraint(Function<T, Object> uniqueConstraint) {
        uniqueConstraintGetters.add(uniqueConstraint);
    }

    public void validate(T item) {
        validate(item, null);
    }

    public void validate(T item, T old) {
        for (Function<T, Object> constraint : uniqueConstraintGetters) {
            Object field = constraint.apply(item);
            for (T d : data) {
                if (d == old)
                    continue;
                if (constraint.apply(d).equals(field)) {
                    throw new IllegalStateException("Unique constraint violated for value: " + field);
                }
            }
        }
    }

    public void dump(Format format, Path path) throws IOException {
        Exporter<T> exporter = exporters.get(format);
        if (exporter == null)
            throw new UnsupportedOperationException("Format not registered: " + format);
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            exporter.dump(bw, data);
        }
    }

    public void load(Format format, Path path) throws IOException {
        Importer<T> importer = importers.get(format);
        if (importer == null)
            throw new UnsupportedOperationException("Format not registered: " + format);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            List<T> loaded = importer.load(br);
            data.clear();
            for (T item : loaded) {
                validate(item);
                data.add(item);
            }
            notifyObservers(getModels());
        }
    }
}
