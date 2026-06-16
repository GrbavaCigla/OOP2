package io.github.GrbavaCigla.simulation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.core.Observable;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class FlightScheduler extends Observable<FlightScheduler> {
    private static FlightScheduler instance;
    private LocalTime time;
    private List<ScheduledFlight> schedule = new ArrayList<>();

    private int step;

    private FlightScheduler() {
        step = Integer.parseInt(Context.getInstance().getProperty("simulation.stepMinutes"));
        reset();
    }

    public static FlightScheduler getInstance() {
        if (instance == null) {
            instance = new FlightScheduler();
        }
        return instance;
    }

    public boolean isRunning() {
        return !LocalTime.MIDNIGHT.equals(time);
    }

    public void setTime(LocalTime time) {
        boolean old = isRunning();
        this.time = time;
        if (isRunning() == old) notifyObservers(this);
    }

    public LocalTime getTime() {
        return time;
    }

    public List<ScheduledFlight> step() {
        time = time.plusMinutes(step);
        List<ScheduledFlight> active = getActiveFlights();
        return active;
    }

    public void reset() {
        time = LocalTime.MIDNIGHT;
        precalculateSchedules();
        notifyObservers(this);
    }

    public List<ScheduledFlight> getSchedule() {
        return schedule;
    }

    public List<ScheduledFlight> getActiveFlights() {
        return schedule.stream()
                .filter(sf -> sf.isActive(time))
                .toList();
    }

    private LocalTime roundUpToStep(LocalTime time) {
        int reminder = time.getMinute() % step;
        if (reminder == 0)
            return time;
        return time.plusMinutes(step - reminder);
    }

    private void precalculateSchedules() {
        schedule.clear();
        Map<Airport, LocalTime> nextAvailableMap = new HashMap<>();
        List<Flight> flights = new ArrayList<>(
                Context.getInstance().getFlightModelList().getModels());

        flights.sort(Comparator.comparing(Flight::getStart));

        for(Flight flight : flights) {
            Airport origin = flight.getOrigin();
            LocalTime rounded = roundUpToStep(flight.getStart());
            LocalTime nextAvailable = nextAvailableMap.getOrDefault(origin, LocalTime.MIDNIGHT);
            LocalTime actualDeparture = rounded.isBefore(nextAvailable) ? nextAvailable : rounded;

            nextAvailableMap.put(origin, actualDeparture.plusMinutes(step));
            schedule.add(new ScheduledFlight(flight, actualDeparture));
        }
    }
}
