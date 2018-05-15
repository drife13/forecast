package org.launchcode.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Entity
public class Prediction {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "End date must be after start date.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private String[] tableHeaders;

    private HashMap<LocalDate, double[]> balanceTable;

    public Prediction() {}

    private void checkEndDate() {
        if (startDate != null && endDate != null) {
            if (this.endDate.isBefore(this.startDate)) {
                this.endDate = null;
            }
        }
    }

    public int getId() {
        return id;
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

    public String[] getTableHeaders() {
        return tableHeaders;
    }

    public void setTableHeaders(String[] tableHeaders) {
        this.tableHeaders = tableHeaders;
    }

    public HashMap<LocalDate, double[]> getBalanceTable() {
        return balanceTable;
    }

    public void setBalanceTable(HashMap<LocalDate, double[]> balanceTable) {
        this.balanceTable = balanceTable;
    }
}
