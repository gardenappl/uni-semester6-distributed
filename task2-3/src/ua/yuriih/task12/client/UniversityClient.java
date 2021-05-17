package ua.yuriih.task12.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UniversityClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());



        UniversityPanel panel = new UniversityPanel(in, out);

        JFrame frame = new JFrame("University 3.0");
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
