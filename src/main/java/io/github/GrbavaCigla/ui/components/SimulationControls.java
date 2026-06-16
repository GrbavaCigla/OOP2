package io.github.GrbavaCigla.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class SimulationControls extends JPanel {
    JLabel timerLabel;

    public SimulationControls() {
        super(new BorderLayout());

        setBackground(Color.lightGray);

        JPanel actionsPanel = new JPanel(new FlowLayout());

        actionsPanel.add(new JButton("Start"));
        actionsPanel.add(new JButton("Stop/Resume"));
        actionsPanel.add(new JButton("Reset"));

        timerLabel = new JLabel("");

        add(actionsPanel, BorderLayout.CENTER);
        add(timerLabel);
    }
}
