package ua.yuriih.lab2task5.client.operations;

import com.rabbitmq.client.Channel;
import ua.yuriih.lab2task5.common.Group;
import ua.yuriih.lab2task5.common.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UpdateGroup extends ClientOperation {
    public UpdateGroup(Channel channel) throws IOException {
        super(Operation.UPDATE_GROUP, channel);
    }

    public void performQuery(Group group) throws IOException {
        writeCode(out);

        out.writeObject(group);
        send();
    }
}
