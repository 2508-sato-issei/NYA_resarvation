package com.example.NYA_reservation.converter;

import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantConverter {

    public RestaurantForm toRestaurantForm(Restaurant restaurant) {
        RestaurantForm restaurantForm = new RestaurantForm();

            restaurantForm.setId(restaurant.getId());
            restaurantForm.setName(restaurant.getName());
            restaurantForm.setTelephone(restaurant.getTelephone());
            restaurantForm.setAddress(restaurant.getAddress());
            restaurantForm.setArea(restaurant.getArea());
            restaurantForm.setGenre(restaurant.getGenre());
            restaurantForm.setExplanation(restaurant.getExplanation());
            restaurantForm.setStartBusiness(restaurant.getStartBusiness());
            restaurantForm.setEndBusiness(restaurant.getEndBusiness());
            restaurantForm.setCapacity(restaurant.getCapacity());
            restaurantForm.setMinAmount(restaurant.getMinAmount());
            restaurantForm.setMaxAmount(restaurant.getMaxAmount());
            restaurantForm.setCreatedDate(restaurant.getCreatedDate());
            restaurantForm.setUpdatedDate(restaurant.getUpdatedDate());
            return restaurantForm;
    }

    //DBから取得したレストラン情報をEntityからFormに詰め替え
    public List<RestaurantForm> toRestaurantFormList(List<Restaurant> results) {
        List<RestaurantForm> restaurants = new ArrayList<>();

        for (Restaurant result : results) {
            RestaurantForm restaurantForm = new RestaurantForm();
            restaurantForm.setId(result.getId());
            restaurantForm.setName(result.getName());
            restaurantForm.setTelephone(result.getTelephone());
            restaurantForm.setAddress(result.getAddress());
            restaurantForm.setArea(result.getArea());
            restaurantForm.setGenre(result.getGenre());
            restaurantForm.setExplanation(result.getExplanation());
            restaurantForm.setStartBusiness(result.getStartBusiness());
            restaurantForm.setEndBusiness(result.getEndBusiness());
            restaurantForm.setCapacity(result.getCapacity());
            restaurantForm.setMinAmount(result.getMinAmount());
            restaurantForm.setMaxAmount(result.getMaxAmount());
            restaurantForm.setCreatedDate(result.getCreatedDate());
            restaurantForm.setUpdatedDate(result.getUpdatedDate());
            restaurants.add(restaurantForm);
        }
        return restaurants;
    }

    //Formの値をEntityに詰め替え
    public Restaurant toRestaurantEntity(RestaurantForm restaurantForm){
        Restaurant restaurant = new Restaurant();

        restaurant.setName(restaurantForm.getName());
        restaurant.setTelephone(restaurantForm.getTelephone());
        restaurant.setAddress(restaurantForm.getAddress());
        restaurant.setArea(restaurantForm.getArea());
        restaurant.setGenre(restaurantForm.getGenre());
        restaurant.setExplanation(restaurantForm.getExplanation());
        restaurant.setStartBusiness(restaurantForm.getStartBusiness());
        restaurant.setEndBusiness(restaurantForm.getEndBusiness());
        restaurant.setCapacity(restaurantForm.getCapacity());
        restaurant.setMinAmount(restaurantForm.getMinAmount());
        restaurant.setMaxAmount(restaurantForm.getMaxAmount());

        if(restaurantForm.getId() != null){
            restaurant.setId(restaurantForm.getId());
            restaurant.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return restaurant;
    }

}
