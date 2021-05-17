package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Operation;
import ua.yuriih.task12.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeleteStudent extends ClientOperation {
    public DeleteStudent() {
        super(Operation.DELETE_STUDENT);
    }

    public void performQuery(ObjectInputStream in, ObjectOutputStream out, int studentId) throws IOException {
        writeCode(out);

        out.writeInt(studentId);
        out.flush();


        int success = in.readInt();
        if (success != 0)
            throw new RuntimeException("Something went wrong on the server");
    }
}
