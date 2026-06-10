package io.github.GrbavaCigla.gui.components;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Label;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.core.Observer;
import io.github.GrbavaCigla.gui.dialogs.WarningDialog;

import java.util.List;

public class ModelPanel<T> extends Panel {
    private java.awt.List table;
    private ModelList<T> model;

    public final Observer<T> itemObserver = (observable, model) -> {
        this.updateItemView(model);
    };

    public final Observer<List<T>> listObserver = (observable, modelList) -> {
        this.updateListView(modelList);
    };

    public ModelPanel(String title, ModelList<T> model) {
        this.model = model;

        setLayout(new BorderLayout(5, 5));

        add(new Label(title, Label.CENTER), BorderLayout.NORTH);
        table = new java.awt.List();
        add(table);

        addActions();

        model.addObserver(this.listObserver);
        updateListView(model.getModels());

        setBackground(Color.LIGHT_GRAY);
        setVisible(true);
    }

    private void addModel(ActionEvent e) {

    }


    private void deleteModel(ActionEvent e) {
        int selectedIndex = table.getSelectedIndex();
        if (selectedIndex == -1) {
            WarningDialog dialog = new WarningDialog(this, "Please select an item to delete.");
            dialog.setVisible(true);
            return;
        }
        T elem = model.getModels().get(selectedIndex);
        model.remove(elem);
    }

    private void addActions() {
        Panel actionsPanel = new Panel(new FlowLayout());

        Button addButton = new Button("Add");
        addButton.addActionListener(e -> addModel(e));
        
        Button deleteButton = new Button("Delete");
        deleteButton.addActionListener(e -> deleteModel(e));
        
        actionsPanel.add(addButton);
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
        table.removeAll();
        for (T item : list) {
            if (item != null) {
                table.add(item.toString());
            }
        }
    }
}