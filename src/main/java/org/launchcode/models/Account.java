package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private int id;

    @NotNull @Size(min = 1, max = 15)
    private String name;

    @NotNull
    private BigDecimal initialAmt;

    @OneToMany
    @JoinColumn(name="account_id")
    private List<Balance> balances = new ArrayList<>();

    @OneToMany
    @JoinColumn(name="account_id")
    private List<Payment> payments = new ArrayList<>();

    public Account() {}

    public Account(String name, BigDecimal initialAmt) {
        this.name = name;
        this.initialAmt = initialAmt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInitialBalance() {
        return initialAmt;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialAmt = initialBalance;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
