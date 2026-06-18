package io.github.GrbavaCigla.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.core.Observable;
import io.github.GrbavaCigla.io.Format;

public class ModelPanel<T extends Observable<T>> extends JPanel {
    private final JTable table;
    private final ModelList<T> model;
    private final Function<T, JDialog> dialogFactory;

    public ModelPanel(String title, ModelList<T> model, DefaultTableModel tableModel,
            Function<T, JDialog> dialogFactory) {
        this.model = model;
        this.dialogFactory = dialogFactory;

        setLayout(new BorderLayout(5, 5));
        add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);

        table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(200, 300));
        add(sp);

        addActions();
        setVisible(true);
    }

    private void addModel() {
        dialogFactory.apply(null).setVisible(true);
    }

    private void editModel() {
        int selectedIndex = table.getSelectedRow();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "Nothing is selected",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        dialogFactory.apply(model.getModels().get(selectedIndex)).setVisible(true);
    }

    private void deleteModel() {
        int selectedIndex = table.getSelectedRow();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Nothing is selected",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        model.remove(selectedIndex);
    }

    private void importModel(Format format) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;
        Path path = chooser.getSelectedFile().toPath();
        try {
            model.load(format, path);
        } catch (IOException | UnsupportedOperationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Import failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportModel(Format format) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;
        Path path = chooser.getSelectedFile().toPath();
        try {
            model.dump(format, path);
        } catch (IOException | UnsupportedOperationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Export failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addActions() {
        JPanel actionsPanel = new JPanel(new GridLayout(4, 2));
        actionsPanel.setBackground(getBackground());

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addModel());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editModel());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteModel());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> model.clear());

        JButton importCSVButton = new JButton("Import as CSV");
        importCSVButton.addActionListener(e -> importModel(Format.CSV));

        JButton exportCSVButton = new JButton("Export as CSV");
        exportCSVButton.addActionListener(e -> exportModel(Format.CSV));

        JButton importJSONButton = new JButton("Import as JSON");
        importJSONButton.addActionListener(e -> importModel(Format.JSON));

        JButton exportJSONButton = new JButton("Export as JSON");
        exportJSONButton.addActionListener(e -> exportModel(Format.JSON));

        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        actionsPanel.add(clearButton);
        actionsPanel.add(importCSVButton);
        actionsPanel.add(exportCSVButton);
        actionsPanel.add(importJSONButton);
        actionsPanel.add(exportJSONButton);
        add(actionsPanel, BorderLayout.SOUTH);
    }

    @Override
    public Insets getInsets() {
        return new Insets(5, 10, 10, 10);
    }
}
