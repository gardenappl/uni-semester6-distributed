package ua.yuriih.distexam;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ua.yuriih.distexam.CommonRmiTask9.*;

public class ClientRmiTask9 {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(12347);
        IProductSearchService productSearchService =
                (IProductSearchService) registry.lookup("//127.0.0.1/products");

        runConsoleUi(productSearchService);
    }

    private static void printHelp() {
        System.out.println("Enter command (0 = get products with name, " +
                "1 = get products with name cheaper than, " +
                "2 = get products with max storage days greater than):");

    }

    private static void runConsoleUi(IProductSearchService productSearchService) throws RemoteException {
        printHelp();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int command = scanner.nextInt();
            scanner.nextLine();

            switch (command) {
                case 0 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    List<Product> products = productSearchService.getProductsWithName(name);
                    for (Product product : products)
                        System.out.println(product);
                }
                case 1 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter max. price: ");
                    BigDecimal price = scanner.nextBigDecimal();

                    List<Product> products = productSearchService.getProductsWithNameCheaperOrEqualTo(name, price);
                    for (Product product : products)
                        System.out.println(product);
                }
                case 2 -> {
                    System.out.print("Storage days: ");
                    int storageDays = scanner.nextInt();
                    List<Product> products = productSearchService.getProductsWithMaxStorageGreaterThan(storageDays);
                    for (Product product : products)
                        System.out.println(product);
                }
            }
            printHelp();
        }
    }
}
