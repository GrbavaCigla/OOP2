package io.github.GrbavaCigla.ui.formatters;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.text.NumberFormatter;

public class CoordinateFormatter extends NumberFormatter {
    private float limit;

    public CoordinateFormatter(NumberFormat format, float limit) {
        super(format);
        this.limit = limit;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        Object value = super.stringToValue(text);
        float v = ((Number) value).floatValue();

        if (v < -limit || v > limit) {
            throw new ParseException("Out of range", 0);
        }

        return v;
    }
}
