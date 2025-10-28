package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.RegularHolidayForm;
import com.example.NYA_reservation.controller.form.ReservationForm;
import com.example.NYA_reservation.repository.RegularHolidayRepository;
import com.example.NYA_reservation.repository.ReservationRepository;
import com.example.NYA_reservation.repository.RestaurantRepository;
import com.example.NYA_reservation.repository.entity.RegularHoliday;
import com.example.NYA_reservation.repository.entity.Reservation;
import com.example.NYA_reservation.repository.entity.Restaurant;
import jakarta.transaction.Transactional;
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
    @Autowired
    RegularHolidayRepository regularHolidayRepository;

    /*
     * 予約登録処理
     */
    public void saveReservation(ReservationForm reservationForm){
        Reservation reservation = setReservationEntity(reservationForm);
        reservationRepository.save(reservation);
    }

    /*
     * 予約件数取得
     */
    public Long countReservations(){
        return reservationRepository.count();
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
        List<Reservation> results = reservationRepository.findByUserIdOrderByReservationDateAsc(userId);
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
        Reservation result = reservationRepository.findById(id).orElse(null);
        if (result == null) {
            return null;
        }
        ReservationForm form = new ReservationForm();
        form.setId(result.getId());
        form.setReservationDate(result.getReservationDate());
        form.setReservationTime(result.getReservationTime());
        form.setHeadcount(result.getHeadcount());
        form.setUserId(result.getUserId());
        if (result.getRestaurant() != null) {
            form.setRestaurantId(result.getRestaurant().getId());
        }
        return form;
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

    //予約変更用の処理
    @Transactional
    public void updateReservation(ReservationForm form) {
        //既存の予約を取得
        Reservation reservation = reservationRepository.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("予約が存在しません: " + form.getId()));
        //変更項目を上書き
        reservation.setReservationDate(form.getReservationDate());
        reservation.setReservationTime(form.getReservationTime());
        reservation.setHeadcount(form.getHeadcount());
        Restaurant restaurant = restaurantRepository.findById(form.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("店舗が存在しません: " + form.getRestaurantId()));
        reservation.setRestaurant(restaurant);
        reservation.setUserId(form.getUserId());
        reservationRepository.save(reservation);
    }

    //当日変更・キャンセルの場合のボタン非表示用処理
    public boolean isAlreadyReservation(ReservationForm reservation) {
        return reservation.getReservationDate() != null &&
                reservation.getReservationDate().isEqual(LocalDate.now());
    }

    public List<RegularHolidayForm> getRegularHolidaysByRestaurantId(Integer restaurantId) {
        List<RegularHoliday> holidays = regularHolidayRepository.findByRestaurantId(restaurantId);

        List<RegularHolidayForm> forms = new ArrayList<>();
        for (RegularHoliday holiday : holidays) {
            RegularHolidayForm form = new RegularHolidayForm();
            form.setId(holiday.getId());
            form.setRestaurantId(holiday.getRestaurantId());
            form.setRegularHoliday(holiday.getRegularHoliday());
            forms.add(form);
        }

        return forms;
    }
}
