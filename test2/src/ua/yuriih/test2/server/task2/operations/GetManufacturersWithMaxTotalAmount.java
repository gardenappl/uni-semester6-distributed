package ua.yuriih.test2.server.task2.operations;

import ua.yuriih.test2.common.Manufacturer;
import ua.yuriih.test2.server.ClockModelDao;
import ua.yuriih.test2.server.ManufacturerDao;

import java.io.*;
import java.util.ArrayList;

public class GetManufacturersWithMaxTotalAmount extends Operation {
    public GetManufacturersWithMaxTotalAmount(ClockModelDao clockModelDao, ManufacturerDao manufacturerDao) {
        super(clockModelDao, manufacturerDao);
    }

    @Override
    public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        int maxTotalAmount = in.readInt();


        ArrayList<Manufacturer> result = manufacturerDao.getManufacturersWithMaxTotalClockAmount(maxTotalAmount);


        out.writeInt(result.size());
        for (Manufacturer manufacturer : result)
            out.writeObject(manufacturer);
        out.flush();
    }
}
