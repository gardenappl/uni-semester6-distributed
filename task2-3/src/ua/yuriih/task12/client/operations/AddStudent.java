package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Operation;
import ua.yuriih.task12.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddStudent extends ClientOperation {
    public AddStudent() {
        super(Operation.ADD_STUDENT);
    }

    public int performQuery(ObjectInputStream in, ObjectOutputStream out, Student student) throws IOException {
        writeCode(out);

        out.writeObject(student);
        out.flush();


        int id = in.readInt();
        student.setId(id);
        return id;
    }
}
