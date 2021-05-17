package ua.yuriih.lab2task5.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class ServerOperation {
    protected final GroupDao groupDao;
    protected final StudentDao studentDao;
    protected final Channel channel;

    public ServerOperation(GroupDao groupDao, StudentDao studentDao, Channel channel) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.channel = channel;
    }

    public abstract void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException;
}
