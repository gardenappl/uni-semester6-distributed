package ua.yuriih.task4bus;


import java.util.Random;

public class BusSimulation {
    public static final int INITIAL_CITY_COUNT = 10;
    public static final int MAX_PRICE = 1000;
    public static final Random rng = new Random();

    public static void main(String[] args) {
        BusRouteGraph routeGraph = new BusRouteGraph();
        for (int i = 0; i < INITIAL_CITY_COUNT; i++) {
            String city = generateCityName(i);
            routeGraph.addCity(city);
            System.err.println("Added " + city);
            
            for (int j = 0; j < i; j++) {
                if (rng.nextDouble() < 0.2) {
                    Route route = new Route(generateCityName(i), generateCityName(j),
                            rng.nextInt(MAX_PRICE));
                    System.err.println("Added " + route);
                    routeGraph.addRoute(route);
                }
            }
        }
        
        new Thread(new RunnableCityChanger(routeGraph)).start();
        new Thread(new RunnablePriceChanger(routeGraph)).start();
        new Thread(new RunnableRouteChanger(routeGraph)).start();

        new Thread(new RunnableRouteFinder(routeGraph, "RouteFinder1")).start();
        new Thread(new RunnableRouteFinder(routeGraph, "RouteFinder2")).start();
    }

    public static String generateCityName(int index) {
        return "City " + index;
    }
}
