package ua.yuriih.test2.server;

import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.common.ClockType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClockModelDao {
    private final Connection connection;

    public ClockModelDao(Connection connection) {
        this.connection = connection;
    }

    public void addModel(ClockModel clockModel) {
        String sql = "INSERT INTO clock_model" +
            " VALUES (0, ?, ?, ?, ?, ?)" +
            " RETURNING id_mo";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, clockModel.getName());
            statement.setInt(2, clockModel.getType().value);
            statement.setBigDecimal(3, clockModel.getPrice());
            statement.setInt(4, clockModel.getAmount());
            statement.setInt(5, clockModel.getManufacturerId());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                clockModel.setId(result.getInt("id_mo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteModel(int id) {
        String sql = "DELETE FROM clock_model" +
                " WHERE id_mo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No clock model with ID " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateModel(ClockModel clockModel) {
        String sql = "UPDATE clock_model" +
                " SET model_name = ?" +
                ", type = ?" +
                ", price = ?" +
                ", amount = ?" +
                ", id_co = ?" +
                " WHERE id_mo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, clockModel.getName());
            statement.setInt(2, clockModel.getType().value);
            statement.setBigDecimal(3, clockModel.getPrice());
            statement.setInt(4, clockModel.getAmount());
            statement.setInt(4, clockModel.getManufacturerId());
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No clock with ID " + clockModel.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<ClockModel> getClockModels(ResultSet result) throws SQLException {
        ArrayList<ClockModel> resultList = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id_mo");
            String name = result.getString("model_name");
            ClockType type = ClockType.get(result.getInt("type"));
            BigDecimal price = result.getBigDecimal("price");
            int amount = result.getInt("amount");
            int manufacturerId = result.getInt("id_co");

            ClockModel clockModel = new ClockModel(id, name, manufacturerId, type, price, amount);
            resultList.add(clockModel);
        }
        return resultList;
    }

    public ArrayList<ClockModel> getAllModelsByType(ClockType type) {
        String sql = "SELECT * FROM clock_model" +
                " WHERE type = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, type.value);

            ResultSet result = statement.executeQuery();
            return getClockModels(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClockModel> getMechanicalModelsCheaperThan(BigDecimal price) {
        String sql = "SELECT * FROM clock_model" +
                " WHERE type = ?"+
                " AND price < ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ClockType.MECHANICAL.value);
            statement.setBigDecimal(2, price);

            ResultSet result = statement.executeQuery();
            return getClockModels(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClockModel> getAllModelsFromCountry(String country) {
        String sql = "SELECT * FROM clock_model" +
                " INNER JOIN clock_company ON clock_model.id_co = clock_company.id_co"+
                " AND country = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);

            ResultSet result = statement.executeQuery();
            return getClockModels(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
