package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private CustomerDao customerDao;

    //Get coupon details by Coupon name
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

    //Get Coupon by uuid
    public CouponEntity getCouponByCouponId(final String couponUuid) throws CouponNotFoundException{
        CouponEntity fetchedCoupon = couponDao.getCouponByCouponId(couponUuid);

        if (fetchedCoupon == null){
            throw new CouponNotFoundException("CPF-002","No coupon by this id");
        }

        return fetchedCoupon;
    }

    //Save a new order
    public OrderEntity saveOrder(OrderEntity newOrder){
        return orderDao.saveOrder(newOrder);
    }

    //Save items in an order
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity){
        return orderItemDao.saveOrderItem(orderItemEntity);
    }

    //Get all past orders of customer by uuid
    //Sort the list based on date & time in desending order
    public List<OrderEntity> getOrdersByCustomers(final String customerUuid){

        CustomerEntity fetchedCustomer = customerDao. getUserByUuid(customerUuid);

        List<OrderEntity> customerOrders = orderDao.getCustomerOrders(fetchedCustomer);
        Collections.sort(customerOrders,OrderService.DateComparator);
        return customerOrders;
    }

    //Comparator to sort category
    public static Comparator<OrderEntity> DateComparator = (s1, s2) -> {
        Date date1
                = s1.getDate();
        Date date2
                = s2.getDate();

        // descending order (latest first)
        return date2.compareTo(
                date1);
    };

}
