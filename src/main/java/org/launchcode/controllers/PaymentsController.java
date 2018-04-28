package org.launchcode.controllers;

import org.launchcode.models.Account;
import org.launchcode.models.Frequency;
import org.launchcode.models.PaymentType;
import org.launchcode.models.data.AccountDao;
import org.launchcode.models.data.PaymentTypeDao;
import org.launchcode.models.forms.AddPaymentTypeForm;
import org.launchcode.models.forms.EditAccountForm;
import org.launchcode.models.forms.EditPaymentTypeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("payments")
public class PaymentsController {

    @Autowired
    PaymentTypeDao paymentTypeDao;

    @Autowired
    AccountDao accountDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        return "redirect:/forecast";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddPaymentTypeForm(Model model) {

        model.addAttribute("title", "Add Recurring Payment");
        model.addAttribute("form", new AddPaymentTypeForm());
        model.addAttribute("frequencies", Frequency.values());
        model.addAttribute("accounts", accountDao.findAll());

        return "payments/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddPaymentTypeForm(Model model, @ModelAttribute @Valid AddPaymentTypeForm form, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Recurring Payment");
            model.addAttribute("form", form);
            model.addAttribute("accounts", accountDao.findAll());
            model.addAttribute("frequencies", Frequency.values());

            return "payments/add";
        }

        PaymentType newPaymentType = new PaymentType();
        newPaymentType.ProcessForm(form, accountDao.findOne(form.getAccountId()));
        paymentTypeDao.save(newPaymentType);

        return "redirect:/home";
    }

    @RequestMapping(value = "edit/{paymentTypeId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int paymentTypeId){
        PaymentType thePaymentType = paymentTypeDao.findOne(paymentTypeId);

        model.addAttribute("title", "Edit "
                + thePaymentType.getName() + " Payment (ID=" + paymentTypeId +")");
        model.addAttribute("form", new EditPaymentTypeForm(thePaymentType));
        model.addAttribute("accounts", accountDao.findAll());
        model.addAttribute("frequencies", Frequency.values());

        return "payments/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(Model model, @ModelAttribute @Valid EditPaymentTypeForm form, Errors errors) {
        PaymentType thePaymentType = paymentTypeDao.findOne(form.getPaymentTypeId());

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit "
                    + thePaymentType.getName() + " Payment (ID=" + form.getPaymentTypeId() +")");
            model.addAttribute("form", form);
            model.addAttribute("accounts", accountDao.findAll());
            model.addAttribute("frequencies", Frequency.values());

            return "payments/edit";
        }

        thePaymentType.ProcessForm(form, accountDao.findOne(form.getAccountId()));
        paymentTypeDao.save(thePaymentType);

        return "redirect:/home";
    }
}
