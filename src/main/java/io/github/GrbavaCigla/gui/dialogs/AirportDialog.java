package io.github.GrbavaCigla.gui.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.gui.components.CodeField;
import io.github.GrbavaCigla.gui.components.CoordinateField;
import io.github.GrbavaCigla.models.Airport;

public class AirportDialog extends DerivedDialog {
    private JFormattedTextField nameField;
    private CodeField codeField;
    private CoordinateField xField;
    private CoordinateField yField;
    private Airport airport;

    private int gridYCounter = 0;

    public AirportDialog(Component parent, Airport airport) {
        this(findFrame(parent), airport);
    }

    public AirportDialog(Component parent) {
        this(findFrame(parent), null);
    }

    public AirportDialog(Frame owner, Airport airport) {
        super(owner, airport == null ? "Add airport" : "Edit airport", true);
        this.airport = airport;

        Panel contentPanel = new Panel(new GridBagLayout()) {
            @Override
            public Insets getInsets() {
                return new Insets(10, 10, 10, 10);
            }
        };

        nameField = new JFormattedTextField();
        codeField = new CodeField();
        xField = new CoordinateField(180);
        yField = new CoordinateField(90);

        addField(contentPanel, "Name", nameField);
        addField(contentPanel, "Code", codeField);
        addField(contentPanel, "X", xField);
        addField(contentPanel, "Y", yField);

        Button submitButton = new Button("Submit");
        submitButton.addActionListener(e -> onSubmit());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = gridYCounter++;
        constraints.insets = new Insets(10, 0, 0, 0);
        contentPanel.add(submitButton, constraints);

        add(contentPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        if (airport != null) {
            updateFields(airport);
        }

        pack();
        setSize(new Dimension(250, getSize().height));
        setLocationRelativeTo(getParent());
    }

    private void onSubmit() {
        try {
            codeField.commitEdit();
            xField.commitEdit();
            yField.commitEdit();

            String name = nameField.getText().trim();
            String code = codeField.getValue().toString();
            float x = xField.getValueAsFloat();
            float y = yField.getValueAsFloat();

            if (airport == null) {
                Context.getInstance()
                        .getAirportModelList()
                        .add(new Airport(name, code, x, y));
            } else {
                Airport newAirport = new Airport(name, code, x, y);
                Context.getInstance().getAirportModelList().validate(newAirport, airport);
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

    private void addField(Panel panel, String name, Component field) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = gridYCounter++;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        panel.add(new Label(name), constraints);

        constraints.gridy = gridYCounter++;
        panel.add(field, constraints);
    }

    private void updateFields(Airport airport) {
        nameField.setText(airport.getName());
        codeField.setText(airport.getCode());
        xField.setValue(airport.getX());
        yField.setValue(airport.getY());
    }
}
