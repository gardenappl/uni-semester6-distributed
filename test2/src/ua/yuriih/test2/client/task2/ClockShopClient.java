package ua.yuriih.test2.client.task2;

import ua.yuriih.test2.client.ConsoleUI;

import java.io.*;
import java.net.Socket;

public class ClockShopClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected.");

        new ConsoleUI(new ClockShopViewModel(in, out)).run();
    }
}
