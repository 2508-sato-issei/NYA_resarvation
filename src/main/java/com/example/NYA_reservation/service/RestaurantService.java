package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.RestaurantForm;
import com.example.NYA_reservation.controller.form.SearchForm;
import com.example.NYA_reservation.dto.AreaReservationCountDto;
import com.example.NYA_reservation.dto.GenreReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantSpecifications;
import com.example.NYA_reservation.repository.RestaurantRepository;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    // 検索結果取得
    public Page<Restaurant> searchRestaurants(SearchForm searchForm, Pageable pageable) {
        String area = searchForm.getArea();
        String genre = searchForm.getGenre();
        Integer headcount = searchForm.getHeadcount();

        Specification<Restaurant> spec = RestaurantSpecifications.searchByCriteria(area, genre, headcount);
        return restaurantRepository.findAll(spec, pageable);
    }

    // レストラン情報を取得（店舗詳細画面用）
    public Restaurant findById(Integer id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    //IDでレストラン情報を取得(予約用)
    public RestaurantForm findRestaurantById(Integer id) {
        Restaurant result = restaurantRepository.findById(id).orElse(null);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(result);
        List<RestaurantForm> restaurant = setRestaurantForm(restaurants);
        return restaurant.get(0);
    }

    //DBから取得したレストラン情報をEntityからFormに詰め替え
    private List<RestaurantForm> setRestaurantForm(List<Restaurant> results) {
        List<RestaurantForm> restaurants = new ArrayList<>();

        for (Restaurant result : results) {
            RestaurantForm restaurant = new RestaurantForm();
            restaurant.setId(result.getId());
            restaurant.setName(result.getName());
            restaurant.setTelephone(result.getTelephone());
            restaurant.setAddress(result.getAddress());
            restaurant.setArea(result.getArea());
            restaurant.setGenre(result.getGenre());
            restaurant.setExplanation(result.getExplanation());
            restaurant.setStartBusiness(result.getStartBusiness());
            restaurant.setEndBusiness(result.getEndBusiness());
            restaurant.setCapacity(result.getCapacity());
            restaurant.setMinAmount(result.getMinAmount());
            restaurant.setMaxAmount(result.getMaxAmount());
            restaurant.setCreatedDate(result.getCreatedDate());
            restaurant.setUpdatedDate(result.getUpdatedDate());
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    // 人気エリア取得
    public List<AreaReservationCountDto> selectTopAreasByReservationCount() {
        return restaurantRepository.findTopAreasByReservationCount(PageRequest.of(0, 5));
    }

    // 人気ジャンル取得
    public List<GenreReservationCountDto> selectTopGenresByReservationCount() {
        return restaurantRepository.findTopGenresByReservationCount(PageRequest.of(0, 5));
    }

    // 人気店舗取得
    public List<RestaurantReservationCountDto> selectTopRestaurantsByReservationCount() {
        return restaurantRepository.findTopRestaurantsByReservationCount(PageRequest.of(0, 5));
    }

}
