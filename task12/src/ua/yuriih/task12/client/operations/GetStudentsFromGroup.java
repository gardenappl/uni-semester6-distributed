package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Operation;
import ua.yuriih.task12.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetStudentsFromGroup extends ClientOperation {
    public GetStudentsFromGroup() {
        super(Operation.GET_STUDENTS_FROM_GROUP);
    }

    public ArrayList<Student> performQuery(ObjectInputStream in, ObjectOutputStream out, int groupId) throws IOException {
        writeCode(out);

        out.writeInt(groupId);
        out.flush();


        int count = in.readInt();
        ArrayList<Student> students = new ArrayList<>(count);
        try {
            for (int i = 0; i < count; i++)
                students.add((Student) in.readObject());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return students;
    }
}
