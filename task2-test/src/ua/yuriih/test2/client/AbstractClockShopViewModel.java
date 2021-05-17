package ua.yuriih.test2.client;

import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.common.ClockType;
import ua.yuriih.test2.common.Manufacturer;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class AbstractClockShopViewModel {
    public abstract ArrayList<ClockModel> getAllModelsByType(ClockType type);
    public abstract ArrayList<ClockModel> getAllModelsFromCountry(String country);
    public abstract ArrayList<ClockModel> getMechanicalModelsCheaperThan(BigDecimal price);
    public abstract ArrayList<Manufacturer> getManufacturersWithMaxTotalAmount(int maxTotalAmount);
}
