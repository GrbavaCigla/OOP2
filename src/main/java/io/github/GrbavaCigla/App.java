package io.github.GrbavaCigla;

import java.io.IOException;
import java.io.InputStream;

import io.github.GrbavaCigla.core.Context;
import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.ui.MainWindow;
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
            airportModel.addUniqueConstraint((a) -> a.getCode());

            airportModel.add(new Airport("Hartsfield-Jackson Atlanta International Airport", "ATL", -84.43f, 33.64f));
            airportModel.add(new Airport("Dubai International Airport", "DXB", 55.36f, 25.25f));
            airportModel.add(new Airport("Tokyo Haneda Airport", "HND", 139.78f, 35.55f));
            airportModel.add(new Airport("London Heathrow Airport", "LHR", -0.45f, 51.47f));
            airportModel.add(new Airport("Los Angeles International Airport", "LAX", -118.41f, 33.94f));
            airportModel.add(new Airport("Chicago O'Hare International Airport", "ORD", -87.91f, 41.98f));
            airportModel.add(new Airport("Istanbul Airport", "IST", 28.75f, 41.28f));
            airportModel.add(new Airport("Belgrade Nikola Tesla Airport", "BEG", 20.31f, 44.82f));
            airportModel.add(new Airport("Beijing Capital International Airport", "PEK", 116.59f, 40.08f));
            airportModel.add(new Airport("Singapore Changi Airport", "SIN", 103.99f, 1.36f));
            airportModel.add(new Airport("Hong Kong International Airport", "HKG", 113.92f, 22.31f));
            airportModel.add(new Airport("Shanghai Pudong International Airport", "PVG", 121.80f, 31.14f));
            airportModel.add(new Airport("Paris Charles de Gaulle Airport", "CDG", 2.55f, 49.01f));
            airportModel.add(new Airport("Sydney Kingsford Smith Airport", "SYD", 151.18f, -33.94f));
            airportModel.add(new Airport("Mexico City International Airport", "MEX", -99.07f, 19.44f));
            airportModel.add(new Airport("Lisbon Humberto Delgado Airport", "LIS", -9.13f, 38.77f));

            ModelList<Airport> flightModel = Context.getInstance().getAirportModelList();

            new MainWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
