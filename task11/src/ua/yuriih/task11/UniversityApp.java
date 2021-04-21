package ua.yuriih.task11;

import ua.yuriih.task11.dao.GroupDao;
import ua.yuriih.task11.dao.StudentDao;
import ua.yuriih.task11.ui.UniversityFrame;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UniversityApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost/UNI", "user", "password");

        GroupDao groupDao = new GroupDao(connection);
        StudentDao studentDao = new StudentDao(connection);

        UniversityFrame frame = new UniversityFrame(groupDao, studentDao);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
