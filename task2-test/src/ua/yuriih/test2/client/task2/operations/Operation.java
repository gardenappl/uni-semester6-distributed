package ua.yuriih.test2.client.task2.operations;

import ua.yuriih.test2.common.task2.Operations;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Operation {
    private final Operations type;

    public Operation(Operations type) {
        this.type = type;
    }

    protected void writeCode(ObjectOutputStream out) throws IOException {
        out.writeInt(type.value);
    }
}
