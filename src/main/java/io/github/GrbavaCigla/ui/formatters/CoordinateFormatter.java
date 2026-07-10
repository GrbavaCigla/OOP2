package io.github.GrbavaCigla.ui.formatters;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.text.NumberFormatter;

public class CoordinateFormatter extends NumberFormatter {
    private int limit;

    public CoordinateFormatter(NumberFormat format, int limit) {
        super(format);
        this.limit = limit;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        Object value = super.stringToValue(text);
        int v = ((Number) value).intValue();

        if (v < -limit || v > limit) {
            throw new ParseException("Out of range", 0);
        }

        return v;
    }
}
