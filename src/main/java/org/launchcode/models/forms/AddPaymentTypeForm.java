package org.launchcode.models.forms;

import org.launchcode.models.Frequency;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AddPaymentTypeForm {

    @NotNull
    private int accountId;

    @NotNull
    @Size(min = 1, max = 15)
    private String name;

    @NotNull
    private BigDecimal amt;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "End date must be after start date.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull
    private Frequency frequency;

    public AddPaymentTypeForm() {}

    private void checkEndDate() {
        if (this.endDate.isBefore(this.startDate)) {
            this.endDate = null;
        }
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal initialAmt) {
        this.amt = initialAmt;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        //checkEndDate();
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        //checkEndDate();
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }
}
