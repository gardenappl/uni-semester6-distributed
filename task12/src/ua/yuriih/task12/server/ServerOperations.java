package ua.yuriih.task12.server;

import ua.yuriih.task12.common.Group;
import ua.yuriih.task12.common.Operation;
import ua.yuriih.task12.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public final class ServerOperations {
    private final GroupDao groupDao;
    private final StudentDao studentDao;

    public ServerOperations(GroupDao groupDao, StudentDao studentDao) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    public ServerOperation getOperation(Operation op) {
        return switch (op) {
            case ADD_GROUP -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Group group = (Group) in.readObject();


                        groupDao.addGroup(group);

                        out.writeInt(group.getId());
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case DELETE_GROUP -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    int groupId = in.readInt();


                    groupDao.deleteGroup(groupId);

                    out.writeInt(0);
                    out.flush();
                }
            };
            case UPDATE_GROUP -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Group group = (Group) in.readObject();


                        groupDao.updateGroup(group);

                        out.writeInt(0);
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case GET_ALL_GROUPS -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    ArrayList<Group> groups = groupDao.getAllGroups();

                    out.writeInt(groups.size());
                    for (Group group : groups)
                        out.writeObject(group);
                    out.flush();
                }
            };
            case GET_STUDENTS_FROM_GROUP -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    int groupId = in.readInt();


                    ArrayList<Student> students = studentDao.getAllStudentsFromGroup(groupId);

                    out.writeInt(students.size());
                    for (Student student : students)
                        out.writeObject(student);
                    out.flush();
                }
            };
            case ADD_STUDENT -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Student student = (Student) in.readObject();


                        studentDao.addStudent(student);

                        out.writeInt(student.getId());
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case UPDATE_STUDENT -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    try {
                        Student student = (Student) in.readObject();


                        studentDao.updateStudent(student);

                        out.writeInt(0);
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            case DELETE_STUDENT -> new ServerOperation(groupDao, studentDao) {
                @Override
                public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
                    int studentId = in.readInt();


                    studentDao.deleteStudent(studentId);

                    out.writeInt(0);
                    out.flush();
                }
            };
        };
    }
}
