package ua.yuriih.task12.server;

import ua.yuriih.task12.common.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientHandler {
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final GroupDao groupDao;
    private final StudentDao studentDao;

    public ClientHandler(ObjectInputStream in, ObjectOutputStream out,
                         GroupDao groupDao, StudentDao studentDao) {

        this.in = in;
        this.out = out;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    public void run() throws IOException {
        System.out.println("Connected.");
        while (!Thread.interrupted()) {
            ServerOperations factory = new ServerOperations(groupDao, studentDao);

            int operationType = in.readInt();
            Operation op = Operation.get(operationType);
            System.err.println("Operation " + op);
            ServerOperation operation = factory.getOperation(op);
            operation.handleQuery(in, out);
        }
    }
}
