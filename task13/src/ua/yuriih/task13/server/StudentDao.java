package ua.yuriih.task13.server;

import ua.yuriih.task13.common.IStudentDao;
import ua.yuriih.task13.common.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentDao extends UnicastRemoteObject implements IStudentDao {
    private final Connection connection;

    public StudentDao(Connection connection) throws RemoteException {
        super();
        this.connection = connection;
    }

    @Override
    public void addStudent(Student student) throws RemoteException {
        String sql = "INSERT INTO STUDENTS" +
                " VALUES (0, ?, ?, ?, ?, ?)" +
                " RETURNING ID_S";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getBirthDate().toString());
            statement.setFloat(4, student.getAverageScore());
            statement.setBoolean(5, student.hasScholarship());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                student.setId(result.getInt("ID_S"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteStudent(int id) throws RemoteException {
        String sql = "DELETE FROM STUDENTS" +
                " WHERE ID_S = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No student with ID " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStudent(Student student) throws RemoteException {
        String sql = "UPDATE STUDENTS" +
                " SET ID_G = ?" +
                ", NAME = ?" +
                ", BIRTHDATE = ?" +
                ", AVG_SCORE = ?" +
                ", HAS_SCHOLAR = ?" +
                " WHERE ID_S = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student.getGroupId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getBirthDate().toString());
            statement.setFloat(4, student.getAverageScore());
            statement.setBoolean(5, student.hasScholarship());
            statement.setInt(6, student.getId());
            if (statement.executeUpdate() == 0)
                throw new IllegalArgumentException("No student with ID " + student.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Student> getAllStudentsFromGroup(int groupId) throws RemoteException {
        String sql = "SELECT * FROM STUDENTS" +
                " WHERE ID_G = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);

            ResultSet result = statement.executeQuery();
            ArrayList<Student> resultList = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("ID_S");
                String name = result.getString("NAME");
                LocalDate birthDate = LocalDate.parse(result.getString("BIRTHDATE"));
                float avgScore = result.getFloat("AVG_SCORE");
                boolean hasScholarship = result.getBoolean("HAS_SCHOLAR");

                Student student = new Student(id, groupId, name, birthDate, hasScholarship);
                student.setAverageScore(avgScore);
                resultList.add(student);
            }
            return resultList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
