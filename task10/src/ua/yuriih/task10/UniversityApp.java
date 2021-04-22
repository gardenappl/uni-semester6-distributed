package ua.yuriih.task10;

import ua.yuriih.task10.controller.UniversityDao;
import ua.yuriih.task10.ui.UniversityPanel;

import javax.swing.*;
import java.awt.*;

public class UniversityApp {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        UniversityDao universityDao = new UniversityDao();

        UniversityPanel panel = new UniversityPanel(universityDao, "test.xml");

        JFrame frame = new JFrame("University");
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
