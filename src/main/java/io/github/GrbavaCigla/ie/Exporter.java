package io.github.GrbavaCigla.ie;

import java.io.BufferedWriter;
import java.util.List;

public interface Exporter<T> {
    void dump(BufferedWriter wr, List<T> data);
}
