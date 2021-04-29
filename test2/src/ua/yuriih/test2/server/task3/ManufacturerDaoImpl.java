package ua.yuriih.test2.server.task3;

import ua.yuriih.test2.common.Manufacturer;
import ua.yuriih.test2.common.task3.IManufacturerDao;
import ua.yuriih.test2.server.ManufacturerDao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ManufacturerDaoImpl extends UnicastRemoteObject implements IManufacturerDao {
    private final ManufacturerDao dao;

    public ManufacturerDaoImpl(ManufacturerDao dao) throws RemoteException {
        super();
        this.dao = dao;
    }

    @Override
    public ArrayList<Manufacturer> getManufacturersWithMaxTotalClockAmount(int maxTotalAmount) {
        return dao.getManufacturersWithMaxTotalClockAmount(maxTotalAmount);
    }
}
