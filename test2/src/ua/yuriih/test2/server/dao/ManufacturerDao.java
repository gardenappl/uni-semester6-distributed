package ua.yuriih.test2.server.dao;

import ua.yuriih.test2.common.model.Manufacturer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManufacturerDao {
    private final Connection connection;

    public ManufacturerDao(Connection connection) {
        this.connection = connection;
    }

    public void addManufacturer(Manufacturer manufacturer) {
        String sql = "INSERT INTO clock_company" +
                " VALUES (0, ?, ?)" +
                " RETURNING id_co";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, manufacturer.getName());
            statement.setString(2, manufacturer.getCountry());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                manufacturer.setId(result.getInt("id_co"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateManufacturer(Manufacturer manufacturer) {
        String sql = "UPDATE clock_company" +
                " SET name = ?" +
                ", country = ?" +
                " WHERE id_co = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, manufacturer.getName());
            statement.setString(2, manufacturer.getCountry());
            statement.setInt(3, manufacturer.getId());
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No manufacturer with ID " + manufacturer.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteManufacturer(int id) {
        // ON DELETE CASCADE
        String sql = "DELETE FROM clock_company" +
                " WHERE id_co = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No manufacturer with ID " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Manufacturer> getManufacturersWithMaxTotalClockAmount(int maxTotalAmount) {
        String sql = "SELECT * FROM clock_company" +
                " WHERE (" +
                "    SELECT COUNT(*) FROM clock_model" +
                "    WHERE clock_model.id_co = clock_company.id_co" +
                ") <= ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, maxTotalAmount);
            ResultSet result = statement.executeQuery();
            ArrayList<Manufacturer> resultList = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("id_co");
                String name = result.getString("name");
                String country = result.getString("country");
                resultList.add(new Manufacturer(id, name, country));
            }
            return resultList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
