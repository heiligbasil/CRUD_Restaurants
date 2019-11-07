package com.lambdaschool.crudyrestaurants.services;

import com.lambdaschool.crudyrestaurants.models.Payment;
import com.lambdaschool.crudyrestaurants.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;

@Transactional
@Service(value = "paymentService")
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentrepos;

    @Override
    public Payment findPaymentById(long id) {
        return paymentrepos.findById(id).orElseThrow(() -> new EntityNotFoundException("Payment " + id + " not found"));
    }

    @Transactional
    @Override
    public Payment save(Payment payment) {
        // restaurants not added through payments
        if (payment.getRestaurants().size() > 0) {
            throw new EntityNotFoundException("Restaurants not added through payments");
        }

        Payment newPayment = new Payment();
        newPayment.setRestaurants(new ArrayList<>());
        newPayment.setType(payment.getType());

        return paymentrepos.save(newPayment);
    }
}
