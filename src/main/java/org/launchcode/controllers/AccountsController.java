package org.launchcode.controllers;

import org.launchcode.models.Account;
import org.launchcode.models.data.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        return "redirect:/home";
    }
}
