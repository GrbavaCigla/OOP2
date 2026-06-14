package io.github.GrbavaCigla.formatters;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;

public class CodeFormatter extends DefaultFormatter {

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null) {
            throw new ParseException("Null value", 0);
        }

        text = text.trim().toUpperCase();

        if (!text.matches("[A-Z]{3}")) {
            throw new ParseException("Must be 3 uppercase letters", 0);
        }

        return text;
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null)
            return "";
        return value.toString();
    }
}