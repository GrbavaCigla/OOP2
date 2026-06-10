package io.github.GrbavaCigla.gui.dialogs;

import java.awt.*;
import java.awt.event.*;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.models.Airport;

public class AirportDialog extends DerivedDialog {
    private TextField nameField;
    private TextField codeField;
    private TextField xField;
    private TextField yField;
    private Airport airport;

    public AirportDialog(Frame owner) {
        super(owner, "Add airport", true);
        addUI();
    }

    public AirportDialog(Component parent) {
        this(findFrame(parent));
    }

    public AirportDialog(Frame owner, Airport airport) {
        super(owner, "Edit airport", true);
        this.airport = airport;
        addUI();
    }

    public AirportDialog(Component parent, Airport airport) {
        this(findFrame(parent), airport);
    }

    public void addUI() {
        setLayout(new GridLayout(3, 1, 5, 5));

        Panel namePanel = new Panel(new BorderLayout(5, 5));
        namePanel.add(new Label("Name:", Label.LEFT), BorderLayout.NORTH);
        nameField = new TextField();
        namePanel.add(nameField, BorderLayout.CENTER);
        add(namePanel);

        Panel secondRowPanel = new Panel(new GridLayout(1, 3, 5, 5));

        Panel codePanel = new Panel(new BorderLayout(5, 5));
        codePanel.add(new Label("Code:", Label.LEFT), BorderLayout.NORTH);
        codeField = new TextField();
        codeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = codeField.getText();
                if (text.length() >= 3 || c < 'A' || c > 'Z') {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        codePanel.add(codeField, BorderLayout.CENTER);
        secondRowPanel.add(codePanel);

        Panel xPanel = new Panel(new BorderLayout(5, 5));
        xPanel.add(new Label("X:", Label.LEFT), BorderLayout.NORTH);
        xField = new TextField();
        xPanel.add(xField, BorderLayout.CENTER);
        secondRowPanel.add(xPanel);

        Panel yPanel = new Panel(new BorderLayout(5, 5));
        yPanel.add(new Label("Y:", Label.LEFT), BorderLayout.NORTH);
        yField = new TextField();
        yPanel.add(yField, BorderLayout.CENTER);
        secondRowPanel.add(yPanel);
        add(secondRowPanel);

        Panel buttonsPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> {
            if (airport == null) {
                Context.getInstance().getAirportModelList()
                        .add(new Airport(
                                getFieldName(),
                                getFieldCode(),
                                getFieldX(),
                                getFieldY()));
            } else {
                airport.setName(getFieldName());
                airport.setCode(getFieldCode());
                airport.setX(getFieldX());
                airport.setY(getFieldY());
            }
            dispose();
        });
        buttonsPanel.add(okButton);

        Button cancelButton = new Button("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);
        add(buttonsPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        if (airport != null) {
            setFieldName(airport.getName());
            setFieldCode(airport.getCode());
            setFieldX(airport.getX());
            setFieldY(airport.getY());
        }

        pack();
        setLocationRelativeTo(getParent());
    }

    private float getFieldX() {
        return Float.parseFloat(xField.getText());
    }

    private float getFieldY() {
        return Float.parseFloat(yField.getText());
    }

    private String getFieldName() {
        return nameField.getText();
    }

    private String getFieldCode() {
        return codeField.getText();
    }

    private void setFieldX(float x) {
        xField.setText(Float.toString(x));
    }

    private void setFieldY(float y) {
        yField.setText(Float.toString(y));
    }

    private void setFieldName(String name) {
        nameField.setText(name);
    }

    private void setFieldCode(String code) {
        codeField.setText(code);
    }
}
