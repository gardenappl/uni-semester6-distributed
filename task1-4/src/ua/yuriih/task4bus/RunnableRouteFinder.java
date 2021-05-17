package ua.yuriih.task4bus;

import java.util.*;
import java.util.concurrent.locks.Lock;

public class RunnableRouteFinder implements Runnable {
    private final BusRouteGraph routeGraph;
    private final Lock readLock;
    private final String name;
    private static final int ROUTE_NOT_FOUND = -1;

    public RunnableRouteFinder(BusRouteGraph routeGraph, String name) {
        this.routeGraph = routeGraph;
        this.readLock = routeGraph.getLock().readLock();
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.err.printf("[%s] Woke up, getting read lock...\n", name);
                readLock.lock();
            } catch (InterruptedException ignored) {}

            List<Route> routes = routeGraph.getRoutes();
            List<String> cities = routeGraph.getCities();
            Collections.shuffle(cities);
            String startCity = cities.get(0);
            String endCity = cities.get(1);

            System.err.printf("[%s] Searching for route between %s and %s.\n", name, startCity, endCity);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}

            int result = findRouteCost(startCity, endCity, 0, new HashSet<>(), routes);
            if (result == ROUTE_NOT_FOUND)
                System.err.printf("[%s] Route not found.\n", name);
            else
                System.err.printf("[%s] Found route, costs %d.\n", name, result);
            readLock.unlock();
        }
    }

    //DFS, finds *any* route
    private int findRouteCost(
            String city,
            String endCity,
            int currentCost,
            HashSet<String> visited,
            List<Route> routes
    ) {
        if (city.equals(endCity))
            return currentCost;

        for (Route route : routes) {
            int result = ROUTE_NOT_FOUND;
            if (route.city1.equals(city)) {
                if (visited.contains(route.city2))
                    continue;
                visited.add(route.city2);
                result = findRouteCost(route.city2, endCity,
                        currentCost + route.price, visited, routes);
                
            } else if (route.city2.equals(city)) {
                if (visited.contains(route.city1))
                    continue;
                visited.add(route.city1);
                result = findRouteCost(route.city1, endCity,
                        currentCost + route.price, visited, routes);
            }
            if (result != ROUTE_NOT_FOUND)
                return result;
        }
        return ROUTE_NOT_FOUND;
    }
}
