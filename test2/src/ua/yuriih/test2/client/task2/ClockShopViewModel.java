package ua.yuriih.test2.client.task2;

import ua.yuriih.test2.client.AbstractClockShopViewModel;
import ua.yuriih.test2.client.task2.operations.GetAllModelsByType;
import ua.yuriih.test2.client.task2.operations.GetAllModelsFromCountry;
import ua.yuriih.test2.client.task2.operations.GetManufacturersWithMaxTotalAmount;
import ua.yuriih.test2.client.task2.operations.GetMechanicalClocksCheaperThan;
import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.common.ClockType;
import ua.yuriih.test2.common.Manufacturer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ClockShopViewModel extends AbstractClockShopViewModel {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ClockShopViewModel(ObjectInputStream in, ObjectOutputStream out) {
        this.out = out;
        this.in = in;
    }

    public ArrayList<ClockModel> getAllModelsByType(ClockType type) {
        try {
            GetAllModelsByType operation = new GetAllModelsByType();
            return operation.performQuery(in, out, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClockModel> getAllModelsFromCountry(String country) {
        try {
            GetAllModelsFromCountry operation = new GetAllModelsFromCountry();
            return operation.performQuery(in, out, country);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ClockModel> getMechanicalModelsCheaperThan(BigDecimal price) {
        try {
            GetMechanicalClocksCheaperThan operation = new GetMechanicalClocksCheaperThan();
            return operation.performQuery(in, out, price);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Manufacturer> getManufacturersWithMaxTotalAmount(int maxTotalAmount) {
        try {
            GetManufacturersWithMaxTotalAmount operation = new GetManufacturersWithMaxTotalAmount();
            return operation.performQuery(in, out, maxTotalAmount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
