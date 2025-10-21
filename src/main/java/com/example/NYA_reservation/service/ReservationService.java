package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.repository.ReservationRepository;
import com.example.NYA_reservation.repository.entity.Reservation;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    /*
     * 予約登録処理
     */
    public void saveReservation(ReservationForm reservationForm){
        Reservation reservation = setReservationEntity(reservationForm);
        reservationRepository.save(reservation);
    }

    /*
     * フォームで送られた値をEntityに詰め替え
     */
    private Reservation setReservationEntity(ReservationForm reservationForm){
        Reservation reservation = new Reservation();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(reservationForm.getRestaurantId());

        //予約登録時はid == null 予約編集時はid != null
//        if(reservationForm.getId() != null){
//            reservation.setId(reservationForm.getId());
//        }

        reservation.setReservationDate(reservationForm.getReservationDate());
        reservation.setReservationTime(reservationForm.getReservationTime());
        reservation.setHeadcount(reservationForm.getHeadcount());
        reservation.setRestaurant(restaurant);
        reservation.setUserId(reservationForm.getUserId());

        return reservation;
    }

}
