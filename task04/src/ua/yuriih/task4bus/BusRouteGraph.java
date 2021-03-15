package ua.yuriih.task4bus;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BusRouteGraph {
    private final List<Route> routes = new ArrayList<>();
    private final Set<String> cities = new HashSet<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    public boolean addCity(String city) {
        return cities.add(city);
    }

    public boolean removeCity(String city) {
        if (!cities.contains(city))
            return false;
        cities.remove(city);
        routes.removeIf(route -> route.city1.equals(city) || route.city2.equals(city));
        return true;
    }
    
    public List<String> getCities() {
        return new ArrayList<>(cities);
    }

    public void addRoute(Route route) {
        if (!cities.contains(route.city1))
            throw new IllegalArgumentException(route.city1 + " is not in the set");
        else if (!cities.contains(route.city2))
            throw new IllegalArgumentException(route.city2 + " is not in the set");

        routes.removeIf(oldRoute -> oldRoute.city1.equals(route.city1) &&
                oldRoute.city2.equals(route.city2));
        routes.add(route);
    }

    public boolean removeRoute(Route route) {
        return routes.remove(route);
    }

    public List<Route> getRoutes() {
        return Collections.unmodifiableList(routes);
    }

    public ReadWriteLock getLock() {
        return lock;
    }
}
