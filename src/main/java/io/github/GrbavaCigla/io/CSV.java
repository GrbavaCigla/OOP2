package io.github.GrbavaCigla.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.github.GrbavaCigla.core.Tabulatable;

public class CSV<T extends Tabulatable> implements Importer<T>, Exporter<T> {

    @Override
    public void dump(BufferedWriter wr, List<T> data) throws IOException {
        if (data == null || data.isEmpty())
            return;

        wr.append(String.join(", ", data.get(0).getColumns()));
        for (T item : data) {
            wr.append(Arrays.stream(item.getRow())
                    .map(Object::toString)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse(""));
        }
        wr.flush();
        wr.close();
    }

    @Override
    public List<T> load(BufferedReader rd) {
        return List.of();
    }

}
