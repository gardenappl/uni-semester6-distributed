package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Group;
import ua.yuriih.task12.common.Operation;
import ua.yuriih.task12.common.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddGroup extends ClientOperation {
    public AddGroup() {
        super(Operation.ADD_GROUP);
    }

    public int performQuery(ObjectInputStream in, ObjectOutputStream out, Group group) throws IOException {
        writeCode(out);

        out.writeObject(group);
        out.flush();


        int id = in.readInt();
        group.setId(id);
        return id;
    }
}
