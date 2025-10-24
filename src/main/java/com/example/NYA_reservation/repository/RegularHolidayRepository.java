package com.example.NYA_reservation.repository;

import com.example.NYA_reservation.repository.entity.RegularHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegularHolidayRepository extends JpaRepository<RegularHoliday, Integer> {
    //レストランIDで定休日情報を取得
    List<RegularHoliday> findRegularHolidaysByRestaurantId(Integer restaurantId);

    //レストランIDで定休日情報を削除
    void deleteByRestaurantId(Integer id);

    List<RegularHoliday> findByRestaurantId(Integer restaurantId);
}
