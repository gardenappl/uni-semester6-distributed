package ua.yuriih.lab2task5.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import ua.yuriih.lab2task5.common.Group;
import ua.yuriih.lab2task5.common.Operation;
import ua.yuriih.lab2task5.common.Student;
import ua.yuriih.lab2task5.server.GroupDao;
import ua.yuriih.lab2task5.server.ServerOperation;
import ua.yuriih.lab2task5.server.ServerOperations;
import ua.yuriih.lab2task5.server.StudentDao;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class UniversityClient {
    public static final String SERVER_TO_CLIENT = "to_client";
    public static final String CLIENT_TO_SERVER = "to_server";

    public static void main(String[] args) throws SQLException, IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection mqConnection = factory.newConnection();
             Channel channel = mqConnection.createChannel()) {
            channel.queueDeclare(SERVER_TO_CLIENT, false, false, false, null);
            channel.queueDeclare(CLIENT_TO_SERVER, false, false, false, null);

            UniversityPanel panel = new UniversityPanel(channel);

            JFrame frame = new JFrame("University 5.0");
            frame.add(panel, BorderLayout.CENTER);
            frame.pack();
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            while (!Thread.interrupted()) {
                channel.basicConsume(SERVER_TO_CLIENT, true, (String callbackTag, Delivery delivery) -> {
                    ByteArrayInputStream bytesIn = new ByteArrayInputStream(delivery.getBody());
                    ObjectInputStream in = new ObjectInputStream(bytesIn);
                    int messageType = in.readInt();

                    switch (messageType) {
                        case Operation.UPDATE_GROUPS_LIST -> panel.updateGroupsList();
                        case Operation.UPDATE_STUDENTS_LIST -> panel.updateStudentsList();
                        case Operation.RECEIVE_GROUPS_LIST -> {
                            int count = in.readInt();
                            ArrayList<Group> groups = new ArrayList<>(count);
                            for (int i = 0; i < count; i++) {
                                try {
                                    groups.add((Group) in.readObject());
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            panel.receiveGroupsList(groups);
                        }
                        case Operation.RECEIVE_STUDENTS_LIST -> {
                            int count = in.readInt();
                            ArrayList<Student> students = new ArrayList<>(count);
                            for (int i = 0; i < count; i++) {
                                try {
                                    students.add((Student) in.readObject());
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            panel.receiveStudentsList(students);
                        }
                    }
                }, callbackTag -> { });
                Thread.sleep(100);
            }
        }
    }
}
