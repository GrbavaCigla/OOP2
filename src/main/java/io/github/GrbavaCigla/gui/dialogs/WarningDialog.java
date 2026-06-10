package io.github.GrbavaCigla.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;

public class WarningDialog extends Dialog {
    private static String title = "Warning";

public WarningDialog(Frame parent, String message) {
        super(parent, title, true);

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

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    public WarningDialog(Component parent, String message) {
        this(findFrame(parent), message);
    }

    private static Frame findFrame(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }
}
