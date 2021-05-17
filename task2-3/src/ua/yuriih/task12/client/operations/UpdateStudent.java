package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Operation;
import ua.yuriih.task12.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UpdateStudent extends ClientOperation {
    public UpdateStudent() {
        super(Operation.UPDATE_STUDENT);
    }

    public void performQuery(ObjectInputStream in, ObjectOutputStream out, Student student) throws IOException {
        writeCode(out);

        out.writeObject(student);
        out.flush();


        int success = in.readInt();
        if (success != 0)
            throw new RuntimeException("Something went wrong on the server");
    }
}
