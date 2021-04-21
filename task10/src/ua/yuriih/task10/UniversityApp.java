package ua.yuriih.task10;

import org.xml.sax.SAXException;
import ua.yuriih.task10.controller.UniversityDao;
import ua.yuriih.task10.model.Group;
import ua.yuriih.task10.model.Student;
import ua.yuriih.task10.ui.UniversityFrame;

import javax.swing.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UniversityApp {
    public static void main(String[] args) throws IOException, SAXException {
        UniversityDao universityDao = new UniversityDao();

        UniversityFrame frame = new UniversityFrame(universityDao, "test.xml");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
