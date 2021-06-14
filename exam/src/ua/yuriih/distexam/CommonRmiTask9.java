package ua.yuriih.distexam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class CommonRmiTask9 {
    public interface IProductSearchService extends Remote {
        List<Product> getProductsWithName(String name) throws RemoteException;
        List<Product> getProductsWithNameCheaperOrEqualTo(String name, BigDecimal maxPriceUah) throws RemoteException;
        List<Product> getProductsWithMaxStorageGreaterThan(int daysMaxStorage) throws RemoteException;
    }

    public static final class Product implements Serializable {
        private int id;
        private String name;
        private long upc;
        private String manufacturer;
        private BigDecimal price;
        private int daysMaxStorage;
        private int amount;

        public Product(String name, long upc, String manufacturer, BigDecimal price, int daysMaxStorage, int amount) {
            this.id = 0;
            this.name = name;
            this.upc = upc;
            this.manufacturer = manufacturer;
            this.price = price;
            this.daysMaxStorage = daysMaxStorage;
            this.amount = amount;
        }

        public Product(int id, String name, long upc, String manufacturer, BigDecimal price, int daysMaxStorage, int amount) {
            this.id = id;
            this.name = name;
            this.upc = upc;
            this.manufacturer = manufacturer;
            this.price = price;
            this.daysMaxStorage = daysMaxStorage;
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getUpc() {
            return upc;
        }

        public void setUpc(long upc) {
            this.upc = upc;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getDaysMaxStorage() {
            return daysMaxStorage;
        }

        public void setDaysMaxStorage(int daysMaxStorage) {
            this.daysMaxStorage = daysMaxStorage;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", upc=" + upc +
                    ", manufacturer='" + manufacturer + '\'' +
                    ", price=" + price +
                    ", daysMaxStorage=" + daysMaxStorage +
                    ", amount=" + amount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return id == product.id && upc == product.upc && daysMaxStorage == product.daysMaxStorage && amount == product.amount && name.equals(product.name) && manufacturer.equals(product.manufacturer) && price.equals(product.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, upc, manufacturer, price, daysMaxStorage, amount);
        }
    }

}
