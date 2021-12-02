package me.ablax.financemanager.dto;

public class PurchaseDto {
    private final Integer id;
    private final Integer transactionId;
    private final String name;
    private final Integer quantity;

    public PurchaseDto(final Integer transactionId, final String name, final Integer quantity) {
        this.transactionId = transactionId;
        this.name = name;
        this.quantity = quantity;
        this.id = 0;
    }

    public PurchaseDto(final Integer id, final Integer transactionId, final String name, final Integer quantity) {
        this.id = id;
        this.transactionId = transactionId;
        this.name = name;
        this.quantity = quantity;
    }

    public Integer getId() {

        return id;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
