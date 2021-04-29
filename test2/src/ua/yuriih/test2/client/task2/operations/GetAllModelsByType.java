package ua.yuriih.test2.client.task2.operations;

import ua.yuriih.test2.common.model.ClockModel;
import ua.yuriih.test2.common.model.ClockType;
import ua.yuriih.test2.common.task2.Operations;
import ua.yuriih.test2.server.dao.ClockModelDao;
import ua.yuriih.test2.server.dao.ManufacturerDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetAllModelsByType extends Operation {
    public GetAllModelsByType() {
        super(Operations.GET_CLOCKS_BY_TYPE);
    }

    public ArrayList<ClockModel> performQuery(ObjectInputStream in, ObjectOutputStream out, ClockType type) throws IOException {
        writeCode(out);
        out.writeInt(type.value);
        out.flush();


        int amount = in.readInt();
        ArrayList<ClockModel> result = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            try {
                result.add((ClockModel) in.readObject());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
