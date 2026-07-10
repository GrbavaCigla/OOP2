package io.github.GrbavaCigla.ui.components;

import javax.swing.JFormattedTextField;
import java.text.NumberFormat;

import io.github.GrbavaCigla.ui.formatters.CoordinateFormatter;

public class CoordinateField extends JFormattedTextField {

    public CoordinateField(int limit) {
        super(createFormatter(limit));
        setFocusLostBehavior(JFormattedTextField.PERSIST);
    }

    private static CoordinateFormatter createFormatter(int limit) {
        NumberFormat format = NumberFormat.getNumberInstance();

        return new CoordinateFormatter(format, limit);
    }

    public int getValueAsInt() {
        Object v = getValue();
        return (v instanceof Number) ? ((Number) v).intValue() : 0;
    }

    public void setValue(int value) {
        super.setValue(value);
    }
}