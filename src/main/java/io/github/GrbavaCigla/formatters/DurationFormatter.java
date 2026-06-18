package io.github.GrbavaCigla.formatters;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;
import java.time.Duration;

public class DurationFormatter extends DefaultFormatter {
    @Override
    public Object stringToValue(String text) throws ParseException {
        try {
            String[] parts = text.split(":");
            if (parts.length != 2) {
                throw new ParseException("Invalid format. Use HH:mm", 0);
            }

            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            if (minutes >= 60) {
                hours += minutes / 60;
                minutes = minutes % 60;
            }

            return Duration.ofHours(hours).plusMinutes(minutes);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new ParseException("Invalid format. Use HH:mm", 0);
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof Duration) {
            Duration duration = (Duration) value;
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            return String.format("%d:%02d", hours, minutes);
        }
        return "";
    }
}