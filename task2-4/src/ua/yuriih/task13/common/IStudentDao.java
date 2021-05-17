package ua.yuriih.task13.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IStudentDao extends Remote {
    void addStudent(Student student) throws RemoteException;
    void deleteStudent(int id) throws RemoteException;
    void updateStudent(Student student) throws RemoteException;
    ArrayList<Student> getAllStudentsFromGroup(int groupId) throws RemoteException;
}
