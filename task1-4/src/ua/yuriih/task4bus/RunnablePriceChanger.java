package ua.yuriih.task4bus;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class RunnablePriceChanger implements Runnable {
    private final BusRouteGraph routeGraph;
    private final Lock writeLock;

    public RunnablePriceChanger(BusRouteGraph routeGraph) {
        this.routeGraph = routeGraph;
        this.writeLock = routeGraph.getLock().writeLock();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.err.println("[PriceChanger] Woke up, getting write lock...");
                writeLock.lock();
            } catch (InterruptedException ignored) {}

            List<Route> routes = routeGraph.getRoutes();
            int routeNum = BusSimulation.rng.nextInt(routes.size());
            Route route = routes.get(routeNum);

            int newPrice = BusSimulation.rng.nextInt(BusSimulation.MAX_PRICE);
            System.err.printf("[PriceChanger] Setting price for %s to %d...", route, newPrice);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}

            routeGraph.addRoute(new Route(route.city1, route.city2, newPrice));

            System.err.println("[PriceChanger] Done.");
            writeLock.unlock();
        }
    }
}
