package io.github.GrbavaCigla.ui;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import io.github.GrbavaCigla.core.Constants;
import io.github.GrbavaCigla.ui.dialogs.InactivityWarningDialog;

public class InactivityTimer {
    private static InactivityTimer instance;

    private final Timer scheduler = new Timer(true);
    private TimerTask inactivityTask;
    private TimerTask closeTask;
    private TimerTask tickTask;
    private InactivityWarningDialog warningDialog;
    private int secondsLeft;
    private int suspended = 0;

    private InactivityTimer() {
    }

    public static InactivityTimer getInstance() {
        if (instance == null) {
            instance = new InactivityTimer();
        }
        return instance;
    }

    public void start() {
        scheduleInactivity();
    }

    public void reset() {
        if (closeTask != null) { closeTask.cancel(); closeTask = null; }
        if (tickTask != null) { tickTask.cancel(); tickTask = null; }
        if (warningDialog != null) { warningDialog.dispose(); warningDialog = null; }
        if (suspended == 0) scheduleInactivity();
    }

    public void suspend() {
        suspended++;
        cancelInactivity();
    }

    public void resume() {
        if (suspended > 0) suspended--;
        if (suspended == 0) scheduleInactivity();
    }

    private synchronized void scheduleInactivity() {
        cancelInactivity();
        inactivityTask = new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(InactivityTimer.this::showWarning);
            }
        };
        scheduler.schedule(inactivityTask, Constants.INACTIVITY_DELAY_MS);
    }

    private synchronized void cancelInactivity() {
        if (inactivityTask != null) {
            inactivityTask.cancel();
            inactivityTask = null;
        }
    }

    private void showWarning() {
        warningDialog = new InactivityWarningDialog();
        warningDialog.setVisible(true);

        secondsLeft = Constants.INACTIVITY_CLOSE_DELAY_MS / 1000;

        closeTask = new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        };
        scheduler.schedule(closeTask, Constants.INACTIVITY_CLOSE_DELAY_MS);

        tickTask = new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (warningDialog != null) warningDialog.updateCountdown(--secondsLeft);
                });
            }
        };
        scheduler.scheduleAtFixedRate(tickTask, 1000, 1000);
    }
}
