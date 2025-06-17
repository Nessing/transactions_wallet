package ru.nessing.transactions_wallet.utils;

//import lombok.Getter;
//
//@Getter
public enum OperationType {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),;

    private final String value;

    OperationType(final String value) {
        this.value = value;
    }

    public static OperationType getOperationsType(final String value) {
        for (final OperationType operationType : OperationType.values()) {
            if (operationType.value.equals(value)) {
                return operationType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
