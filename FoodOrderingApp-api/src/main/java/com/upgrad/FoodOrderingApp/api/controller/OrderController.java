package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ItemService itemService;

    //Get Coupon Details
    @CrossOrigin
    @RequestMapping(path = "/order/coupon/{coupon_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(@RequestHeader("authorization") final String authorization, @PathVariable("coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        String[] splitStrings = authorization.split(" ");
        String accessToken = splitStrings[1];

        CustomerEntity loggedCustomer = customerService.getCustomer(accessToken);
        CouponEntity fetchedCoupon = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();

        couponDetailsResponse.setId(UUID.fromString(fetchedCoupon.getUuid()));
        couponDetailsResponse.setCouponName(fetchedCoupon.getCouponName());
        couponDetailsResponse.setPercent(fetchedCoupon.getPercent());

        return new ResponseEntity<>(couponDetailsResponse, HttpStatus.OK);
    }

    //Save new order
    @CrossOrigin
    @RequestMapping(path = "/order", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveNewOrder(@RequestHeader("authorization") final String authorization, @RequestBody(required = false) final SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, CouponNotFoundException, RestaurantNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, ItemNotFoundException {


        String[] splitStrings = authorization.split(" ");
        String accessToken = splitStrings[1];

        CustomerEntity loggedCustomer = customerService.getCustomer(accessToken);
        CouponEntity coupon = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());
        PaymentEntity payment = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());
        AddressEntity address = addressService.getAddressByUUID(saveOrderRequest.getAddressId(),loggedCustomer);
        RestaurantEntity restaurant = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());

        OrderEntity newOrder = new OrderEntity();
        newOrder.setUuid(UUID.randomUUID().toString());
        newOrder.setBill(saveOrderRequest.getBill());
        newOrder.setDate(new Date());
        if (saveOrderRequest.getDiscount() != null) {
            newOrder.setDiscount(saveOrderRequest.getDiscount());
        }

        newOrder.setCoupon(coupon);

        newOrder.setCustomer(loggedCustomer);
        newOrder.setAddress(address);
        newOrder.setPayment(payment);
        newOrder.setRestaurant(restaurant);

        OrderEntity savedOrder = orderService.saveOrder(newOrder);

        List<ItemQuantity> itemQuantityFromRequest = saveOrderRequest.getItemQuantities();

        for (ItemQuantity itemQuantity : itemQuantityFromRequest) {
            OrderItemEntity oiEntity = new OrderItemEntity();

            ItemEntity item = itemService.getItemByUuid(itemQuantity.getItemId().toString());
            oiEntity.setItem(item);
            oiEntity.setOrder(savedOrder);
            oiEntity.setQuantity(itemQuantity.getQuantity());
            oiEntity.setPrice(itemQuantity.getPrice());

            orderService.saveOrderItem(oiEntity);
        }

        SaveOrderResponse savedOrderResponse = new SaveOrderResponse();
        savedOrderResponse.setId(savedOrder.getUuid());
        savedOrderResponse.setStatus("ORDER SUCCESSFULLY PLACED");

        return new ResponseEntity<>(savedOrderResponse, HttpStatus.CREATED);
    }

    //Get orders of customers
    @CrossOrigin
    @RequestMapping(path = "/order/customer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getOrdersByCustomers(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {


        String[] splitStrings = authorization.split(" ");
        String accessToken = splitStrings[1];

        CustomerEntity loggedCustomer = customerService.getCustomer(accessToken);
        List<OrderEntity> customerOrders = orderService.getOrdersByCustomers(loggedCustomer.getUuid());

        List<OrderList> customerOrderList = new ArrayList<>();
        for(OrderEntity eachOrderEntity: customerOrders){
            OrderList fetchedOrderList = new OrderList();
            fetchedOrderList.setId(UUID.fromString(eachOrderEntity.getUuid()));
            //fetchedOrderList.setBill(eachOrderEntity.getBill()); // <<big  decimal to number pending>>

            OrderListCoupon customerOrderListCouopon = new OrderListCoupon();
            customerOrderListCouopon.setId(UUID.fromString(eachOrderEntity.getCoupon().getUuid()));
            customerOrderListCouopon.setCouponName(eachOrderEntity.getCoupon().getCouponName());
            customerOrderListCouopon.setPercent(eachOrderEntity.getCoupon().getPercent());
            fetchedOrderList.setCoupon(customerOrderListCouopon);

            //fetchedOrderList.setDiscount(eachOrderEntity.getDiscount());// <big  decimal to number pending>>
            fetchedOrderList.setDate(eachOrderEntity.getDate().toString()); //<<<check output>>>//

            OrderListPayment customerOLPayment = new OrderListPayment();
            customerOLPayment.setId(UUID.fromString(eachOrderEntity.getPayment().getUuid()));
            customerOLPayment.setPaymentName(eachOrderEntity.getPayment().getPaymentName());
            fetchedOrderList.setPayment(customerOLPayment);

            OrderListCustomer customerOLCustomer = new OrderListCustomer();
            customerOLCustomer.setId(UUID.fromString(eachOrderEntity.getCustomer().getUuid()));
            customerOLCustomer.setFirstName(eachOrderEntity.getCustomer().getFirstName());
            customerOLCustomer.setLastName(eachOrderEntity.getCustomer().getLastName());
            customerOLCustomer.setContactNumber(eachOrderEntity.getCustomer().getContactNumber());
            customerOLCustomer.setEmailAddress(eachOrderEntity.getCustomer().getEmail());
            fetchedOrderList.setCustomer(customerOLCustomer);

            OrderListAddress customerOLAddress = new OrderListAddress();
            customerOLAddress.setId(UUID.fromString(eachOrderEntity.getAddress().getUuid()));
            customerOLAddress.setFlatBuildingName(eachOrderEntity.getAddress().getFlatBuilNo());
            customerOLAddress.setLocality(eachOrderEntity.getAddress().getLocality());
            customerOLAddress.setCity(eachOrderEntity.getAddress().getCity());
            customerOLAddress.setPincode(eachOrderEntity.getAddress().getPincode());

            OrderListAddressState custOLAState = new OrderListAddressState();
            custOLAState.setId(UUID.fromString(eachOrderEntity.getAddress().getState().getUuid()));
            custOLAState.setStateName(eachOrderEntity.getAddress().getState().getStateName());
            customerOLAddress.setState(custOLAState);
            fetchedOrderList.setAddress(customerOLAddress);


            //List of ItemQuantityResponse
            List<ItemQuantityResponse> customerItemQuantity = new ArrayList<>();
            for(OrderItemEntity orderItemEntity: eachOrderEntity.getOrderItemEntities()){
                ItemQuantityResponse iqr = new ItemQuantityResponse();

                ItemQuantityResponseItem iqrt = new ItemQuantityResponseItem();
                iqrt.setId(UUID.fromString(orderItemEntity.getItem().getUuid()));
                iqrt.setItemName(orderItemEntity.getItem().getItemName());
                iqrt.setItemPrice(orderItemEntity.getItem().getPrice());
                //iqrt.setType(orderItemEntity.getItem().getType()); // <<fetch enum or convert string to enum>>
                iqr.setItem(iqrt);

                iqr.setPrice(orderItemEntity.getPrice());
                iqr.setQuantity(orderItemEntity.getQuantity());
                customerItemQuantity.add(iqr);
            }
            fetchedOrderList.setItemQuantities(customerItemQuantity);

            customerOrderList.add(fetchedOrderList);
        }

        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
        customerOrderResponse.setOrders(customerOrderList);
        return new ResponseEntity<>(customerOrderResponse, HttpStatus.OK);
    }

}