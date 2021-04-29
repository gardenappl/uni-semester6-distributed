package ua.yuriih.test2.client.task2.operations;

import ua.yuriih.test2.common.model.ClockModel;
import ua.yuriih.test2.common.task2.Operations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GetMechanicalClocksCheaperThan extends Operation {
    public GetMechanicalClocksCheaperThan() {
        super(Operations.GET_MECHANICAL_CLOCKS_CHEAPER_THAN);
    }

    public ArrayList<ClockModel> performQuery(ObjectInputStream in, ObjectOutputStream out, BigDecimal price) throws IOException {
        writeCode(out);
        out.writeLong(price.unscaledValue().longValue());
        out.writeInt(price.scale());
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
