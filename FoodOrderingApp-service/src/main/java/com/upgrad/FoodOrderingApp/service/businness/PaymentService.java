package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    //Get all payment modes
    public List<PaymentEntity> getAllPaymentMethods(){
        return paymentDao.getPaymentModes();
    }

    //Get payment by uuid
    public PaymentEntity getPaymentByUUID(final String paymentUuid) throws PaymentMethodNotFoundException {

        PaymentEntity fetchedPayment= paymentDao.getPaymentByUuid(paymentUuid);
        if(fetchedPayment==null){
            throw new PaymentMethodNotFoundException("PNF-002","No payment method found by this id");
        }
        return fetchedPayment;

    }

}
