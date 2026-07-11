package io.github.GrbavaCigla.simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import io.github.GrbavaCigla.models.Flight;

public class ScheduledFlight {
    private Flight flight;
    private LocalTime actualDeparture;
    private final List<float[]> waypoints = new ArrayList<>();
    private float speed;

    public ScheduledFlight(Flight flight, LocalTime actualDeparture) {
        this.flight = flight;
        this.actualDeparture = actualDeparture;
        waypoints.add(new float[] { flight.getOrigin().getX(), flight.getOrigin().getY() });
        waypoints.add(new float[] { flight.getDestination().getX(), flight.getDestination().getY() });
        calculateSpeed();
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

    public void addWaypoint(float x, float y) {
        waypoints.add(waypoints.size() - 1, new float[] { x, y });
        calculateSpeed();
    }

    private static float distance(float[] a, float[] b) {
        float dx = b[0] - a[0];
        float dy = b[1] - a[1];
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void calculateSpeed() {
        float totalLength = 0;
        for (int i = 0; i < waypoints.size() - 1; i++) {
            totalLength += distance(waypoints.get(i), waypoints.get(i + 1));
        }

        long total = flight.getDuration().toMinutes();
        speed = total == 0 ? totalLength : totalLength / total;
    }

    public float[] getPosition(LocalTime current) {
        long elapsed = Duration.between(actualDeparture, current).toMinutes();
        float remaining = speed * Math.max(0, elapsed);

        for (int i = 0; i < waypoints.size() - 1; i++) {
            float[] from = waypoints.get(i);
            float[] to = waypoints.get(i + 1);
            float length = distance(from, to);

            if (remaining <= length) {
                float t = length == 0 ? 1.0f : remaining / length;
                return new float[] {
                        from[0] + (to[0] - from[0]) * t,
                        from[1] + (to[1] - from[1]) * t,
                };
            }
            remaining -= length;
        }

        return waypoints.get(waypoints.size() - 1);
    }

    public boolean isActive(LocalTime current) {
        return !current.isBefore(actualDeparture) && current.isBefore(getActualArrival());
    }
}
