package ua.yuriih.test2.client.task2.operations;

import ua.yuriih.test2.common.Manufacturer;
import ua.yuriih.test2.common.task2.Operations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetManufacturersWithMaxTotalAmount extends Operation {
    public GetManufacturersWithMaxTotalAmount() {
        super(Operations.GET_MANUFACTURERS_WITH_MAX_TOTAL_AMOUNT);
    }

    public ArrayList<Manufacturer> performQuery(ObjectInputStream in, ObjectOutputStream out, int maxTotalAmount) throws IOException {
        writeCode(out);
        out.writeInt(maxTotalAmount);
        out.flush();


        int amount = in.readInt();
        ArrayList<Manufacturer> result = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            try {
                result.add((Manufacturer) in.readObject());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
