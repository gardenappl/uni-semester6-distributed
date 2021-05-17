package ua.yuriih.lab2task5.client.operations;

import com.rabbitmq.client.Channel;
import ua.yuriih.lab2task5.common.Operation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import static ua.yuriih.lab2task5.client.UniversityClient.CLIENT_TO_SERVER;

public abstract class ClientOperation {
    private final Operation op;
    private final ByteArrayOutputStream bytesOut;
    protected final ObjectOutputStream out;
    private final Channel channel;

    public ClientOperation(Operation op, Channel channel) throws IOException {
        this.op = op;
        bytesOut = new ByteArrayOutputStream();
        out = new ObjectOutputStream(bytesOut);
        this.channel = channel;
    }

    protected void writeCode(ObjectOutputStream out) throws IOException {
        out.writeInt(op.id);
    }

    protected void send() throws IOException {
        out.flush();
        channel.basicPublish("", CLIENT_TO_SERVER, null, bytesOut.toByteArray());
    }
}
