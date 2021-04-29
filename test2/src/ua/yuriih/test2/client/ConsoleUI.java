package ua.yuriih.test2.client;

import ua.yuriih.test2.common.ClockModel;
import ua.yuriih.test2.common.ClockType;
import ua.yuriih.test2.common.Manufacturer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {
    private final AbstractClockShopViewModel viewModel;

    public ConsoleUI(AbstractClockShopViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void run() {
        System.out.println("Enter command (CLOCKS_BY_TYPE, " +
                "CLOCKS_BY_COUNTRY, " +
                "CLOCKS_MECHANICAL_CHEAPER_THAN, " +
                "MANUFACTURERS_MAX_TOTAL_AMOUNT):");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.isEmpty())
                continue;

            switch (line) {
                case "CLOCKS_BY_TYPE" -> {
                    System.out.println("Enter type (0 = mechanical, 1 = piezo):");
                    int typeNum = scanner.nextInt();
                    ArrayList<ClockModel> clockModels =
                            viewModel.getAllModelsByType(ClockType.get(typeNum));
                    for (ClockModel clockModel : clockModels)
                        System.out.println(clockModel);
                }
                case "CLOCKS_BY_COUNTRY" -> {
                    System.out.println("Enter country:");
                    String country = scanner.nextLine();
                    ArrayList<ClockModel> clockModels =
                            viewModel.getAllModelsFromCountry(country);
                    for (ClockModel clockModel : clockModels)
                        System.out.println(clockModel);
                }
                case "CLOCKS_MECHANICAL_CHEAPER_THAN" -> {
                    System.out.println("Enter price:");
                    BigDecimal price = scanner.nextBigDecimal();
                    ArrayList<ClockModel> clockModels =
                            viewModel.getMechanicalModelsCheaperThan(price);
                    for (ClockModel clockModel : clockModels)
                        System.out.println(clockModel);
                }
                case "MANUFACTURERS_MAX_TOTAL_AMOUNT" -> {
                    System.out.println("Enter max. total amount:");
                    int maxTotalAmount = scanner.nextInt();
                    ArrayList<Manufacturer> manufacturers =
                            viewModel.getManufacturersWithMaxTotalAmount(maxTotalAmount);
                    for (Manufacturer manufacturer : manufacturers)
                        System.out.println(manufacturer);
                }
            }
            System.out.println("Enter command (CLOCKS_BY_TYPE, " +
                    "CLOCKS_BY_COUNTRY, " +
                    "CLOCKS_MECHANICAL_CHEAPER_THAN, " +
                    "MANUFACTURERS_MAX_TOTAL_AMOUNT):");
        }
    }
}
