package ua.yuriih.test2;

import ua.yuriih.test2.dao.ManufacturerDao;
import ua.yuriih.test2.dao.ClockModelDao;
import ua.yuriih.test2.model.ClockType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClockShopApp {
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost/DISTTEST2", "user", "password");

        ManufacturerDao manufacturerDao = new ManufacturerDao(connection);
        ClockModelDao clockModelDao = new ClockModelDao(connection);

        System.out.println(clockModelDao.getAllModelsByType(ClockType.PIEZO));
        System.out.println(clockModelDao.getMechanicalModelsCheaperThan(new BigDecimal("250.50")));
        System.out.println(clockModelDao.getAllModelsFromCountry("Ukraine"));
        System.out.println(manufacturerDao.getManufacturersWithMaxTotalClockAmount(1));
    }
}
