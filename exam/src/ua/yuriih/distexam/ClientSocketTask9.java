package ua.yuriih.distexam;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ua.yuriih.distexam.CommonSocketTask9.*;

public class ClientSocketTask9 {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected.");

        runConsoleUi(out, in);
    }

    private static <T> List<T> readList(ObjectInputStream in) throws IOException {
        int size = in.readInt();

        ArrayList<T> result = new ArrayList<>(size);
        try {
            for (int i = 0; i < size; i++) {
                result.add((T) in.readObject());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static void printHelp() {
        System.out.println("Enter command (0 = get products with name, " +
                "1 = get products with name cheaper than, " +
                "2 = get products with max storage days greater than):");

    }

    private static void runConsoleUi(ObjectOutputStream out, ObjectInputStream in) throws IOException {
        printHelp();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int command = scanner.nextInt();
            scanner.nextLine();


            switch (command) {
                case 0 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    out.writeInt(command);
                    out.writeUTF(name);
                    out.flush();

                    List<Product> products = readList(in);
                    for (Product product : products)
                        System.out.println(product);
                }
                case 1 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter max. price: ");
                    BigDecimal price = scanner.nextBigDecimal();

                    out.writeInt(command);
                    out.writeUTF(name);
                    out.writeObject(price);
                    out.flush();

                    List<Product> products = readList(in);
                    for (Product product : products)
                        System.out.println(product);
                }
                case 2 -> {
                    System.out.print("Storage days: ");
                    int storageDays = scanner.nextInt();

                    out.writeInt(command);
                    out.writeInt(storageDays);
                    out.flush();

                    List<Product> products = readList(in);
                    for (Product product : products)
                        System.out.println(product);
                }
            }

            printHelp();
        }
    }
}
