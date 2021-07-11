package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    //Fetch all restaurants
    @CrossOrigin
    @RequestMapping(path = "/restaurant", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByRating() {

        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByRating();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        List<RestaurantList> restaurantLists = new ArrayList<>();

        for (RestaurantEntity restaurant : restaurantEntities) {
            RestaurantList restlist = new RestaurantList();
            restlist.setId(UUID.fromString(restaurant.getUuid()));
            restlist.setRestaurantName(restaurant.getRestaurantName());
            restlist.setPhotoURL(restaurant.getPhotoUrl());
            restlist.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));
            restlist.setAveragePrice(restaurant.getAvgPrice());
            restlist.setNumberCustomersRated(restaurant.getNumberCustomersRated());

            RestaurantDetailsResponseAddress restaurantAddress = new RestaurantDetailsResponseAddress();
            restaurantAddress.setId(UUID.fromString(restaurant.getAddress().getUuid()));
            restaurantAddress.setFlatBuildingName(restaurant.getAddress().getFlatBuildingNumber());
            restaurantAddress.setLocality(restaurant.getAddress().getLocality());
            restaurantAddress.setCity(restaurant.getAddress().getCity());
            restaurantAddress.setPincode(restaurant.getAddress().getPincode());

            RestaurantDetailsResponseAddressState restAddState = new RestaurantDetailsResponseAddressState();
            restAddState.setId(UUID.fromString(restaurant.getAddress().getState().getUuid()));
            restAddState.setStateName(restaurant.getAddress().getState().getStateName());

            restaurantAddress.setState(restAddState);
            restlist.setAddress(restaurantAddress);

            List<CategoryEntity> restaurantCategory = categoryService.getCategoriesByRestaurant(restaurant.getUuid());
            StringBuilder categoryList = new StringBuilder();
            Iterator<CategoryEntity> itr = restaurantCategory.iterator();
            while (itr.hasNext()) {
                categoryList.append(itr.next().getCategoryName());
                if (itr.hasNext()) {
                    categoryList.append(",");
                }
            }

            restlist.setCategories(categoryList.toString());
            restaurantLists.add(restlist);
        }

        restaurantListResponse.restaurants(restaurantLists);

        return new ResponseEntity<>(restaurantListResponse, HttpStatus.OK);
    }

    //Fetch restaurant by UUID
    @CrossOrigin
    @RequestMapping(path = "/api/restaurant/{restaurant_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByUuid(@PathVariable("restaurant_id") final String restaurantUuid) throws RestaurantNotFoundException {

        RestaurantEntity fetchedRestaurant = restaurantService.restaurantByUUID(restaurantUuid);

        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();

        return new ResponseEntity<>(restaurantDetailsResponse, HttpStatus.OK);
    }

    //update restaurant rating given by customer
    @CrossOrigin
    @RequestMapping(path = "/api/restaurant/{restaurant_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantRating(@PathVariable("restaurant_id") final String restaurantUuid
            , @RequestHeader("authorization ") final String authorization, @RequestParam("customer_rating") final double customerRating) throws RestaurantNotFoundException, AuthorizationFailedException, InvalidRatingException {


        CustomerEntity loggedCustomer = customerService.getCustomer(authorization);
        RestaurantEntity fetchedRestaurant = restaurantService.restaurantByUUID(restaurantUuid);

        RestaurantEntity updatedRestaurant = restaurantService.updateRestaurantRating(fetchedRestaurant, customerRating);

        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse();
        restaurantUpdatedResponse.setId(UUID.fromString(updatedRestaurant.getUuid()));
        restaurantUpdatedResponse.setStatus("RESTAURANT RATING UPDATED SUCCESSFULLY");

        return new ResponseEntity<>(restaurantUpdatedResponse, HttpStatus.OK);
    }


    //Fetch restaurant by category ID
    @CrossOrigin
    @RequestMapping(path = "/restaurant/category/{category_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategoryId(@PathVariable("category_id") final String categoryId) {

        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByRating();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        List<RestaurantList> restaurantLists = new ArrayList<>();

        for (RestaurantEntity restaurant : restaurantEntities) {
            RestaurantList restlist = new RestaurantList();
            restlist.setId(UUID.fromString(restaurant.getUuid()));
            restlist.setRestaurantName(restaurant.getRestaurantName());
            restlist.setPhotoURL(restaurant.getPhotoUrl());
            restlist.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));
            restlist.setAveragePrice(restaurant.getAvgPrice());
            restlist.setNumberCustomersRated(restaurant.getNumberCustomersRated());

            RestaurantDetailsResponseAddress restaurantAddress = new RestaurantDetailsResponseAddress();
            restaurantAddress.setId(UUID.fromString(restaurant.getAddress().getUuid()));
            restaurantAddress.setFlatBuildingName(restaurant.getAddress().getFlatBuildingNumber());
            restaurantAddress.setLocality(restaurant.getAddress().getLocality());
            restaurantAddress.setCity(restaurant.getAddress().getCity());
            restaurantAddress.setPincode(restaurant.getAddress().getPincode());

            RestaurantDetailsResponseAddressState restAddState = new RestaurantDetailsResponseAddressState();
            restAddState.setId(UUID.fromString(restaurant.getAddress().getState().getUuid()));
            restAddState.setStateName(restaurant.getAddress().getState().getStateName());

            restaurantAddress.setState(restAddState);
            restlist.setAddress(restaurantAddress);

            List<CategoryEntity> restaurantCategory = categoryService.getCategoriesByRestaurant(restaurant.getUuid());
            StringBuilder categoryList = new StringBuilder();
            Iterator<CategoryEntity> itr = restaurantCategory.iterator();
            while (itr.hasNext()) {
                categoryList.append(itr.next().getCategoryName());
                if (itr.hasNext()) {
                    categoryList.append(",");
                }
            }

            restlist.setCategories(categoryList.toString());
            restaurantLists.add(restlist);
        }

        restaurantListResponse.restaurants(restaurantLists);

        return new ResponseEntity<>(restaurantListResponse, HttpStatus.OK);
    }

    //Fetch restaurant by name
    @CrossOrigin
    @RequestMapping(path = "/restaurant/name/{reastaurant_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantByName(@PathVariable("reastaurant_name") final String searchName) throws RestaurantNotFoundException {

        List<RestaurantEntity> restaurantEntities = restaurantService.restaurantsByName(searchName);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        List<RestaurantList> restaurantLists = new ArrayList<>();

        for (RestaurantEntity restaurant : restaurantEntities) {
            RestaurantList restlist = new RestaurantList();
            restlist.setId(UUID.fromString(restaurant.getUuid()));
            restlist.setRestaurantName(restaurant.getRestaurantName());
            restlist.setPhotoURL(restaurant.getPhotoUrl());
            restlist.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));
            restlist.setAveragePrice(restaurant.getAvgPrice());
            restlist.setNumberCustomersRated(restaurant.getNumberCustomersRated());

            RestaurantDetailsResponseAddress restaurantAddress = new RestaurantDetailsResponseAddress();
            restaurantAddress.setId(UUID.fromString(restaurant.getAddress().getUuid()));
            restaurantAddress.setFlatBuildingName(restaurant.getAddress().getFlatBuildingNumber());
            restaurantAddress.setLocality(restaurant.getAddress().getLocality());
            restaurantAddress.setCity(restaurant.getAddress().getCity());
            restaurantAddress.setPincode(restaurant.getAddress().getPincode());

            RestaurantDetailsResponseAddressState restAddState = new RestaurantDetailsResponseAddressState();
            restAddState.setId(UUID.fromString(restaurant.getAddress().getState().getUuid()));
            restAddState.setStateName(restaurant.getAddress().getState().getStateName());

            restaurantAddress.setState(restAddState);
            restlist.setAddress(restaurantAddress);

            List<CategoryEntity> restaurantCategory = categoryService.getCategoriesByRestaurant(restaurant.getUuid());
            StringBuilder categoryList = new StringBuilder();
            Iterator<CategoryEntity> itr = restaurantCategory.iterator();
            while (itr.hasNext()) {
                categoryList.append(itr.next().getCategoryName());
                if (itr.hasNext()) {
                    categoryList.append(",");
                }
            }

            restlist.setCategories(categoryList.toString());
            restaurantLists.add(restlist);
        }

        restaurantListResponse.restaurants(restaurantLists);

        return new ResponseEntity<>(restaurantListResponse, HttpStatus.OK);
    }
}