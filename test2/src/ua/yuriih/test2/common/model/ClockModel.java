package ua.yuriih.test2.common.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClockModel implements Serializable {
    private int id;

    private final String name;
    private final int manufacturerId;

    private final ClockType type;
    private final BigDecimal price;
    private final int amount;

    public ClockModel(int id, String name, int manufacturerId, ClockType type, BigDecimal price, int amount) {
        this.id = id;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ClockType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    @Override
    public String toString() {
        return "ClockModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturerId=" + manufacturerId +
                ", type=" + type +
                ", cost=" + price +
                ", amount=" + amount +
                '}';
    }

    public int getAmount() {
        return amount;
    }
}
