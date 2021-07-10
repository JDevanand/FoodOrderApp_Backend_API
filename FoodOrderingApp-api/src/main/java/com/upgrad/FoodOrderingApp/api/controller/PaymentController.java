package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.api.model.StatesList;
import com.upgrad.FoodOrderingApp.api.model.StatesListResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @CrossOrigin
    @RequestMapping(path="/payment",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentListResponse> getAllPaymentMethods(){

        List<PaymentEntity> paymentEntities = paymentService.getAllPaymentMethods();
        List<PaymentResponse> paymentResponses = new ArrayList<>();

        for(PaymentEntity paymentEntity : paymentEntities ){
            PaymentResponse payment = new PaymentResponse();
            payment.setId(UUID.fromString(paymentEntity.getUuid()));
            payment.setPaymentName(paymentEntity.getPaymentName());
            paymentResponses.add(payment);
        }

        PaymentListResponse paymentListResponse = new PaymentListResponse();
        return new ResponseEntity<>(paymentListResponse.paymentMethods(paymentResponses), HttpStatus.OK);
    }
}
