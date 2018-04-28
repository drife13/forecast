package org.launchcode.models.forms;

import org.launchcode.models.PaymentType;

import javax.validation.constraints.NotNull;

public class EditPaymentTypeForm extends AddPaymentTypeForm {
    @NotNull
    private int paymentTypeId;

    public EditPaymentTypeForm () {}

    public EditPaymentTypeForm(PaymentType paymentType) {
        this.setPaymentTypeId(paymentType.getId());
        this.setName(paymentType.getName());
        this.setAmt(paymentType.getAmt());
        this.setStartDate(paymentType.getStartDate());
        this.setEndDate(paymentType.getEndDate());
        this.setFrequency(paymentType.getFrequency());
        this.setAccountId(paymentType.getAccount().getId());
    }

    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }
}
