package ua.yuriih.task13.common;

import java.util.HashMap;

public enum Operation {
    GET_ALL_GROUPS(0),
    ADD_GROUP(1),
    UPDATE_GROUP(2),
    DELETE_GROUP(3),
    GET_STUDENTS_FROM_GROUP(4),
    ADD_STUDENT(5),
    UPDATE_STUDENT(6),
    DELETE_STUDENT(7);

    public final int id;
    private static final HashMap<Integer, Operation> operations = new HashMap<>();

    Operation(int id) {
        this.id = id;
    }

    public static Operation get(int id) {
        return operations.get(id);
    }

    static {
        for (Operation type : Operation.values())
            operations.put(type.id, type);
    }
}
