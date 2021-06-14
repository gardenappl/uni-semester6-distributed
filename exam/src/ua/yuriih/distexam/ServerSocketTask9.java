package ua.yuriih.distexam;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ua.yuriih.distexam.CommonSocketTask9.Product;

public class ServerSocketTask9 {
    public static final class ProductRepository {
        private ProductRepository() {
        }

        private static ProductRepository INSTANCE = null;

        public synchronized static ProductRepository getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new ProductRepository();
                INSTANCE.addProduct(new Product("Super Toothbrush", 124141249867L, "Teeth Inc.", BigDecimal.valueOf(59), 1000, 100));
                INSTANCE.addProduct(new Product("Super Toothbrush", 124141231234L, "Another Toothbrush Company", BigDecimal.valueOf(6950, 2), 2000, 60));
                INSTANCE.addProduct(new Product("Mega Toothpaste", 124113513599L, "Teeth Inc.", BigDecimal.valueOf(30), 200, 500));
            }
            return INSTANCE;
        }

        private final HashMap<Integer, Product> productsById = new HashMap<>();
        private int lastId = 0;

        public synchronized void addProduct(Product product) {
            if (product.getId() == 0) {
                lastId++;
                product.setId(lastId);
            }
            productsById.put(product.getId(), product);
        }

        public void deleteProduct(int id) {
            productsById.remove(id);
        }

        public void updateProduct(Product product) {
            productsById.replace(product.getId(), product);
        }

        public Product getProduct(int id) {
            return productsById.get(id);
        }

        public List<Product> getProductsWithName(String name) {
            ArrayList<Product> result = new ArrayList<>();
            for (Product product : productsById.values()) {
                if (product.getName().equals(name))
                    result.add(product);
            }
            return result;
        }

        public List<Product> getProductsWithNameCheaperOrEqualTo(String name, BigDecimal maxPriceUah) {
            ArrayList<Product> result = new ArrayList<>();
            for (Product product : productsById.values()) {
                if (product.getName().equals(name) && product.getPrice().compareTo(maxPriceUah) <= 0)
                    result.add(product);
            }
            return result;
        }

        public List<Product> getProductsWithMaxStorageGreaterThan(int daysMaxStorage) {
            ArrayList<Product> result = new ArrayList<>();
            for (Product product : productsById.values()) {
                if (product.getDaysMaxStorage() > daysMaxStorage)
                    result.add(product);
            }
            return result;
        }
    }

    private static final class ClientHandler {
        private final ObjectInputStream in;
        private final ObjectOutputStream out;

        private ClientHandler(ObjectOutputStream out, ObjectInputStream in) {
            this.in = in;
            this.out = out;
        }

        public void handleOperations() throws IOException, ClassNotFoundException {
            ProductRepository productRepository = ProductRepository.getInstance();

            while (!Thread.currentThread().isInterrupted()) {
                int operation = in.readInt();
                switch (operation) {
                    case 0 -> {
                        String name = in.readUTF();
                        List<Product> products = productRepository.getProductsWithName(name);

                        out.writeInt(products.size());
                        for (Product product : products)
                            out.writeObject(product);
                    }
                    case 1 -> {
                        String name = in.readUTF();
                        BigDecimal price = (BigDecimal) in.readObject();
                        List<Product> products = productRepository.getProductsWithNameCheaperOrEqualTo(name, price);

                        out.writeInt(products.size());
                        for (Product product : products)
                            out.writeObject(product);
                    }
                    case 2 -> {
                        int days = in.readInt();
                        List<Product> products = productRepository.getProductsWithMaxStorageGreaterThan(days);

                        out.writeInt(products.size());
                        for (Product product : products)
                            out.writeObject(product);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);

        int threads = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                while (!Thread.interrupted()) {
                    System.out.println("Started listening");
                    try (Socket socket = serverSocket.accept()) {
                        System.out.println("Accepted");
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("Got I/O streams");
                        ClientHandler handler = new ClientHandler(out, in);
                        handler.handleOperations();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
