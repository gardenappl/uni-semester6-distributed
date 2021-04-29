package ua.yuriih.test2.client.task2.operations;

import ua.yuriih.test2.common.model.ClockModel;
import ua.yuriih.test2.common.model.ClockType;
import ua.yuriih.test2.common.task2.Operations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetAllModelsFromCountry extends Operation {
    public GetAllModelsFromCountry() {
        super(Operations.GET_CLOCKS_FROM_COUNTRY);
    }

    public ArrayList<ClockModel> performQuery(ObjectInputStream in, ObjectOutputStream out, String country) throws IOException {
        writeCode(out);
        out.writeUTF(country);
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
