package io.github.GrbavaCigla.ui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.simulation.FlightScheduler;

public class SimulationControls extends JPanel {
    private JLabel timerLabel;
    private FlightScheduler scheduler;
    private volatile boolean running = false;
    private Thread simulationThread;

    public SimulationControls() {
        super(new BorderLayout());

        scheduler = FlightScheduler.getInstance();
        scheduler.addObserver((e, s) -> handleSchedulerUpdate(s));

        JPanel actionsPanel = new JPanel(new FlowLayout());

        JButton toggleButton = new JButton("Start/Stop");
        toggleButton.addActionListener(this::toggleSimulation);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(this::resetSimulation);

        actionsPanel.add(toggleButton);
        actionsPanel.add(resetButton);

        timerLabel = new JLabel(LocalTime.MIDNIGHT.toString());

        add(actionsPanel, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.EAST);

        simulationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (running) {
                    scheduler.step();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    private void handleSchedulerUpdate(FlightScheduler s) {
        updateTimerLabel(s.getTime());
    }

    private void toggleSimulation(ActionEvent event) {
        running = !running;
        Context ctx = Context.getInstance();
        if (running) {
            scheduler.recalculate();
            ctx.getInactivityTimer().suspend();
        } else {
            ctx.getInactivityTimer().resume();
        }
        ctx.setSimulationRunning(running);
    }

    private void resetSimulation(ActionEvent event) {
        if (running) {
            running = false;
            Context ctx = Context.getInstance();
            ctx.getInactivityTimer().resume();
            ctx.setSimulationRunning(false);
        }
        scheduler.reset();
    }

    private void updateTimerLabel(LocalTime time) {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText(time.toString());
        });
    }
}