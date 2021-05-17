package ua.yuriih.test2.server.task3;

import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.common.ClockType;
import ua.yuriih.test2.common.task3.IClockModelDao;
import ua.yuriih.test2.server.ClockModelDao;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClockModelDaoImpl extends UnicastRemoteObject implements IClockModelDao {
    private final ClockModelDao dao;

    public ClockModelDaoImpl(ClockModelDao dao) throws RemoteException {
        super();
        this.dao = dao;
    }

    @Override
    public ArrayList<ClockModel> getAllModelsByType(ClockType type) {
        return dao.getAllModelsByType(type);
    }

    @Override
    public ArrayList<ClockModel> getMechanicalModelsCheaperThan(BigDecimal price) {
        return dao.getMechanicalModelsCheaperThan(price);
    }

    @Override
    public ArrayList<ClockModel> getAllModelsFromCountry(String country) {
        return dao.getAllModelsFromCountry(country);
    }
}
