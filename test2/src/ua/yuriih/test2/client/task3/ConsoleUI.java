package ua.yuriih.test2.client.task3;

import ua.yuriih.test2.client.task2.operations.GetAllModelsByType;
import ua.yuriih.test2.client.task2.operations.GetAllModelsFromCountry;
import ua.yuriih.test2.client.task2.operations.GetManufacturersWithMaxTotalAmount;
import ua.yuriih.test2.client.task2.operations.GetMechanicalClocksCheaperThan;
import ua.yuriih.test2.common.model.ClockModel;
import ua.yuriih.test2.common.model.ClockType;
import ua.yuriih.test2.common.model.Manufacturer;
import ua.yuriih.test2.common.task3.IClockModelDao;
import ua.yuriih.test2.common.task3.IManufacturerDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {
    public void run(IClockModelDao clockModelDao, IManufacturerDao manufacturerDao) throws IOException {
        System.out.println("Enter command (CLOCKS_BY_TYPE, " +
                "CLOCKS_BY_COUNTRY, " +
                "CLOCKS_MECHANICAL_CHEAPER_THAN, " +
                "MANUFACTURERS_MAX_TOTAL_AMOUNT):");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (line) {
                case "CLOCKS_BY_TYPE" -> {
                    System.out.println("Enter type (0 = mechanical, 1 = piezo):");
                    int typeNum = scanner.nextInt();
                    ArrayList<ClockModel> clockModels =
                            clockModelDao.getAllModelsByType(ClockType.get(typeNum));
                    for (ClockModel clockModel : clockModels)
                        System.out.println(clockModel);
                }
                case "CLOCKS_BY_COUNTRY" -> {
                    System.out.println("Enter country:");
                    String country = scanner.nextLine();
                    ArrayList<ClockModel> clockModels =
                            clockModelDao.getAllModelsFromCountry(country);
                    for (ClockModel clockModel : clockModels)
                        System.out.println(clockModel);
                }
                case "CLOCKS_MECHANICAL_CHEAPER_THAN" -> {
                    System.out.println("Enter price:");
                    BigDecimal price = scanner.nextBigDecimal();
                    ArrayList<ClockModel> clockModels =
                            clockModelDao.getMechanicalModelsCheaperThan(price);
                    for (ClockModel clockModel : clockModels)
                        System.out.println(clockModel);
                }
                case "MANUFACTURERS_MAX_TOTAL_AMOUNT" -> {
                    System.out.println("Enter max. total amount:");
                    int maxTotalAmount = scanner.nextInt();
                    ArrayList<Manufacturer> manufacturers =
                            manufacturerDao.getManufacturersWithMaxTotalClockAmount(maxTotalAmount);
                    for (Manufacturer manufacturer : manufacturers)
                        System.out.println(manufacturer);
                }
                default -> {
                    System.out.println("Unknown command.");
                }
            }

            System.out.println("Enter command (CLOCKS_BY_TYPE, " +
                    "CLOCKS_BY_COUNTRY, " +
                    "CLOCKS_MECHANICAL_CHEAPER_THAN, " +
                    "MANUFACTURERS_MAX_TOTAL_AMOUNT):");
        }
    }
}
