package ua.yuriih.task10;

import org.xml.sax.SAXException;
import ua.yuriih.task10.controller.UniversityDao;
import ua.yuriih.task10.model.Group;
import ua.yuriih.task10.model.Student;

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

        universityDao.readFromFile("test.xml");

//        Group group = new Group(0, "Hope's Peak 78th");
//        universityDao.addGroup(group);
//
//        Student student = new Student(0, 0, "Hifumi", LocalDate.of(1999, Month.JULY, 5), true);
//        universityDao.addStudent(student);
//
//        group = new Group(1, "1-A");
//        universityDao.addGroup(group);
//
//        student = new Student(1, 1, "Deku", LocalDate.of(2000, Month.JULY, 5), false);
//        student.setAverageScore(1000);
//        universityDao.addStudent(student);

        universityDao.saveToFile("test.xml");

        System.out.println(universityDao.getAllGroups());
        System.out.println(universityDao.getAllStudents());

        universityDao.deleteGroup(0);
        System.out.println(universityDao.getAllStudents());
    }
}
