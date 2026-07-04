package io.github.GrbavaCigla.ui.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import io.github.GrbavaCigla.core.Constants;

public class InactivityWarningDialog extends DerivedDialog {
    private final JLabel countdownLabel;

    public InactivityWarningDialog() {
        super(null, "Inactivity Warning", false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JPanel contentPanel = createContentPanel();

        int secondsLeft = Constants.INACTIVITY_CLOSE_DELAY_MS / 1000;
        countdownLabel = new JLabel("App will close in " + secondsLeft + " seconds.");

        addField(contentPanel, "No activity detected.", countdownLabel);

        add(contentPanel);

        finalizeLayout(250);
    }

    public void updateCountdown(int seconds) {
        countdownLabel.setText("App will close in " + seconds + " seconds.");
    }
}
