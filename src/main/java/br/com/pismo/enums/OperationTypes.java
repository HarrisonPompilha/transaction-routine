package br.com.pismo.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum OperationTypes {

    CASH_PURCHASE(1, "COMPRA À VISTA", -1),
    PURCHASE_IN_INSTALLMENTS(2, "COMPRA PARCELADA", -1),
    WITHDRAW(3, "SAQUE", -1),
    PAYMENT(4, "PAGAMENTO", 1);

    private int id;
    private String description;
    private int calculation;

    OperationTypes(int id, String description, int calculation) {
        this.id = id;
        this.description = description;
        this.calculation = calculation;
    }
    
    public static Optional<OperationTypes> fromId(int id) {
        return Arrays.asList(values()).stream().filter(operation -> operation.id == id).findFirst();
    }
    
}
