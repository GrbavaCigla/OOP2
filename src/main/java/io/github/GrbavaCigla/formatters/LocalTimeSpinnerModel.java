
package io.github.GrbavaCigla.formatters;

import java.time.Duration;
import java.time.LocalTime;

import javax.swing.AbstractSpinnerModel;
import io.github.GrbavaCigla.core.Constants;

public class LocalTimeSpinnerModel extends AbstractSpinnerModel {
    private LocalTime time;
    private Duration stepDuration;

    public LocalTimeSpinnerModel() {
        this(
            LocalTime.now(),
            Constants.FLIGHT_STEP_TIME
        );
    }

    public LocalTimeSpinnerModel(LocalTime time, Duration stepDuration) {
        this.time = time;
        this.stepDuration = stepDuration;
    }

    @Override
    public Object getValue() {
        return time;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof LocalTime) {
            this.time = (LocalTime) value;
            fireStateChanged();
        }
    }

    @Override
    public Object getNextValue() {
        return time.plus(stepDuration);
    }

    @Override
    public Object getPreviousValue() {
        return time.minus(stepDuration);
    }
}