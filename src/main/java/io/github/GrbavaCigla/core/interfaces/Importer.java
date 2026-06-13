package io.github.GrbavaCigla.core.interfaces;

import java.io.BufferedReader;
import java.util.List;

public interface Importer<T> {
    List<T> load(BufferedReader rd);
}
