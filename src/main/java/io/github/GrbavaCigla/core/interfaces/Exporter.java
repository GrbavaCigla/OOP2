package io.github.GrbavaCigla.core.interfaces;

import java.io.BufferedWriter;
import java.util.List;

public interface Exporter<T> {
    void dump(BufferedWriter wr, List<T> data);
}
