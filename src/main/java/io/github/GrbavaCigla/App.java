package io.github.GrbavaCigla;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

import io.github.GrbavaCigla.core.ModelStore;
import io.github.GrbavaCigla.core.ModelList;
import io.github.GrbavaCigla.ui.MainWindow;
import io.github.GrbavaCigla.models.Airport;
import io.github.GrbavaCigla.models.Flight;

public class App {
    public static void main(String[] args) {
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("project.properties")) {
            if (input == null) {
                System.out.println("Unable to find project.properties");
                return;
            }

            Properties properties = new Properties();
            properties.load(input);

            ModelList<Airport> airportModel = ModelStore.getAirportModelList();

            List<Airport> airports = List.of(
                    new Airport("Hartsfield-Jackson Atlanta International Airport", "ATL", -84, 34),
                    new Airport("Dubai International Airport", "DXB", 55, 25),
                    new Airport("Tokyo Haneda Airport", "HND", 140, 36),
                    new Airport("London Heathrow Airport", "LHR", 0, 51),
                    new Airport("Los Angeles International Airport", "LAX", -118, 34),
                    new Airport("Chicago O'Hare International Airport", "ORD", -88, 42),
                    new Airport("Istanbul Airport", "IST", 29, 41),
                    new Airport("Belgrade Nikola Tesla Airport", "BEG", 20, 45),
                    new Airport("Beijing Capital International Airport", "PEK", 117, 40),
                    new Airport("Singapore Changi Airport", "SIN", 104, 1),
                    new Airport("Hong Kong International Airport", "HKG", 114, 22),
                    new Airport("Shanghai Pudong International Airport", "PVG", 122, 31),
                    new Airport("Paris Charles de Gaulle Airport", "CDG", 3, 49),
                    new Airport("Sydney Kingsford Smith Airport", "SYD", 151, -34),
                    new Airport("Mexico City International Airport", "MEX", -99, 19),
                    new Airport("Lisbon Humberto Delgado Airport", "LIS", -9, 39));

            airports.forEach(airportModel::add);

            ModelList<Flight> flightModel = ModelStore.getFlightModelList();

            flightModel.add(new Flight(airports.get(3), airports.get(12), LocalTime.of(0, 10), Duration.ofHours(1)));
            flightModel.add(new Flight(airports.get(3), airports.get(15), LocalTime.of(0, 10), Duration.ofHours(2)));
            flightModel.add(new Flight(airports.get(3), airports.get(6), LocalTime.of(0, 10), Duration.ofHours(4)));

            flightModel.add(new Flight(airports.get(6), airports.get(7), LocalTime.of(0, 20), Duration.ofHours(1)));
            flightModel.add(new Flight(airports.get(6), airports.get(1), LocalTime.of(0, 20), Duration.ofHours(4)));

            flightModel.add(new Flight(airports.get(4), airports.get(5), LocalTime.of(0, 30), Duration.ofHours(4)));
            flightModel.add(new Flight(airports.get(4), airports.get(0), LocalTime.of(0, 30), Duration.ofHours(4)));
            flightModel.add(new Flight(airports.get(4), airports.get(2), LocalTime.of(0, 30), Duration.ofHours(11)));

            flightModel.add(new Flight(airports.get(8), airports.get(11), LocalTime.of(0, 40), Duration.ofHours(2)));
            flightModel.add(new Flight(airports.get(8), airports.get(10), LocalTime.of(0, 40), Duration.ofHours(3)));

            flightModel.add(new Flight(airports.get(9), airports.get(10), LocalTime.of(0, 50), Duration.ofHours(4)));
            flightModel.add(new Flight(airports.get(9), airports.get(13), LocalTime.of(0, 50), Duration.ofHours(8)));

            flightModel.add(new Flight(airports.get(1), airports.get(6), LocalTime.of(1, 0), Duration.ofHours(4)));
            flightModel.add(new Flight(airports.get(1), airports.get(9), LocalTime.of(1, 0), Duration.ofHours(7)));
            flightModel.add(new Flight(airports.get(1), airports.get(3), LocalTime.of(1, 0), Duration.ofHours(7)));

            flightModel.add(new Flight(airports.get(0), airports.get(5), LocalTime.of(1, 10), Duration.ofHours(2)));
            flightModel.add(new Flight(airports.get(0), airports.get(4), LocalTime.of(1, 10), Duration.ofHours(5)));

            flightModel.add(new Flight(airports.get(12), airports.get(3), LocalTime.of(1, 20), Duration.ofHours(1)));
            flightModel.add(new Flight(airports.get(12), airports.get(15), LocalTime.of(1, 20), Duration.ofHours(2)));
            flightModel.add(new Flight(airports.get(12), airports.get(6), LocalTime.of(1, 20), Duration.ofHours(3)));

            flightModel.add(new Flight(airports.get(14), airports.get(5), LocalTime.of(1, 30), Duration.ofHours(4)));
            flightModel.add(new Flight(airports.get(14), airports.get(0), LocalTime.of(1, 30), Duration.ofHours(3)));

            flightModel.add(new Flight(airports.get(2), airports.get(8), LocalTime.of(1, 40), Duration.ofHours(3)));
            flightModel.add(new Flight(airports.get(2), airports.get(9), LocalTime.of(1, 40), Duration.ofHours(7)));

            flightModel.add(new Flight(airports.get(15), airports.get(3), LocalTime.of(1, 50), Duration.ofHours(2)));
            flightModel.add(new Flight(airports.get(15), airports.get(12), LocalTime.of(1, 50), Duration.ofHours(2)));

            flightModel.add(new Flight(airports.get(10), airports.get(9), LocalTime.of(2, 0), Duration.ofHours(4)));
            flightModel.add(new Flight(airports.get(10), airports.get(11), LocalTime.of(2, 0), Duration.ofHours(2)));

            flightModel.add(new Flight(airports.get(7), airports.get(6), LocalTime.of(2, 10), Duration.ofHours(1)));
            flightModel.add(new Flight(airports.get(7), airports.get(3), LocalTime.of(2, 10), Duration.ofHours(3)));

            new MainWindow(properties.getProperty("project.name"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
