package org.launchcode.models.data;

import org.launchcode.models.Account;
import org.launchcode.models.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface PaymentDao extends CrudRepository<Payment, Integer> {
    List<Payment> findByAccountAndDate(Account account, LocalDate date);
}