package io.github.GrbavaCigla.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public interface Exporter<T> {
    void dump(BufferedWriter wr, List<T> data) throws IOException;
}
