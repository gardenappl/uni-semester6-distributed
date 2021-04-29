package ua.yuriih.test2.server.task2.operations;

import ua.yuriih.test2.server.ClockModelDao;
import ua.yuriih.test2.server.ManufacturerDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Operation {
    protected final ClockModelDao clockModelDao;
    protected final ManufacturerDao manufacturerDao;

    public Operation(ClockModelDao clockModelDao, ManufacturerDao manufacturerDao) {
        this.clockModelDao = clockModelDao;
        this.manufacturerDao = manufacturerDao;
    }

    public abstract void handleQuery(ObjectInputStream in, ObjectOutputStream out) throws IOException;
}
