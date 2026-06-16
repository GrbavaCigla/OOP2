package io.github.GrbavaCigla.ui.dialogs;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JDialog;

public abstract class DerivedDialog extends JDialog {
    public DerivedDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    protected static Frame findFrame(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }
}