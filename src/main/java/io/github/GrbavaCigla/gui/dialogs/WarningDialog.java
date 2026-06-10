package io.github.GrbavaCigla.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WarningDialog extends DerivedDialog {
    private static String title = "Warning";

    public WarningDialog(Frame owner, String message) {
        super(owner, title, true);

        Panel contentPanel = new Panel(new BorderLayout()) {
            @Override
            public Insets getInsets() {
                return new Insets(10, 10, 10, 10);
            }
        };
        contentPanel.add(new Label(message), BorderLayout.CENTER);

        Panel actionPanel = new Panel(new FlowLayout());
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dispose());
        actionPanel.add(okButton);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    public WarningDialog(Component parent, String message) {
        this(findFrame(parent), message);
    }
}
