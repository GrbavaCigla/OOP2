package io.github.GrbavaCigla.simulation;

import java.time.Duration;
import java.time.LocalTime;

import io.github.GrbavaCigla.models.Flight;

public class ScheduledFlight {
    private Flight flight;
    private LocalTime actualDeparture;

    public ScheduledFlight(Flight flight, LocalTime actualDeparture) {
        this.flight = flight;
        this.actualDeparture = actualDeparture;
    }

    public Flight getFlight() {
        return flight;
    }

    public LocalTime getActualDeparture() {
        return actualDeparture;
    }

    public LocalTime getActualArrival() {
        return actualDeparture.plus(flight.getDuration());
    }

    public float[] getPosition(LocalTime current) {
        long elapsed = Duration.between(actualDeparture, current).toMinutes();
        long total = flight.getDuration().toMinutes();

        float progress = total == 0 ? 1.0f : Math.min(1.0f, (float) elapsed / total);

        float originX = flight.getOrigin().getX();
        float originY = flight.getOrigin().getY();
        float destinationX = flight.getDestination().getX();
        float destinationY = flight.getDestination().getY();

        float x = originX + (destinationX - originX) * progress;
        float y = originY + (destinationY - originY) * progress;

        return new float[] { x, y };
    }

    public boolean isActive(LocalTime current) {
        return !current.isBefore(actualDeparture) && current.isBefore(getActualArrival());
    }
}
