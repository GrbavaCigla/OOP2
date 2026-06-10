package io.github.GrbavaCigla;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.gui.MainWindow;
import io.github.GrbavaCigla.models.Airport;

public class App {
    public static void main(String[] args) {
        Properties properties = new Properties();

        try (InputStream input = App.class.getClassLoader().getResourceAsStream("project.properties")) {
            if (input == null) {
                System.out.println("Unable to find project.properties");
                return;
            }

            properties.load(input);

            ModelList<Airport> airportModel = Context.getInstance().getAirportModelList();
            airportModel.add(new Airport("Test", "TST", 10.0f, 10.0f));
            airportModel.add(new Airport("Test", "TST", 10.0f, 10.0f));
            airportModel.add(new Airport("Test", "TST", 10.0f, 10.0f));

            new MainWindow(properties);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
