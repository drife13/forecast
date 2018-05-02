package org.launchcode.controllers;

import org.launchcode.models.Prediction;
import org.launchcode.models.data.AccountDao;
import org.launchcode.models.data.PaymentTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
public class HomeController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    PaymentTypeDao paymentTypeDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "foreca$t");
        model.addAttribute("accounts", accountDao.findAll());
        model.addAttribute("paymentTypes", paymentTypeDao.findAll());
        model.addAttribute("prediction", new Prediction());

        return "home/index";
    }
}
