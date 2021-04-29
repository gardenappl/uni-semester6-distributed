package ua.yuriih.test2.common;

public enum ClockType {
    MECHANICAL(0),
    PIEZO(1);

    public final int value;
    private static final ClockType[] TYPES;

    ClockType(int value) {
        this.value = value;
    }

    public static ClockType get(int value) {
        return TYPES[value];
    }

    static {
        TYPES = new ClockType[ClockType.values().length];
        for (ClockType type : ClockType.values())
            TYPES[type.value] = type;
    }
}
