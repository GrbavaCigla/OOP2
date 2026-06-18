package io.github.GrbavaCigla.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public interface Importer<T> {
    List<T> load(BufferedReader rd) throws IOException;
}
