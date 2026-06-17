package io.github.GrbavaCigla.formatters;

import java.time.Duration;
import javax.swing.AbstractSpinnerModel;
import io.github.GrbavaCigla.core.Constants;

public class DurationSpinnerModel extends AbstractSpinnerModel {
    private Duration duration;
    private final Duration minDuration;
    private final Duration stepDuration;

    public DurationSpinnerModel() {
        this(
            Constants.FLIGHT_DEFAULT_DURATION,
            Constants.FLIGHT_MIN_DURATION,
            Constants.FLIGHT_STEP_DURATION
        );
    }

    public DurationSpinnerModel(Duration duration, Duration minDuration, Duration stepDuration) {
        this.duration = duration;
        this.minDuration = minDuration;
        this.stepDuration = stepDuration;
    }

    @Override
    public Object getValue() {
        return duration;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Duration) {
            this.duration = (Duration) value;
            fireStateChanged();
        }
    }

    @Override
    public Object getNextValue() {
        return duration.plus(stepDuration);
    }

    @Override
    public Object getPreviousValue() {
        Duration prev = duration.minus(stepDuration);
        return (prev.compareTo(minDuration) < 0) ? null : prev;
    }
}