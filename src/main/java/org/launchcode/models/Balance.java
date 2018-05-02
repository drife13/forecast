package org.launchcode.models;

import org.launchcode.models.data.PaymentDao;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Balance {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Account account;

    private BigDecimal amt;

    private LocalDate date;

    @OneToOne
    private Balance prevBalance;

    @OneToMany
    @JoinColumn(name="balance_id")
    private List<Payment> payments = new ArrayList<>();

    public Balance() {}

    public Balance(Balance prevBalance) {
        this.prevBalance = prevBalance;
        this.account = prevBalance.getAccount();
        this.date = prevBalance.getDate().plusDays(1);
        this.amt = prevBalance.getAmt();
    }

    public void pointPaymentsToThisBalance(List<Payment> payments, PaymentDao paymentDao) {
        for (Payment payment : payments) {
            this.amt.add(payment.getAmt());
            payment.setBalance(this);
            paymentDao.save(payment);
        }
    }

    public int getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Balance getPrevBalance() {
        return prevBalance;
    }

    public void setPrevBalance(Balance prevBalance) {
        this.prevBalance = prevBalance;
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
