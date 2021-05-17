package ua.yuriih.test2.server.task2.operations;

import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.server.ClockModelDao;
import ua.yuriih.test2.server.ManufacturerDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GetAllModelsFromCountry extends Operation {
    public GetAllModelsFromCountry(ClockModelDao clockModelDao, ManufacturerDao manufacturerDao) {
        super(clockModelDao, manufacturerDao);
    }

    @Override
    public void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        String country = in.readUTF();


        ArrayList<ClockModel> result = clockModelDao.getAllModelsFromCountry(country);


        out.writeInt(result.size());
        for (ClockModel clockModel : result)
            out.writeObject(clockModel);
        out.flush();
    }
}
