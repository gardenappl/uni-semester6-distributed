package ua.yuriih.lab2task5.client.operations;

import com.rabbitmq.client.Channel;
import ua.yuriih.lab2task5.client.operations.ClientOperation;
import ua.yuriih.lab2task5.common.Group;
import ua.yuriih.lab2task5.common.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetAllGroups extends ClientOperation {
    public GetAllGroups(Channel channel) throws IOException {
        super(Operation.GET_ALL_GROUPS, channel);
    }

    public void performQuery() throws IOException {
        writeCode(out);
        send();
    }
}
