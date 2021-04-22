package ua.yuriih.task11;

import ua.yuriih.task11.dao.GroupDao;
import ua.yuriih.task11.dao.StudentDao;
import ua.yuriih.task11.ui.UniversityPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UniversityApp {
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost/UNI", "user", "password");

        GroupDao groupDao = new GroupDao(connection);
        StudentDao studentDao = new StudentDao(connection);

        UniversityPanel panel = new UniversityPanel(groupDao, studentDao);

        JFrame frame = new JFrame("University 2.0");
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
