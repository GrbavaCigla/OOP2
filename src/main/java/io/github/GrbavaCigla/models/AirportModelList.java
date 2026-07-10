package io.github.GrbavaCigla.models;

import java.util.HashMap;
import java.util.Map;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.io.CSV;
import io.github.GrbavaCigla.io.Format;
import io.github.GrbavaCigla.io.JSON;

public class AirportModelList extends ModelList<Airport> {
    public AirportModelList() {
        addUniqueConstraint(Airport::getCode);
        addFormat(Format.CSV, createCsvFormat());
        addFormat(Format.JSON, createJsonFormat());
    }

    private CSV<Airport> createCsvFormat() {
        return new CSV<Airport>() {
            @Override
            protected String[] getColumns() {
                return new String[] { "CODE", "NAME", "X", "Y" };
            }

            @Override
            protected String[] toRow(Airport a) {
                return new String[] {
                        a.getCode(), a.getName(),
                        String.valueOf(a.getX()), String.valueOf(a.getY())
                };
            }

            @Override
            protected Airport fromRow(String[] p) {
                return new Airport(p[1], p[0], Integer.parseInt(p[2]), Integer.parseInt(p[3]));
            }
        };
    }

    private JSON<Airport> createJsonFormat() {
        return new JSON<Airport>() {
            @Override
            protected Map<String, Object> toObject(Airport a) {
                Map<String, Object> map = new HashMap<>();
                map.put("code", a.getCode());
                map.put("name", a.getName());
                map.put("x", a.getX());
                map.put("y", a.getY());
                return map;
            }

            @Override
            protected Airport fromObject(Map<String, String> f) {
                return new Airport(f.get("name"), f.get("code"),
                        Integer.parseInt(f.get("x")), Integer.parseInt(f.get("y")));
            }
        };
    }
}
