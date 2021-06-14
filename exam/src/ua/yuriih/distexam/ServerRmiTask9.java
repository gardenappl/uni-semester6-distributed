package ua.yuriih.distexam;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ua.yuriih.distexam.CommonRmiTask9.*;

public class ServerRmiTask9 {
    public static final class ProductSearchService extends UnicastRemoteObject implements IProductSearchService {
        private final ProductRepository productRepository;

        public ProductSearchService(ProductRepository repository) throws RemoteException {
            super();
            this.productRepository = repository;
        }

        @Override
        public List<Product> getProductsWithName(String name) throws RemoteException {
            return productRepository.getProductsWithName(name);
        }

        @Override
        public List<Product> getProductsWithNameCheaperOrEqualTo(String name, BigDecimal maxPriceUah) throws RemoteException {
            return productRepository.getProductsWithNameCheaperOrEqualTo(name, maxPriceUah);
        }

        @Override
        public List<Product> getProductsWithMaxStorageGreaterThan(int daysMaxStorage) throws RemoteException {
            return productRepository.getProductsWithMaxStorageGreaterThan(daysMaxStorage);
        }
    }

    public static final class ProductRepository {
        private ProductRepository() {}

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

    public static void main(String[] args) throws IOException {
        Registry registry = LocateRegistry.createRegistry(12347);

        ProductRepository productRepository = ProductRepository.getInstance();
        ProductSearchService productSearchService = new ProductSearchService(productRepository);

        System.err.println("Binding...");
        registry.rebind("//127.0.0.1/products", productSearchService);
        System.err.println("Bound products search service");
    }
}
