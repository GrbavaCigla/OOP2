package io.github.GrbavaCigla.ui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import io.github.GrbavaCigla.core.Constants;

public class InactivityWarningDialog extends DerivedDialog {
    private final JLabel countdownLabel;

    public InactivityWarningDialog() {
        super(null, "Inactivity Warning", false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            public Insets getInsets() {
                return new Insets(10, 10, 10, 10);
            }
        };

        int secondsLeft = Constants.INACTIVITY_CLOSE_DELAY_MS / 1000;
        countdownLabel = new JLabel("App will close in " + secondsLeft + " seconds.");

        addField(contentPanel, "No activity detected.", countdownLabel);

        add(contentPanel);

        pack();
        setSize(new Dimension(250, getSize().height));
        setLocationRelativeTo(getParent());
    }

    public void updateCountdown(int seconds) {
        countdownLabel.setText("App will close in " + seconds + " seconds.");
    }
}
