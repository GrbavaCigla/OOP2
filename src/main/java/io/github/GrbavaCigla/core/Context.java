package io.github.GrbavaCigla.core;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Timer;
// import java.util.TimerTask;
// import java.util.function.Supplier;

import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class Context {
    private static Context instance;

    private ModelList<Airport> airportModelList;
    private ModelList<Flight> flightModelList;
    // private Timer timer;
    // private List<Supplier<TimerTask>> tasksFactory;
    // private List<Long> delays;
    // private List<TimerTask> tasks;

    private Context() {
        airportModelList = new ModelList<Airport>();
        flightModelList = new ModelList<Flight>();
    //     timer = new Timer(true);
    //     tasksFactory = new ArrayList<Supplier<TimerTask>>();
    //     delays = new ArrayList<Long>();
    //     tasks = new ArrayList<TimerTask>();
    }

    public static Context getInstance() {
        if(instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public ModelList<Airport> getAirportModelList() {
        return airportModelList;
    }

    public ModelList<Flight> getFlightModelList() {
        return flightModelList;
    }

    // public void addTimerTask(Supplier<TimerTask> tt, long delay) {
    //     tasksFactory.add(tt);
    //     TimerTask task = tt.get();
    //     tasks.add(task);
    //     delays.add(delay);
    //     timer.schedule(task, delay);
    // }

    // public void resetTimer() {
    //     for(TimerTask task : tasks) {
    //         task.cancel();
    //     }
    //     tasks.clear();
    //     for(Supplier<TimerTask> taskFactory : tasksFactory) {
    //         TimerTask task = taskFactory.get();
    //         tasks.add(task);
    //         timer.schedule(task, delays);
    //     }
    // }
}
