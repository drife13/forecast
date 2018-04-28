package org.launchcode.controllers;

import org.launchcode.models.Account;
import org.launchcode.models.data.AccountDao;
import org.launchcode.models.forms.EditAccountForm;
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
@RequestMapping("accounts")
public class AccountsController {

    @Autowired
    AccountDao accountDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        return "redirect:/forecast";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddAccountForm(Model model) {
        model.addAttribute("title", "Add Account");
        model.addAttribute(new Account());

        return "accounts/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddAccountForm(@ModelAttribute @Valid Account newAccount,
                                        Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Account");
            return "accounts/add";
        }

        accountDao.save(newAccount);

        return "redirect:/home";
    }

    @RequestMapping(value = "edit/{accountId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int accountId){
        Account theAccount = accountDao.findOne(accountId);

        model.addAttribute("form", new EditAccountForm(theAccount));
        model.addAttribute("title", "Edit "
                            + theAccount.getName() + " Account (ID=" + accountId +")");

        return "accounts/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(Model model, @ModelAttribute @Valid EditAccountForm form, Errors errors) {

        Account theAccount = accountDao.findOne(form.getAccountId());

        if (errors.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("title", "Edit"
                            + theAccount.getName() + " Account (ID=" + form.getAccountId() +")");

            return "accounts/edit";
        }

        theAccount.setName(form.getName());
        theAccount.setInitialAmt(form.getInitialAmt());

        accountDao.save(theAccount);

        return "redirect:/home";
    }
}
