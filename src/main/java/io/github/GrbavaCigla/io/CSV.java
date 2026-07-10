package io.github.GrbavaCigla.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CSV<T> implements Importer<T>, Exporter<T> {

    protected abstract String[] getColumns();

    protected abstract String[] toRow(T item);

    protected abstract T fromRow(String[] parts);

    @Override
    public void dump(BufferedWriter wr, List<T> data) throws IOException {
        wr.append(String.join(",", getColumns()));
        for (T item : data) {
            wr.newLine();
            wr.append(String.join(",", toRow(item)));
        }
        wr.flush();
    }

    @Override
    public List<T> load(BufferedReader rd) throws IOException {
        List<T> result = new ArrayList<>();
        List<String> columns = Arrays.asList(getColumns());
        List<String> header = Arrays.stream(rd.readLine().split(",")).map(s -> s.trim()).toList();
        String line;
        while ((line = rd.readLine()) != null) {
            if (line.isBlank())
                continue;

            String[] item = new String[columns.size()];
            String[] fields = line.split(",");
            for (int i = 0; i < fields.length; i++) {
                int index = columns.indexOf(header.get(i));
                if (index == -1)
                    throw new IOException("Unknown column: " + header.get(i));
                item[index] = fields[i].trim();
            }

            result.add(fromRow(item));
        }
        return result;
    }
}
