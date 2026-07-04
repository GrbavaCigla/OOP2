package io.github.GrbavaCigla.ui.dialogs;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public abstract class DerivedDialog extends JDialog {
    private int gridYCounter = 0;

    public int getGridYCounter() {
        return gridYCounter;
    }

    public void setGridYCounter(int gridYCounter) {
        this.gridYCounter = gridYCounter;
    }

    public DerivedDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    protected static Frame findFrame(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }

    protected void addField(JPanel panel, String name, Component field) {
        GridBagConstraints constraints = new GridBagConstraints();
        
        constraints.gridx = 0;
        constraints.gridy = gridYCounter++;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        panel.add(new JLabel(name), constraints);

        constraints.gridy = gridYCounter++;
        panel.add(field, constraints);
    }
}