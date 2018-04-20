package org.launchcode.models.data;

import org.launchcode.models.PaymentType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PaymentTypeDao extends CrudRepository<PaymentType, Integer> {
}