package io.github.GrbavaCigla.ui.components;

import javax.swing.JFormattedTextField;
import java.text.NumberFormat;

import io.github.GrbavaCigla.formatters.CoordinateFormatter;

public class CoordinateField extends JFormattedTextField {

    public CoordinateField(float limit) {
        super(createFormatter(limit));
        setFocusLostBehavior(JFormattedTextField.PERSIST);
    }

    private static CoordinateFormatter createFormatter(float limit) {
        NumberFormat format = NumberFormat.getNumberInstance();

        return new CoordinateFormatter(format, limit);
    }

    public float getValueAsFloat() {
        Object v = getValue();
        return (v instanceof Number) ? ((Number) v).floatValue() : 0f;
    }

    public void setValue(float value) {
        super.setValue(value);
    }
}