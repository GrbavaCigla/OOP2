package io.github.GrbavaCigla.models;

import java.time.Duration;
import java.time.LocalTime;

import io.github.GrbavaCigla.core.Observable;
import io.github.GrbavaCigla.core.interfaces.Tabulatable;

public class Flight extends Observable<Flight> implements Tabulatable {
    private Airport origin;
    private Airport destination;
    private LocalTime start;
    private Duration duration;

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

    @Override
    public Object[] getRow() {
        return new Object[] { origin.getName(), destination.getName() };
    }

    @Override
    public Object[] getColumns() {
        return new Object[] { "Origin", "Destination" };
    }

    public Airport getOrigin() {
        return origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public LocalTime getStart() {
        return start;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setOrigin(Airport origin) {
        if (this.origin == origin)
            return;
        this.origin = origin;
        notifyObservers(this);
    }

    public void setDestination(Airport destination) {
        if (this.destination == destination)
            return;
        this.destination = destination;
        notifyObservers(this);
    }

    public void setStart(LocalTime start) {
        if (this.start == start)
            return;
        this.start = start;
        notifyObservers(this);
    }

    public void setDuration(Duration duration) {
        if (this.duration == duration)
            return;
        this.duration = duration;
        notifyObservers(this);
    }

    public void update(Flight flight) {
        if (this.destination == flight.destination
                && this.origin == flight.origin
                && this.start == flight.start
                && this.duration == flight.duration)
            return;

        this.origin = flight.origin;
        this.destination = flight.destination;
        this.duration = flight.duration;
        this.start = flight.start;

        notifyObservers(this);
    }
}
