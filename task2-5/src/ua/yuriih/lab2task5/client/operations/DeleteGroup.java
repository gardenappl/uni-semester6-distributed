package ua.yuriih.lab2task5.client.operations;

import com.rabbitmq.client.Channel;
import ua.yuriih.lab2task5.client.operations.ClientOperation;
import ua.yuriih.lab2task5.common.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeleteGroup extends ClientOperation {
    public DeleteGroup(Channel channel) throws IOException {
        super(Operation.DELETE_GROUP, channel);
    }

    public void performQuery(int groupId) throws IOException {
        writeCode(out);

        out.writeInt(groupId);
        send();
    }
}
