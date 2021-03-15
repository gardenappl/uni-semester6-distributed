package ua.yuriih.task4bus;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class RunnableRouteChanger implements Runnable {
    private final BusRouteGraph routeGraph;
    private final Lock writeLock;

    public RunnableRouteChanger(BusRouteGraph routeGraph) {
        this.routeGraph = routeGraph;
        this.writeLock = routeGraph.getLock().writeLock();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.err.println("[RouteChanger] Woke up, getting write lock...");
                writeLock.lock();
            } catch (InterruptedException ignored) {}

            List<Route> routes = routeGraph.getRoutes();
            int routeNum = BusSimulation.rng.nextInt(routes.size());
            Route route = routes.get(routeNum);
            
            List<String> cities = routeGraph.getCities();
            Collections.shuffle(cities);
            Route newRoute = new Route(cities.get(0), cities.get(1),
                    BusSimulation.rng.nextInt(BusSimulation.MAX_PRICE));

            System.err.printf("[RouteChanger] Replacing %s with %s...\n", route, newRoute);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}

            routeGraph.removeRoute(route);
            routeGraph.addRoute(newRoute);

            System.err.println("[PriceChanger] Done.");
            writeLock.unlock();
        }
    }
}
