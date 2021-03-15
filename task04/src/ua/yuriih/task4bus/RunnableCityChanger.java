package ua.yuriih.task4bus;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class RunnableCityChanger implements Runnable {
    private final BusRouteGraph routeGraph;
    private final Lock writeLock;
    private static int currentCityNum = BusSimulation.INITIAL_CITY_COUNT;

    public RunnableCityChanger(BusRouteGraph routeGraph) {
        this.routeGraph = routeGraph;
        this.writeLock = routeGraph.getLock().writeLock();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.err.println("[CityChanger] Woke up, getting write lock...");
                writeLock.lock();
            } catch (InterruptedException ignored) {}

            List<String> cities = routeGraph.getCities();
            String city = cities.get(0);
            String newCity = BusSimulation.generateCityName(currentCityNum);
            currentCityNum++;
            System.err.printf("[CityChanger] Replacing %s with %s...\n", city, newCity);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}

            routeGraph.removeCity(city);
            routeGraph.addCity(newCity);

            System.err.println("[CityChanger] Done.");
            writeLock.unlock();
        }
    }
}
