package ua.yuriih.task12.client.operations;

import ua.yuriih.task12.common.Group;
import ua.yuriih.task12.common.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetAllGroups extends ClientOperation {
    public GetAllGroups() {
        super(Operation.GET_ALL_GROUPS);
    }

    public ArrayList<Group> performQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        writeCode(out);
        out.flush();

        int count = in.readInt();
        ArrayList<Group> groups = new ArrayList<>(count);
        try {
            for (int i = 0; i < count; i++)
                groups.add((Group) in.readObject());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }
}
