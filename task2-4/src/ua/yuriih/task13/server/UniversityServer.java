package ua.yuriih.task13.server;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UniversityServer {
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost/UNI", "user", "password");

        Registry registry = LocateRegistry.createRegistry(12347);

        GroupDao groupDao = new GroupDao(connection);
        StudentDao studentDao = new StudentDao(connection);

        System.err.println("Binding...");
        registry.rebind("//127.0.0.1/GroupDAO", groupDao);
        System.err.println("Bound group DAO.");
        registry.rebind("//127.0.0.1/StudentDAO", studentDao);
        System.err.println("Bound student DAO.");
    }
}
