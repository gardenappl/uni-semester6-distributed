package ua.yuriih.test2.server.task2;

import ua.yuriih.test2.common.task2.Operations;
import ua.yuriih.test2.server.dao.ClockModelDao;
import ua.yuriih.test2.server.dao.ManufacturerDao;
import ua.yuriih.test2.server.task2.operations.Operation;
import ua.yuriih.test2.server.task2.operations.OperationFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final ClockModelDao clockModelDao;
    private final ManufacturerDao manufacturerDao;

    public ClientHandler(ObjectOutputStream out, ObjectInputStream in,
                         ClockModelDao clockModelDao, ManufacturerDao manufacturerDao) {
        this.out = out;
        this.in = in;
        this.clockModelDao = clockModelDao;
        this.manufacturerDao = manufacturerDao;
    }

    public void run() throws IOException {
        System.out.println("Connected.");
        while (!Thread.interrupted()) {
            OperationFactory factory = new OperationFactory(clockModelDao, manufacturerDao);

            int operationType = in.readInt();
            Operation operation = factory.make(Operations.get(operationType));
            operation.handleQuery(in, out);
        }
    }
}
