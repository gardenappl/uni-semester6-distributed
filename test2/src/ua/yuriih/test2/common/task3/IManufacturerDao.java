package ua.yuriih.test2.common.task3;

import ua.yuriih.test2.common.Manufacturer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IManufacturerDao extends Remote {
    ArrayList<Manufacturer> getManufacturersWithMaxTotalClockAmount(int maxTotalAmount) throws RemoteException;
}
