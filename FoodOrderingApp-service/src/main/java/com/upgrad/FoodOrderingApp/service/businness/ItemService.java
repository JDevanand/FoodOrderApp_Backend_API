package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantItemDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private RestaurantItemDao restaurantItemDao;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;


    //getItemsby Category and restaurant
    public List<ItemEntity> getItemsByCategoryAndRestaurant(final String restaurantUuid, final String categoryUuid) throws RestaurantNotFoundException, CategoryNotFoundException {

        RestaurantEntity currentRestaurant = restaurantService.restaurantByUUID(restaurantUuid);
        CategoryEntity currentCategory = categoryService.getCategoryById(categoryUuid);

        return itemDao.getItemsByCategoryAndRestaurant(currentRestaurant,currentCategory);

    }

    //Get Item by uuid
    public ItemEntity getItemByUuid(final String itemUuid) throws ItemNotFoundException {
        ItemEntity fetchedItem =  itemDao.getItembyUuid(itemUuid);
        if(fetchedItem==null){
            throw new ItemNotFoundException("INF-003","No item by this id exist");
        }
        return fetchedItem;
    }

    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity){

        // List of orders by restaurant
        List<OrderEntity> fetchedOrderEntity = orderDao.getRestaurantOrders(restaurantEntity);
        // List of order-item entities from the orders
        List fetchedItems = orderItemDao.getOrderItemByOrderId(fetchedOrderEntity);
        //Get list of items from the orderitementity list & its quantity
        List<ItemEntity> itemEntities = new ArrayList<>();

        return itemEntities;
    }
}
