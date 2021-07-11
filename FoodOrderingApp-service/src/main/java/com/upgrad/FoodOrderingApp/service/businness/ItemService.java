package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private RestaurantItemDao restaurantItemDao;

    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity){
        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDao.getRestaurantItemsById(restaurantEntity);

        List<ItemEntity> itemEntityList = new ArrayList<>();

        /*
        //List of items
        for(RestaurantItemEntity items : restaurantItemEntities){
            if(catItem.getCategory().getId() == category.getId()){
                itemEntityList.add(catItem.getItem());
            }
        }
        */

        //sort descending order of items
        //send top 5 of sorted list
        return itemEntityList;
    }

}
