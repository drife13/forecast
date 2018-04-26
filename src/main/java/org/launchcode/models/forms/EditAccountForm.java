package org.launchcode.models.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class EditAccountForm {
    @NotNull
    @Size(min = 1, max = 15)
    private String name;

    @NotNull
    private BigDecimal initialAmt;

    @NotNull
    private int accountId;

    public EditAccountForm() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInitialAmt() {
        return initialAmt;
    }

    public void setInitialAmt(BigDecimal initialAmt) {
        this.initialAmt = initialAmt;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
