package io.github.GrbavaCigla.gui.components;

import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.core.Observable;
import io.github.GrbavaCigla.core.interfaces.Observer;
import io.github.GrbavaCigla.core.interfaces.Tabulatable;

import java.util.List;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ModelPanel<T extends Observable<T> & Tabulatable> extends JPanel {
    private JTable table;
    private ModelList<T> model;
    private Function<T, JDialog> dialogFactory;

    public final Observer<T> itemObserver = (observable, model) -> {
        this.updateItemView(model);
    };

    public final Observer<List<T>> listObserver = (observable, modelList) -> {
        this.updateListView(modelList);
    };

    private TableModel getTableModel() {
        List<T> list = model.getModels();

        if (list.isEmpty()) {
            return new DefaultTableModel(new Object[] {}, 0);
        }

        DefaultTableModel tableModel = new DefaultTableModel(list.get(0).getColumns(), 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return list.get(0).getColumnClass(columnIndex);
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                super.setValueAt(value, row, column);
                T item = model.getModels().get(row);
                item.updateCell(value, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return list.get(row).isColumnEditable(column);
            }
        };

        for (T item : list) {
            tableModel.addRow(item.getRow());
        }

        return tableModel;
    }

    public ModelPanel(String title, ModelList<T> model, Function<T, JDialog> dialogFactory) {
        this.dialogFactory = dialogFactory;
        this.model = model;

        setLayout(new BorderLayout(5, 5));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(200, 300));
        add(sp);

        addActions();

        model.addObservers(itemObserver, listObserver);
        updateListView(model.getModels());

        setBackground(Color.LIGHT_GRAY);
        setVisible(true);
    }

    private void addModel(ActionEvent e) {
        dialogFactory.apply(null).setVisible(true);
    }

    private void editModel(ActionEvent e) {
        int selectedIndex = table.getSelectedRow();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "Nothing is selected",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        T elem = model.getModels().get(selectedIndex);
        dialogFactory.apply(elem).setVisible(true);
    }

    private void deleteModel(ActionEvent e) {
        int selectedIndex = table.getSelectedRow();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Nothing is selected",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        model.remove(selectedIndex);
    }

    private void addActions() {
        JPanel actionsPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addModel(e));

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editModel(e));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteModel(e));

        actionsPanel.add(addButton);
        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        add(actionsPanel, BorderLayout.SOUTH);
    }

    @Override
    public Insets getInsets() {
        return new Insets(5, 10, 10, 10);
    }

    private void updateItemView(T model) {
        updateListView(this.model.getModels());
    }

    private void updateListView(List<T> list) {
        model.addObservers(itemObserver, listObserver);
        table.setModel(getTableModel());
    }
}