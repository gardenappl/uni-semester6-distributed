package ua.yuriih.lab2task5.client.operations;

import com.rabbitmq.client.Channel;
import ua.yuriih.lab2task5.common.Operation;
import ua.yuriih.lab2task5.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddStudent extends ClientOperation {
    public AddStudent(Channel channel) throws IOException {
        super(Operation.ADD_STUDENT, channel);
    }

    public void performQuery(Student student) throws IOException {
        writeCode(out);

        out.writeObject(student);
        send();
    }
}
