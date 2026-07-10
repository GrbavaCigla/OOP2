package io.github.GrbavaCigla.simulation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.GrbavaCigla.core.Constants;
import io.github.GrbavaCigla.core.ModelStore;
import io.github.GrbavaCigla.core.Observable;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class FlightScheduler extends Observable<FlightScheduler> {
    private static FlightScheduler instance;
    private LocalTime time;
    private List<ScheduledFlight> schedule = new ArrayList<>();

    private FlightScheduler() {
        reset();
    }

    public static synchronized FlightScheduler getInstance() {
        if (instance == null) {
            instance = new FlightScheduler();
        }
        return instance;
    }

    public synchronized boolean isRunning() {
        return !LocalTime.MIDNIGHT.equals(time);
    }

    public synchronized LocalTime getTime() {
        return time;
    }

    public synchronized List<ScheduledFlight> step() {
        time = time.plusMinutes(Constants.SIM_MICROSTEP_MINUTES);
        notifyObservers(this);
        return getActiveFlights();
    }

    public synchronized void reset() {
        time = LocalTime.MIDNIGHT;
        notifyObservers(this);
    }

    public List<ScheduledFlight> getSchedule() {
        return Collections.unmodifiableList(schedule);
    }

    public synchronized List<ScheduledFlight> getActiveFlights() {
        return schedule.stream()
                .filter(sf -> sf.isActive(time))
                .toList();
    }

    private static LocalTime roundUpToStep(LocalTime time) {
        int remainder = time.getMinute() % Constants.SIM_STEP_MINUTES;
        if (remainder == 0)
            return time;
        return time.plusMinutes(Constants.SIM_STEP_MINUTES - remainder);
    }

    public synchronized void recalculate() {
        schedule.clear();
        Map<Airport, LocalTime> nextAvailableMap = new HashMap<>();
        List<Flight> flights = new ArrayList<>(
                ModelStore.getFlightModelList().getModels());

        flights.sort(Comparator.comparing(Flight::getStart));

        for (Flight flight : flights) {
            if (!flight.isValid())
                continue;
            Airport origin = flight.getOrigin();
            LocalTime rounded = roundUpToStep(flight.getStart());
            LocalTime nextAvailable = nextAvailableMap.getOrDefault(origin, LocalTime.MIDNIGHT);
            LocalTime actualDeparture = rounded.isBefore(nextAvailable) ? nextAvailable : rounded;

            nextAvailableMap.put(origin, actualDeparture.plusMinutes(Constants.SIM_STEP_MINUTES));
            schedule.add(new ScheduledFlight(flight, actualDeparture));
        }
    }
}
