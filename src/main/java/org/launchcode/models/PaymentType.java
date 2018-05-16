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
        setName(form.getName());
        setAmt(form.getAmt());
        setStartDate(form.getStartDate());
        setEndDate(form.getEndDate());
        setFrequency(form.getFrequency());
        setAccount(account);

        if (this.frequency.equals(Frequency.SINGLE)) {
            setEndDate(form.getStartDate());
        }
    }

    private void checkEndDate() {
        if (startDate != null && endDate != null) {
            if (this.endDate.isBefore(this.startDate)) {
                this.endDate = null;
            }
        }
    }

    public void generatePayments(LocalDate simStartDate, LocalDate simEndDate, PaymentDao paymentDao) {
        LocalDate actualStartDate = this.startDate;
        int i = 0;
        while (actualStartDate.isBefore(simStartDate)) {
            actualStartDate = plusFreq(this.startDate, ++i);
        }

        LocalDate actualEndDate = this.endDate;
        if (actualEndDate.isAfter(simEndDate)) actualEndDate = simEndDate;

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

        LocalDate current = start;
        int i = 0;
        while (current.isBefore(end) || current.isEqual(end)) {
            dates.add(current);
            current = plusFreq(start, ++i);
        }
        return dates;
    }

    private LocalDate plusFreq(LocalDate date, int i) {
        switch (this.frequency) {
            case SINGLE:
            case DAILY:
                return date.plusDays(i);
            case WEEKLY:
                return date.plusWeeks(i);
            case BIWEEKLY:
                return date.plusWeeks(2 * i);
            case MONTHLY:
                return date.plusMonths(i);
            case ANNUALLY:
                return date.plusYears(i);
            default:
                return date;
        }
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

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
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
