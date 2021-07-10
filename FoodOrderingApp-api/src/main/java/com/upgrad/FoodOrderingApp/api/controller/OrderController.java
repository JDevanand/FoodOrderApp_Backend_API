package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @CrossOrigin
    @RequestMapping(path="/order/coupon/{coupon_name}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(@RequestHeader("authorization") final String accessToken, @RequestParam("coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        CouponEntity fetchedCoupon = orderService.getCouponByCouponName(accessToken, couponName);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();

        couponDetailsResponse.setId(UUID.fromString(fetchedCoupon.getUuid()));
        couponDetailsResponse.setCouponName(fetchedCoupon.getCouponName());
        couponDetailsResponse.setPercent(fetchedCoupon.getPercent());

        return new ResponseEntity<>(couponDetailsResponse, HttpStatus.OK);
    }

}
