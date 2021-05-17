package ua.yuriih.lab2task5.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import ua.yuriih.lab2task5.common.Group;
import ua.yuriih.lab2task5.common.Operation;
import ua.yuriih.lab2task5.common.Student;
import ua.yuriih.lab2task5.common.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Queue;

import static ua.yuriih.lab2task5.server.UniversityServer.SERVER_TO_CLIENT;

public final class ServerOperations {
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final Channel channel;

    public ServerOperations(GroupDao groupDao, StudentDao studentDao, Channel channel) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.channel = channel;
    }

    public ServerOperation getOperation(Operation op) {
        return switch (op) {
            case ADD_GROUP -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Group group = (Group) in.readObject();

                        groupDao.addGroup(group);

                        out.writeInt(Operation.UPDATE_GROUPS_LIST);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case DELETE_GROUP -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    int groupId = in.readInt();

                    groupDao.deleteGroup(groupId);

                    out.writeInt(Operation.UPDATE_GROUPS_LIST);
                }
            };
            case UPDATE_GROUP -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Group group = (Group) in.readObject();

                        groupDao.updateGroup(group);

                        out.writeInt(Operation.UPDATE_GROUPS_LIST);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case GET_ALL_GROUPS -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    ArrayList<Group> groups = groupDao.getAllGroups();

                    out.writeInt(Operation.RECEIVE_GROUPS_LIST);
                    out.writeInt(groups.size());
                    for (Group group : groups)
                        out.writeObject(group);
                    out.flush();
                }
            };
            case GET_STUDENTS_FROM_GROUP -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    int groupId = in.readInt();


                    ArrayList<Student> students = studentDao.getAllStudentsFromGroup(groupId);

                    out.writeInt(Operation.RECEIVE_STUDENTS_LIST);
                    out.writeInt(students.size());
                    for (Student student : students)
                        out.writeObject(student);
                }
            };
            case ADD_STUDENT -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Student student = (Student) in.readObject();


                        studentDao.addStudent(student);

                        out.writeInt(Operation.UPDATE_STUDENTS_LIST);
                        out.writeInt(student.getId());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case UPDATE_STUDENT -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Student student = (Student) in.readObject();


                        studentDao.updateStudent(student);

                        out.writeInt(Operation.UPDATE_STUDENTS_LIST);
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case DELETE_STUDENT -> new ServerOperation(groupDao, studentDao, channel) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    int studentId = in.readInt();


                    studentDao.deleteStudent(studentId);

                    out.writeInt(Operation.UPDATE_STUDENTS_LIST);
                    out.flush();
                }
            };
        };
    }
}
