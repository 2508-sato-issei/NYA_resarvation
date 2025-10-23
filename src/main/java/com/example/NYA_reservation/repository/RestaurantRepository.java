package com.example.NYA_reservation.repository;

import com.example.NYA_reservation.dto.AreaReservationCountDto;
import com.example.NYA_reservation.dto.GenreReservationCountDto;
import com.example.NYA_reservation.dto.RestaurantReservationCountDto;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>, JpaSpecificationExecutor<Restaurant> {

    // 人気エリア取得
    @Query("SELECT new com.example.NYA_reservation.dto.AreaReservationCountDto(r.area, COUNT(res)) " +
            "FROM Restaurant r " +
            "JOIN r.reservations res " +
            "GROUP BY r.area " +
            "ORDER BY COUNT(res) DESC")
    List<AreaReservationCountDto> findTopAreasByReservationCount(Pageable pageable);

    // 人気ジャンル取得
    @Query("SELECT new com.example.NYA_reservation.dto.GenreReservationCountDto(r.genre, COUNT(res)) " +
            "FROM Restaurant r " +
            "JOIN r.reservations res " +
            "GROUP BY r.genre " +
            "ORDER BY COUNT(res) DESC")
    List<GenreReservationCountDto> findTopGenresByReservationCount(Pageable pageable);

    // 人気店舗取得
    @Query("SELECT new com.example.NYA_reservation.dto.RestaurantReservationCountDto(r.id, r.name, COUNT(res)) " +
            "FROM Restaurant r " +
            "JOIN r.reservations res " +
            "GROUP BY r.id, r.name " +
            "ORDER BY COUNT(res) DESC")
    List<RestaurantReservationCountDto> findTopRestaurantsByReservationCount(Pageable pageable);

}
