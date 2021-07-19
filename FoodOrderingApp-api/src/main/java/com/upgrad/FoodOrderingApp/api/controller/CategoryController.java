
package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
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
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Get all category
    @CrossOrigin
    @RequestMapping(path="/category",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories(){

        List<CategoryEntity> categoryEntities = categoryService.getAllCategoriesOrderedByName();
        List<CategoryListResponse> categoryListResponses = new ArrayList<>();
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();

        if(categoryEntities == null){
            categoriesListResponse.setCategories(null);
            return new ResponseEntity<>(categoriesListResponse, HttpStatus.OK);
        }


        for(CategoryEntity category: categoryEntities){
            CategoryListResponse clr = new CategoryListResponse();
            clr.setId(UUID.fromString(category.getUuid()));
            clr.setCategoryName(category.getCategoryName());
            categoryListResponses.add(clr);
        }


        return new ResponseEntity<>(categoriesListResponse.categories(categoryListResponses), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path="/category/{category_id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryByUuid(@PathVariable("category_id")final String categoryUuid) throws CategoryNotFoundException {

        CategoryEntity fetchedCategory = categoryService.getCategoryById(categoryUuid);
        CategoryDetailsResponse categoriesDetailsResponse = new CategoryDetailsResponse();
        categoriesDetailsResponse.setId(UUID.fromString(fetchedCategory.getUuid()));
        categoriesDetailsResponse.setCategoryName(fetchedCategory.getCategoryName());

        List<ItemEntity> itemsTaggedToCategory = categoryService.getItemsbyCategoryId(fetchedCategory);
        if(itemsTaggedToCategory == null){
            categoriesDetailsResponse.setItemList(null);
            return new ResponseEntity<>(categoriesDetailsResponse, HttpStatus.OK);
        }

        List<ItemList> itemLists = new ArrayList<>();
        for(ItemEntity items: itemsTaggedToCategory){
            ItemList itm = new ItemList();
            itm.setId(UUID.fromString(items.getUuid()));
            itm.setItemName(items.getItemName());

            for(ItemList.ItemTypeEnum itmtype : ItemList.ItemTypeEnum.values()) {
                if (items.getType().toString().equals(itmtype.toString())) itm.setItemType(itmtype);
            }

            itm.setPrice(items.getPrice());
            itemLists.add(itm);
        }
        categoriesDetailsResponse.setItemList(itemLists);

        return new ResponseEntity<>(categoriesDetailsResponse, HttpStatus.OK);
    }

}