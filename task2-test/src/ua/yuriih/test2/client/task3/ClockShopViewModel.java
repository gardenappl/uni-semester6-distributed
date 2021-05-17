package ua.yuriih.test2.client.task3;

import ua.yuriih.test2.client.AbstractClockShopViewModel;
import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.common.ClockType;
import ua.yuriih.test2.common.Manufacturer;
import ua.yuriih.test2.common.task3.IClockModelDao;
import ua.yuriih.test2.common.task3.IManufacturerDao;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClockShopViewModel extends AbstractClockShopViewModel {
    private final IClockModelDao clockModelDao;
    private final IManufacturerDao manufacturerDao;

    public ClockShopViewModel(IClockModelDao clockModelDao, IManufacturerDao manufacturerDao) {
        this.clockModelDao = clockModelDao;
        this.manufacturerDao = manufacturerDao;
    }

    public ArrayList<ClockModel> getAllModelsByType(ClockType type) {
        try {
            return clockModelDao.getAllModelsByType(type);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClockModel> getAllModelsFromCountry(String country) {
        try {
            return clockModelDao.getAllModelsFromCountry(country);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClockModel> getMechanicalModelsCheaperThan(BigDecimal price) {
        try {
            return clockModelDao.getMechanicalModelsCheaperThan(price);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Manufacturer> getManufacturersWithMaxTotalAmount(int maxTotalAmount) {
        try {
            return manufacturerDao.getManufacturersWithMaxTotalClockAmount(maxTotalAmount);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
