package ua.yuriih.lab2task5.client.operations;

import com.rabbitmq.client.Channel;
import ua.yuriih.lab2task5.common.Operation;
import ua.yuriih.lab2task5.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetStudentsFromGroup extends ClientOperation {
    public GetStudentsFromGroup(Channel channel) throws IOException {
        super(Operation.GET_STUDENTS_FROM_GROUP, channel);
    }

    public void performQuery(int groupId) throws IOException {
        writeCode(out);

        out.writeInt(groupId);
        send();
    }
}
