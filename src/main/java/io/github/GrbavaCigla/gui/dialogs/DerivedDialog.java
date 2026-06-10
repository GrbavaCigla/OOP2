package io.github.GrbavaCigla.gui.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;

public abstract class DerivedDialog extends Dialog {
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
