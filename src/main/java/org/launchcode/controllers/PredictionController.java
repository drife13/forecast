package org.launchcode.controllers;

import org.apache.tomcat.jni.Local;
import org.launchcode.models.*;
import org.launchcode.models.data.*;
import org.launchcode.models.forms.EditAccountForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("prediction")
public class PredictionController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    BalanceDao balanceDao;

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    PaymentTypeDao paymentTypeDao;

    @Autowired
    PredictionDao predictionDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewPreviousPrediction(Model model) {
        return "redirect:/home";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String viewNewPrediction(Model model, @ModelAttribute @Valid Prediction prediction, Errors errors) {
        LocalDate simStartDate = prediction.getStartDate();
        LocalDate simEndDate = prediction.getEndDate();

        paymentDao.deleteAll();
        balanceDao.deleteAll();

        for (PaymentType paymentType : paymentTypeDao.findAll()) {
            paymentType.generatePayments(simStartDate, simEndDate, paymentDao);
        }

        for (Account account : accountDao.findAll()) {
            LocalDate simDate = simStartDate;

            Balance currentBalance = new Balance();
            currentBalance.setAccount(account);
            currentBalance.setDate(simDate);
            currentBalance.setAmt(account.getInitialAmt());
            balanceDao.save(currentBalance);

            List<Payment> payments = paymentDao.findByAccountAndDate(account, simDate);
            currentBalance.pointPaymentsToThisBalance(payments, paymentDao);
            balanceDao.save(currentBalance);

            while (simDate.isBefore(simEndDate)) {
                Balance prevBalance = currentBalance;
                currentBalance = new Balance(prevBalance);
                balanceDao.save(currentBalance);

                simDate = currentBalance.getDate();

                payments = paymentDao.findByAccountAndDate(account, simDate);
                currentBalance.pointPaymentsToThisBalance(payments, paymentDao);
                balanceDao.save(currentBalance);
            }
        }

        LocalDate simDate = simStartDate;
        List<LocalDate> dates = new ArrayList<>();
        while (simDate.isBefore(simEndDate) || simDate.equals(simEndDate)) {
            dates.add(simDate);
            simDate = simDate.plusDays(1);
        }

        int numAccounts = (int) accountDao.count();
        HashMap<LocalDate, BigDecimal[]> balanceTable = new HashMap<>();

        for (LocalDate date : dates) {
            BigDecimal[] balanceRow = new BigDecimal[numAccounts + 1];

            int j = 0;
            for (Account account : accountDao.findAll()) {
                List<Balance> balance = balanceDao.findByAccountAndDate(account, date);
                balanceRow[j] = balance.get(0).getAmt();
                balanceRow[numAccounts] = balanceRow[numAccounts].add(balanceRow[j]);
                j++;
            }

            balanceTable.put(date, balanceRow);
        }

        String[] tableHeaders = new String[1 + numAccounts + 1];
        tableHeaders[0] = "Date";
        int i = 1;
        for (Account account : accountDao.findAll()) {
            tableHeaders[i++] = account.getName();
        }
        tableHeaders[i] = "Net Total";

        model.addAttribute("title", "Prediction for dates "
                + simStartDate.toString() + " to " + simEndDate.toString());
        model.addAttribute("headers", tableHeaders);
        model.addAttribute("table", balanceTable.entrySet());

        prediction.setTableHeaders(tableHeaders);
        prediction.setBalanceTable(balanceTable);
        predictionDao.save(prediction);

        return "prediction/view";
    }

}
