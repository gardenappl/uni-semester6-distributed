package ua.yuriih.task4bus;

import java.util.Objects;

public class Route {
    public final String city1;
    public final String city2;
    public final int price;

    public Route(String city1, String city2, int price) {
        if (city1.compareTo(city2) < 0) {
            this.city1 = city1;
            this.city2 = city2;
        } else {
            this.city1 = city2;
            this.city2 = city1;
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "city1='" + city1 + '\'' +
                ", city2='" + city2 + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return price == route.price && Objects.equals(city1, route.city1) && Objects.equals(city2, route.city2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city1, city2, price);
    }
}
