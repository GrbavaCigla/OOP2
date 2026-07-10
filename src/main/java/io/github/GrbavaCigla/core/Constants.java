package io.github.GrbavaCigla.core;

import java.time.Duration;

public class Constants {
    public static final int WINDOW_HEIGHT = 700;
    public static final int WINDOW_WIDTH = 1000;
    public static final int AIRPORT_MARKER_SIZE = 14;
    public static final int AIRPORT_X_LIMIT = 180;
    public static final int AIRPORT_Y_LIMIT = 90;
    public static final int SIM_STEP_MINUTES = 10;
    public static final int SIM_MICROSTEP_MINUTES = 2;
    public static final int SIM_REAL_MICROSTEP_MS = 200;
    public static final Duration FLIGHT_DEFAULT_DURATION = Duration.ofHours(1);
    public static final Duration FLIGHT_MIN_DURATION = Duration.ofMinutes(1);
    public static final Duration FLIGHT_STEP_DURATION = Duration.ofMinutes(30);
    public static final Duration FLIGHT_STEP_TIME = Duration.ofHours(1);
    public static final int INACTIVITY_DELAY_MS = 55_000;
    public static final int INACTIVITY_CLOSE_DELAY_MS = 5_000;
}
