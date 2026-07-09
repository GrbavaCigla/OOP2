package io.github.GrbavaCigla.ui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.github.GrbavaCigla.simulation.FlightScheduler;
import io.github.GrbavaCigla.simulation.Simulation;
import io.github.GrbavaCigla.ui.InactivityTimer;

public class SimulationControls extends JPanel {
    private JLabel timerLabel;
    private JButton toggleButton;

    public SimulationControls() {
        super(new BorderLayout());

        FlightScheduler.getInstance().addObserver((e, s) -> handleSchedulerUpdate(s));

        JPanel actionsPanel = new JPanel(new FlowLayout());

        toggleButton = new JButton("Start");
        toggleButton.addActionListener(e -> Simulation.getInstance().toggle());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> Simulation.getInstance().reset());

        actionsPanel.add(toggleButton);
        actionsPanel.add(resetButton);

        timerLabel = new JLabel(LocalTime.MIDNIGHT.toString());

        Simulation.getInstance().addObserver((o, running) -> handleRunningChange(running));

        add(actionsPanel, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.EAST);
    }

    private void handleSchedulerUpdate(FlightScheduler s) {
        updateTimerLabel(s.getTime());
    }

    private void handleRunningChange(boolean running) {
        toggleButton.setText(running ? "Stop" : "Start");
        if (running) {
            InactivityTimer.getInstance().suspend();
        } else {
            InactivityTimer.getInstance().resume();
        }
    }

    private void updateTimerLabel(LocalTime time) {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText(time.toString());
        });
    }
}