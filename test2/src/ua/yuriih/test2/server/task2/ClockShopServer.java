package ua.yuriih.test2.server.task2;

import ua.yuriih.test2.server.ManufacturerDao;
import ua.yuriih.test2.server.ClockModelDao;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClockShopServer {
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost/DISTTEST2", "user", "password");

        ManufacturerDao manufacturerDao = new ManufacturerDao(connection);
        ClockModelDao clockModelDao = new ClockModelDao(connection);

        ServerSocket serverSocket = new ServerSocket(12345);

        int threads = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                while (!Thread.interrupted()) {
                    System.out.println("Started listening");
                    try (Socket socket = serverSocket.accept()) {
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        ClientHandler handler = new ClientHandler(out, in, clockModelDao, manufacturerDao);
                        handler.run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
