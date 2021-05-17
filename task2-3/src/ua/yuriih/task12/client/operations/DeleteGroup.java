package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Group;
import ua.yuriih.task12.common.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeleteGroup extends ClientOperation {
    public DeleteGroup() {
        super(Operation.DELETE_GROUP);
    }

    public void performQuery(ObjectInputStream in, ObjectOutputStream out, int groupId) throws IOException {
        writeCode(out);

        out.writeInt(groupId);
        out.flush();


        int success = in.readInt();
        if (success != 0)
            throw new RuntimeException("Something wrong happened on the server: " + success);
    }
}
