package ua.yuriih.lab2task5.common;

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

    //Responses
    public static final int UPDATE_GROUPS_LIST = 0;
    public static final int UPDATE_STUDENTS_LIST = 1;
    public static final int RECEIVE_GROUPS_LIST = 2;
    public static final int RECEIVE_STUDENTS_LIST = 3;

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
