package me.ablax.financemanager.dto;

public class Transaction {
    private final Integer id;
    private final String name;
    private final Double amount;

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Transaction(Integer id, String name, Double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Transaction(String name, Double amount) {
        this.name = name;
        this.amount = amount;
        this.id = 0;
    }

    public Integer getId() {
        return id;
    }
}
