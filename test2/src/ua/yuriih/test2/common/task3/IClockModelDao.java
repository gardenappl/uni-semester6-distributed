package ua.yuriih.test2.common.task3;

import ua.yuriih.test2.common.model.ClockModel;
import ua.yuriih.test2.common.model.ClockType;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IClockModelDao extends Remote {
    ArrayList<ClockModel> getAllModelsByType(ClockType type) throws RemoteException;

    ArrayList<ClockModel> getMechanicalModelsCheaperThan(BigDecimal price) throws RemoteException;

    ArrayList<ClockModel> getAllModelsFromCountry(String country) throws RemoteException;
}
