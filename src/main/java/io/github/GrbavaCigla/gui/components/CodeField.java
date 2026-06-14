package io.github.GrbavaCigla.gui.components;

import javax.swing.JFormattedTextField;

import io.github.GrbavaCigla.formatters.CodeFormatter;

public class CodeField extends JFormattedTextField {

    public CodeField() {
        super(new CodeFormatter());
        setColumns(3);
        setFocusLostBehavior(JFormattedTextField.PERSIST);
    }

    public String getCode() {
        return (String) getValue();
    }

    public void setCode(String code) {
        setValue(code);
    }
}