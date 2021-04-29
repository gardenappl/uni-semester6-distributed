package ua.yuriih.test2.common.task3;

import ua.yuriih.test2.common.model.ClockModel;
import ua.yuriih.test2.common.model.ClockType;
import ua.yuriih.test2.common.model.Manufacturer;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IManufacturerDao extends Remote {
    ArrayList<Manufacturer> getManufacturersWithMaxTotalClockAmount(int maxTotalAmount) throws RemoteException;
}
