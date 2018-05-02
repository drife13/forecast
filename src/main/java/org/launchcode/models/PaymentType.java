package org.launchcode.models;

import org.launchcode.models.data.PaymentDao;
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

    public void ProcessForm(AddPaymentTypeForm form, Account account) {
        this.name = form.getName();
        this.amt = form.getAmt();
        this.startDate = form.getStartDate();
        this.endDate = form.getEndDate();
        this.frequency = form.getFrequency();
        this.account = account;
    }

    private void checkEndDate() {
        if (startDate != null && endDate != null) {
            if (this.endDate.isBefore(this.startDate)) {
                this.endDate = null;
            }
        }
    }

    public void generatePayments(LocalDate simStartDate, LocalDate simEndDate, PaymentDao paymentDao) {
        LocalDate actualStartDate = simStartDate;
        LocalDate actualEndDate = simEndDate;

        if (simStartDate.isBefore(this.startDate)) actualStartDate = this.startDate;
        if (simEndDate.isAfter(this.endDate)) actualEndDate = this.endDate;
        if (actualEndDate.isBefore(actualStartDate)) actualEndDate = actualStartDate;

        List<LocalDate> dates = generateDateList(actualStartDate, actualEndDate);
        for (LocalDate date : dates)
        {
            Payment payment = new Payment();
            payment.setPaymentType(this);
            payment.setAccount(this.account);
            payment.setAmt(this.amt);
            payment.setDate(date);

            paymentDao.save(payment);
        }
    }

    private List<LocalDate> generateDateList(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();

        int i = 0;
        LocalDate current = start;
        while (current.isBefore(end) || current.isEqual(end)) {
            dates.add(current);

            switch (this.frequency) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate initialDate) {
        this.startDate = initialDate;
        checkEndDate();
    }

    public LocalDate getEndDate() {
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
