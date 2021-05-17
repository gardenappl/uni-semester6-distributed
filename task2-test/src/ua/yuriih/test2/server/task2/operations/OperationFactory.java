package ua.yuriih.test2.server.task2.operations;

import ua.yuriih.test2.common.task2.Operations;
import ua.yuriih.test2.server.ClockModelDao;
import ua.yuriih.test2.server.ManufacturerDao;

public final class OperationFactory {
    private final ClockModelDao clockModelDao;
    private final ManufacturerDao manufacturerDao;

    public OperationFactory(ClockModelDao clockModelDao, ManufacturerDao manufacturerDao) {
        this.clockModelDao = clockModelDao;
        this.manufacturerDao = manufacturerDao;
    }

    public Operation make(Operations type) {
        return switch (type) {
            case GET_CLOCKS_BY_TYPE -> new GetAllModelsByType(clockModelDao, manufacturerDao);
            case GET_MECHANICAL_CLOCKS_CHEAPER_THAN -> new GetMechanicalClocksCheaperThan(clockModelDao, manufacturerDao);
            case GET_CLOCKS_FROM_COUNTRY -> new GetAllModelsFromCountry(clockModelDao, manufacturerDao);
            case GET_MANUFACTURERS_WITH_MAX_TOTAL_AMOUNT -> new GetManufacturersWithMaxTotalAmount(clockModelDao, manufacturerDao);
        };
    }
}
