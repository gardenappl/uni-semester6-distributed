package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Operation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public abstract class ClientOperation {
    private final Operation op;

    public ClientOperation(Operation op) {
        this.op = op;
    }

    protected void writeCode(ObjectOutputStream out) throws IOException {
        out.writeInt(op.id);
    }
}
