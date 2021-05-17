package ua.yuriih.lab2task5.server;

import ua.yuriih.lab2task5.common.Group;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GroupDao {
    private final Connection connection;

    public GroupDao(Connection connection) throws RemoteException {
        super();
        this.connection = connection;
    }

    public void addGroup(Group group) {
        String sql = "INSERT INTO GROUPS" +
                " VALUES (0, ?)" +
                " RETURNING ID_G";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, group.getName());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                group.setId(result.getInt("ID_G"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGroup(Group group) {
        String sql = "UPDATE GROUPS" +
                " SET NAME = ?" +
                " WHERE ID_G = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, group.getName());
            statement.setInt(2, group.getId());
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No group with ID " + group.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteGroup(int id) {
        // ON DELETE CASCADE
        String sql = "DELETE FROM GROUPS" +
                " WHERE ID_G = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No group with ID " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Group> getAllGroups() {
        String sql = "SELECT * FROM GROUPS";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            ArrayList<Group> resultList = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("ID_G");
                String name = result.getString("NAME");
                resultList.add(new Group(id, name));
            }
            return resultList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
