package ua.yuriih.lab2task5.client.operations;

import com.rabbitmq.client.Channel;
import ua.yuriih.lab2task5.common.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeleteStudent extends ClientOperation {
    public DeleteStudent(Channel channel) throws IOException {
        super(Operation.DELETE_STUDENT, channel);
    }

    public void performQuery(int studentId) throws IOException {
        writeCode(out);

        out.writeInt(studentId);
        send();
    }
}
