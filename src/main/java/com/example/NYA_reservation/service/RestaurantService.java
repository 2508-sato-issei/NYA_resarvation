package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.SearchForm;
import com.example.NYA_reservation.dto.AreaReservationCountDto;
import com.example.NYA_reservation.dto.GenreReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantReservationCountDto;
import com.example.NYA_reservation.repository.RestaurantRepository;
import com.example.NYA_reservation.dto.RestaurantSpecifications;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    // 検索結果取得
    public List<Restaurant> searchRestaurants(SearchForm searchForm) {
        String area = searchForm.getArea();
        String genre = searchForm.getGenre();
        Integer headcount = searchForm.getHeadcount();

        Specification<Restaurant> spec = RestaurantSpecifications.searchByCriteria(area, genre, headcount);
        return restaurantRepository.findAll(spec);
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
