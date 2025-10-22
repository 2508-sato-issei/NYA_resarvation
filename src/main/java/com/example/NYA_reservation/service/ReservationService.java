package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.repository.ReservationRepository;
import com.example.NYA_reservation.repository.RestaurantRepository;
import com.example.NYA_reservation.repository.entity.Reservation;
import com.example.NYA_reservation.repository.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

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

    //マイページ画面に予約一覧を表示
    public List<ReservationForm> findByUserId(Integer userId) {
        List<Reservation> results = reservationRepository.findByUserIdOrderByIdDesc(userId);
        return setReservationForm(results);
    }

    // DBから取得したデータをFormに設定
    private List<ReservationForm> setReservationForm(List<Reservation> results){
        List<ReservationForm> reservations = new ArrayList<>();

        for(Reservation result : results){
            ReservationForm reservation = new ReservationForm();
            reservation.setId(result.getId());
            reservation.setReservationDate(result.getReservationDate());
            reservation.setReservationTime(result.getReservationTime());
            reservation.setHeadcount(result.getHeadcount());
            reservation.setUserId(result.getUserId());
            reservation.setCreatedDate(result.getCreatedDate());
            reservation.setUpdatedDate(result.getUpdatedDate());
            if (result.getRestaurant() != null) {
                reservation.setRestaurantId(result.getRestaurant().getId());
                reservation.setRestaurant(result.getRestaurant());
            }
            reservations.add(reservation);
        }
        return reservations;
    }

    //予約変更時の予約情報を取得
    public ReservationForm findById(Integer id) {
        Reservation result =  reservationRepository.findById(id).orElse(null);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(result);
        List<ReservationForm> reservation = setReservationForm(reservations);
        return reservation.get(0);
    }

    //予約をキャンセルする処理
    public void deleteReservation(Integer id) {
        reservationRepository.deleteById(id);
    }

    //定員を超える際のバリデーション用処理
    public Integer getRestaurantCapacity(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(Restaurant::getCapacity)
                .orElse(0);
    }

    //当日変更・キャンセルの場合のボタン非表示用処理
    public boolean isAlreadyReservation(ReservationForm reservation) {
        return reservation.getReservationDate() != null &&
                reservation.getReservationDate().isEqual(LocalDate.now());
    }

}
