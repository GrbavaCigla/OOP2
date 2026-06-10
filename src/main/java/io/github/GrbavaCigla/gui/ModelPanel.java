package io.github.GrbavaCigla.gui;

import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Label;

import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.core.Observer;

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

        setLayout(new BorderLayout());

        add(new Label(title, Label.CENTER), BorderLayout.NORTH);
        table = new java.awt.List();
        add(table, BorderLayout.CENTER);

        model.addObserver(this.listObserver);

        updateListView(model.getModels());

        setVisible(true);
    }

    private void updateItemView(T model) {
        updateListView((List<T>) this.model.getModels());
    }

    private void updateListView(List<T> list) {
        table.removeAll();
        for(T item : list) {
            if (item != null) {
                table.add(item.toString());
            }
        }
    }
}