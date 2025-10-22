package com.example.NYA_reservation.repository;

import com.example.NYA_reservation.repository.entity.RegularHolidays;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegularHolidaysRepository extends JpaRepository<RegularHolidays, Integer> {
    //レストランIDで定休日情報を取得
    List<RegularHolidays> findRegularHolidaysByRestaurantId(Integer restaurantId);

    //レストランIDで定休日情報を削除
    void deleteByRestaurantId(Integer id);
}
