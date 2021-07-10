package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class OrderService {

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private CouponDao couponDao;

    public CouponEntity getCouponByCouponName (String accessToken, String couponName) throws AuthorizationFailedException, CouponNotFoundException {
        CustomerAuthEntity loggedUserAuthTokenEntity = customerAuthDao.getUserAuthToken(accessToken);
        if(loggedUserAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        if(loggedUserAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        if (loggedUserAuthTokenEntity.getExpiresAt().compareTo(ZonedDateTime.now()) > 0) {

            if(couponName == null){
                throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
            }

            CouponEntity fetchedCoupon = couponDao.getCouponByName(couponName);
            if(fetchedCoupon == null){
                throw new CouponNotFoundException("CPF-001","No coupon by this name");
            }

            return fetchedCoupon;

        } else {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
    }

}
