package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class ItemController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ItemService itemService;
    //Get the items of a restaurant by restaurant uuid
    @CrossOrigin
    @RequestMapping(path="/item/restaurant/{restaurant_id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getCategoryByUuid(@PathVariable("restaurant_id")final String restaurantUuid) throws RestaurantNotFoundException {

        RestaurantEntity fetchedRestaurant = restaurantService.restaurantByUUID(restaurantUuid);
        List<ItemEntity> topFiveItems = itemService.getItemsByPopularity(fetchedRestaurant);

        List<ItemList> itemLists = new ArrayList<ItemList>();
        for(ItemEntity items: topFiveItems){
            ItemList itmlist = new ItemList();
            itmlist.setId(UUID.fromString(items.getUuid()));
            itmlist.setItemName(items.getItemName());
            itmlist.setPrice(items.getPrice());
            for(ItemList.ItemTypeEnum itmtype : ItemList.ItemTypeEnum.values()) {
                if (items.getType().toString().equals(itmtype.toString())) itmlist.setItemType(itmtype);
            }
            itemLists.add(itmlist);
        }

        ItemListResponse itemListResponse = new ItemListResponse() ;
        itemListResponse.addAll(itemLists);

        return new ResponseEntity<>(itemListResponse, HttpStatus.OK);
    }
}
