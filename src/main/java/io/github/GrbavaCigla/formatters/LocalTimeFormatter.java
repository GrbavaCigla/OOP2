package io.github.GrbavaCigla.formatters;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeFormatter extends DefaultFormatter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Object stringToValue(String text) throws ParseException {
        try {
            return LocalTime.parse(text, formatter);
        } catch (Exception e) {
            throw new ParseException("Invalid time format. Use HH:mm", 0);
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof LocalTime) {
            return ((LocalTime) value).format(formatter);
        }
        return "";
    }
}