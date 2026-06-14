package io.github.GrbavaCigla.gui.components;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;

public class SimulationControls extends Panel {
    Label timerLabel;

    public SimulationControls() {
        super(new BorderLayout());

        setBackground(Color.lightGray);

        Panel actionsPanel = new Panel(new FlowLayout());

        actionsPanel.add(new Button("Start"));
        actionsPanel.add(new Button("Stop/Resume"));
        actionsPanel.add(new Button("Reset"));

        timerLabel = new Label();

        add(actionsPanel, BorderLayout.CENTER);
        add(timerLabel);
    }
}
