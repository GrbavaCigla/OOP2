package io.github.GrbavaCigla.models;

import java.time.Duration;
import java.time.LocalTime;

import io.github.GrbavaCigla.core.Observable;

public class Flight extends Observable<Flight> {
    Airport origin;
    Airport destination;
    LocalTime start;
    Duration duration;

    public Flight(Airport origin, Airport destination, LocalTime start, Duration duration) {
        this.origin = origin;
        this.destination = destination;
        this.start = start;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return origin + " -> " + destination + " (" + start.toString() + ")";
    }
}
