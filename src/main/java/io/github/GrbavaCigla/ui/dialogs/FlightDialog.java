package io.github.GrbavaCigla.ui.dialogs;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.time.Duration;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.formatters.LocalTimeSpinnerModel;
import io.github.GrbavaCigla.formatters.DurationFormatter;
import io.github.GrbavaCigla.formatters.DurationSpinnerModel;
import io.github.GrbavaCigla.formatters.LocalTimeFormatter;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class FlightDialog extends DerivedDialog {
    private JComboBox<Airport> originField;
    private JComboBox<Airport> destinationField;
    private JSpinner startField;
    private JSpinner durationField;
    private Flight flight;

    public FlightDialog(Component parent, Flight flight) {
        this(findFrame(parent), flight);
    }

    public FlightDialog(Component parent) {
        this(findFrame(parent), null);
    }

    public FlightDialog(Frame owner, Flight flight) {
        super(owner, flight == null ? "Add flight" : "Edit flight", true);
        this.flight = flight;

        JPanel contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            public Insets getInsets() {
                return new Insets(10, 10, 10, 10);
            }
        };

        Airport[] airports = Context.getInstance().getAirportModelList().getModels().toArray(new Airport[0]);

        originField = new JComboBox<>(airports);
        destinationField = new JComboBox<>(airports);
        startField = new JSpinner(new LocalTimeSpinnerModel());
        durationField = new JSpinner(new DurationSpinnerModel());

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) startField.getEditor();
        editor.getTextField().setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new LocalTimeFormatter()));
        editor.getTextField().setEditable(true);

        editor = (JSpinner.DefaultEditor) durationField.getEditor();
        editor.getTextField().setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new DurationFormatter()));
        editor.getTextField().setEditable(true);

        addField(contentPanel, "Origin", originField);
        addField(contentPanel, "Destination", destinationField);
        addField(contentPanel, "Start Time", startField);
        addField(contentPanel, "Duration", durationField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> onSubmit());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = getGridYCounter() + 1;
        constraints.insets = new Insets(10, 0, 0, 0);
        contentPanel.add(submitButton, constraints);

        add(contentPanel);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dispose();
            }
        });

        if (flight != null) {
            updateFields(flight);
        }

        pack();
        setSize(300, getSize().height);
        setLocationRelativeTo(getParent());
    }

    private void onSubmit() {
        try {
            startField.commitEdit();
            durationField.commitEdit();

            Airport origin = (Airport) originField.getSelectedItem();
            Airport destination = (Airport) destinationField.getSelectedItem();
            LocalTime start = (LocalTime) startField.getValue();
            Duration duration = (Duration) durationField.getValue();

            if (flight == null) {
                Context.getInstance()
                        .getFlightModelList()
                        .add(new Flight(origin, destination, start, duration));
            } else {
                Flight newFlight = new Flight(origin, destination, start, duration);
                Context.getInstance().getFlightModelList().validate(newFlight, flight);
                flight.update(newFlight);
            }

            dispose();
        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFields(Flight flight) {
        originField.setSelectedItem(flight.getOrigin());
        destinationField.setSelectedItem(flight.getDestination());
        startField.setValue(flight.getStart());
        durationField.setValue(flight.getDuration());
    }
}