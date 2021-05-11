package ua.yuriih.task13.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGroupDao extends Remote {
    void addGroup(Group group) throws RemoteException;
    void updateGroup(Group group) throws RemoteException;
    void deleteGroup(int id) throws RemoteException;
    ArrayList<Group> getAllGroups() throws RemoteException;
}
