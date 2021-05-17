package ua.yuriih.test2.server.task3;

import ua.yuriih.test2.server.ClockModelDao;
import ua.yuriih.test2.server.ManufacturerDao;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClockShopServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException, SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost/DISTTEST2", "user", "password");

        ManufacturerDao manufacturerDao = new ManufacturerDao(connection);
        ClockModelDao clockModelDao = new ClockModelDao(connection);


        Registry registry = LocateRegistry.createRegistry(12346);

        ManufacturerDaoImpl serverManufacturerDao = new ManufacturerDaoImpl(manufacturerDao);
        ClockModelDaoImpl serverClockModelDao = new ClockModelDaoImpl(clockModelDao);
        System.err.println("Binding...");
        registry.rebind("//127.0.0.1/ManufacturerDAO", serverManufacturerDao);
        System.err.println("Bound manufacturer");
        registry.rebind("//127.0.0.1/ClockModelDAO", serverClockModelDao);
        System.err.println("Bound clock model");
    }
}
