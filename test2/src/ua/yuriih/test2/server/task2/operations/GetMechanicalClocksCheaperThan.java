package ua.yuriih.test2.server.task2.operations;

import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.server.ClockModelDao;
import ua.yuriih.test2.server.ManufacturerDao;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GetMechanicalClocksCheaperThan extends Operation {
    public GetMechanicalClocksCheaperThan(ClockModelDao clockModelDao, ManufacturerDao manufacturerDao) {
        super(clockModelDao, manufacturerDao);
    }

    @Override
    public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        BigDecimal price = BigDecimal.valueOf(in.readLong(), in.readInt());


        ArrayList<ClockModel> result = clockModelDao.getMechanicalModelsCheaperThan(price);


        out.writeInt(result.size());
        for (ClockModel clockModel : result)
            out.writeObject(clockModel);
        out.flush();
    }
}
