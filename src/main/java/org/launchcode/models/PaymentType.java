package org.launchcode.models;

import org.launchcode.models.forms.AddPaymentTypeForm;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PaymentType {
    @Id
    @GeneratedValue
    private int id;

    @NotNull @Size(min = 1, max = 15)
    private String name;

    @NotNull
    private BigDecimal amt;

    @NotNull
    private LocalDate startDate;

    @NotNull(message = "End date must be after start date.")
    private LocalDate endDate;

    @NotNull
    private Frequency frequency;

    @ManyToOne
    private Account account;

    @OneToMany
    @JoinColumn(name="payment_type_id")
    private List<Payment> payments = new ArrayList<>();

    public PaymentType() {}

    public PaymentType(AddPaymentTypeForm form, Account account) {
        this.name = form.getName();
        this.amt = form.getAmt();
        this.startDate = form.getStartDate();
        this.endDate = form.getEndDate();
        this.frequency = form.getFrequency();
        this.account = account;
    }

    private void checkEndDate() {
        if (this.endDate.isBefore(this.startDate)) {
            this.endDate = null;
        }
    }

    private void setPayments(LocalDate simStartDate, LocalDate simEndDate) {
        if (simStartDate.isBefore(this.startDate)) simStartDate = this.startDate;
        if (simEndDate.isAfter(this.endDate)) simEndDate = this.endDate;
        if (simEndDate.isBefore(simStartDate)) simEndDate = simStartDate;

        this.payments = new ArrayList<>();
        List<LocalDate> dates = generateDateList(simStartDate, simEndDate, this.frequency);
        for (LocalDate date : dates)
        {
            Payment payment = new Payment();
            payment.setPaymentType(this);
            payment.setAccount(this.account);
            payment.setAmt(this.amt);
            payment.setDate(date);

            this.payments.add(payment);
        }
    }

    private List<LocalDate> generateDateList(LocalDate start, LocalDate end, Frequency frequency) {
        List<LocalDate> dates = new ArrayList<>();

        int i = 0;
        LocalDate current = start;
        while (current.isBefore(end) || current.isEqual(end)) {
            dates.add(current);

            switch (frequency) {
                case SINGLE:
                case DAILY:
                    current = start.plusDays(++i);
                    break;

                case WEEKLY:
                    current = start.plusWeeks(++i);
                    break;

                case BIWEEKLY:
                    current = start.plusWeeks(2*++i);
                    break;

                case MONTHLY:
                    current = start.plusMonths(++i);
                    break;

                case ANNUALLY:
                    current = start.plusYears(++i);
                    break;
            }
        }
        return dates;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public LocalDate getStartDateDate() {
        return startDate;
    }

    public void setStartDate(LocalDate initialDate) {
        this.startDate = initialDate;
        checkEndDate();
    }

    public LocalDate getEndDateDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        checkEndDate();
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }
}
