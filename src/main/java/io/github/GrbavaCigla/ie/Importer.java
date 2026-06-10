package io.github.GrbavaCigla.ie;

import java.io.BufferedReader;
import java.util.List;

public interface Importer<T> {
    List<T> load(BufferedReader rd);
}
