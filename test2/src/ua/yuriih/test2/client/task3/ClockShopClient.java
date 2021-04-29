package ua.yuriih.test2.client.task3;

import ua.yuriih.test2.common.task3.IClockModelDao;
import ua.yuriih.test2.common.task3.IManufacturerDao;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClockShopClient {

    public static void main(String[] args) throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(12346);
        IClockModelDao clockModelDao =
                (IClockModelDao) registry.lookup("//127.0.0.1/ClockModelDAO");
        IManufacturerDao manufacturerDao =
                (IManufacturerDao) registry.lookup("//127.0.0.1/ManufacturerDAO");

        new ConsoleUI().run(clockModelDao, manufacturerDao);
    }
}
