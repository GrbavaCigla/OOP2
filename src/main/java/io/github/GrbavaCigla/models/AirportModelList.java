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
                return new String[] { "Name", "Code", "X", "Y" };
            }

            @Override
            protected String[] toRow(Airport a) {
                return new String[] {
                        a.getName(), a.getCode(),
                        String.valueOf(a.getX()), String.valueOf(a.getY())
                };
            }

            @Override
            protected Airport fromRow(String[] p) {
                Airport a = new Airport(p[0], p[1], Float.parseFloat(p[2]), Float.parseFloat(p[3]));
                if (p.length > 4) a.setVisible(Boolean.parseBoolean(p[4]));
                return a;
            }
        };
    }

    private JSON<Airport> createJsonFormat() {
        return new JSON<Airport>() {
            @Override
            protected Map<String, Object> toObject(Airport a) {
                Map<String, Object> map = new HashMap<>();
                map.put("Name", a.getName());
                map.put("Code", a.getCode());
                map.put("X", a.getX());
                map.put("Y", a.getY());
                return map;
            }

            @Override
            protected Airport fromObject(Map<String, String> f) {
                return new Airport(f.get("Name"), f.get("Code"),
                        Float.parseFloat(f.get("X")), Float.parseFloat(f.get("Y")));
            }
        };
    }
}
