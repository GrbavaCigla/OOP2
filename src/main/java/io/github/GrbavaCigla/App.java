package io.github.GrbavaCigla;

import java.io.IOException;
import java.io.InputStream;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.gui.MainWindow;
import io.github.GrbavaCigla.models.Airport;

public class App {
    public static void main(String[] args) {
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("project.properties")) {
            if (input == null) {
                System.out.println("Unable to find project.properties");
                return;
            }

            Context.getInstance().loadProperties(input);

            ModelList<Airport> airportModel = Context.getInstance().getAirportModelList();
            airportModel.add(new Airport("Test", "TST", 50.0f, 20.0f));
            airportModel.add(new Airport("Test", "TST", 70.0f, 40.0f));
            airportModel.add(new Airport("Test", "TST", 80.0f, 70.0f));

            new MainWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
