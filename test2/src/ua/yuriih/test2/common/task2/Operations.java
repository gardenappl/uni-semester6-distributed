package ua.yuriih.test2.common.task2;

import ua.yuriih.test2.common.model.ClockType;

import java.util.HashMap;

public enum Operations {
    GET_CLOCKS_BY_TYPE(1),
    GET_MECHANICAL_CLOCKS_CHEAPER_THAN(2),
    GET_CLOCKS_FROM_COUNTRY(3),
    GET_MANUFACTURERS_WITH_MAX_TOTAL_AMOUNT(4);

    public final int value;
    private static final HashMap<Integer, Operations> operations = new HashMap<>();

    Operations(int value) {
        this.value = value;
    }

    public static Operations get(int value) {
        return operations.get(value);
    }

    static {
        for (Operations type : Operations.values())
            operations.put(type.value, type);
    }
}
