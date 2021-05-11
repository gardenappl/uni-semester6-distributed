package ua.yuriih.task13.client;

import ua.yuriih.task13.common.IGroupDao;
import ua.yuriih.task13.common.IStudentDao;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UniversityClient {
    public static void main(String[] args) throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(12347);
        IGroupDao groupDao =
                (IGroupDao) registry.lookup("//127.0.0.1/GroupDAO");
        IStudentDao studentDao =
                (IStudentDao) registry.lookup("//127.0.0.1/StudentDAO");

        UniversityPanel panel = new UniversityPanel(groupDao, studentDao);

        JFrame frame = new JFrame("University 4.0");
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
