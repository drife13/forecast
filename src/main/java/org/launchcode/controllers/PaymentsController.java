package org.launchcode.controllers;

import org.launchcode.models.Account;
import org.launchcode.models.Frequency;
import org.launchcode.models.PaymentType;
import org.launchcode.models.data.AccountDao;
import org.launchcode.models.data.PaymentTypeDao;
import org.launchcode.models.forms.AddPaymentTypeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        Iterable<Account> accounts = accountDao.findAll();

        AddPaymentTypeForm form = new AddPaymentTypeForm(accounts);

        model.addAttribute("title", "Add Recurring Payment");
        model.addAttribute("form", form);
        model.addAttribute("frequencies", Frequency.values());

        return "payments/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddPaymentTypeForm(Model model, @ModelAttribute @Valid AddPaymentTypeForm form, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("form", form);
            return "payments/add";
        }

        Account account = accountDao.findOne(form.getAccountId());
        PaymentType newPaymentType = new PaymentType(form, account);
        paymentTypeDao.save(newPaymentType);

        return "redirect:/home";
    }
}
