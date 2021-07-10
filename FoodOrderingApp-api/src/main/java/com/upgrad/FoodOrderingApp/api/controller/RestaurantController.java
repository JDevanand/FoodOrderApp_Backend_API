package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    //Fetch all restaurants
    @CrossOrigin
    @RequestMapping(path = "/restaurant", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurant() {

        List<RestaurantEntity> restaurantEntities = restaurantService.getAllRestaurant();
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        return new ResponseEntity<>(restaurantListResponse, HttpStatus.OK);
    }

    //Fetch restaurant by UUID
    @CrossOrigin
    @RequestMapping(path = "/api/restaurant/{restaurant_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByUuid(@PathVariable("restaurant_id") final String restaurantUuid) {

        RestaurantEntity fetchedRestaurant = restaurantService.restaurantByUUID(restaurantUuid);

        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();

        return new ResponseEntity<>(restaurantDetailsResponse, HttpStatus.OK);
    }

}