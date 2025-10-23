package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.RegularHolidayForm;
import com.example.NYA_reservation.repository.RegularHolidayRepository;
import com.example.NYA_reservation.repository.entity.RegularHoliday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegularHolidayService {
    @Autowired
    RegularHolidayRepository regularHolidayRepository;

    //定休日情報を全件取得
    public List<RegularHolidayForm> findAllRegularHolidays(){
        List<RegularHoliday> results = regularHolidayRepository.findAll();
        return setRegularHolidaysForm(results);
    }

    //restaurantIdで定休日情報を取得
    public List<RegularHolidayForm> findRegularHolidaysByRestaurantId(Integer restaurantId){
        List<RegularHoliday> result = regularHolidayRepository.findRegularHolidaysByRestaurantId(restaurantId);
        return setRegularHolidaysForm(result);
    }

    //定休日を登録
    public void addRegularHolidays(List<RegularHolidayForm> regularHolidays){
        for(RegularHolidayForm regularHoliday : regularHolidays){
            RegularHoliday rh = setRegularHolidaysEntity(regularHoliday);
            regularHolidayRepository.saveAndFlush(rh);
        }
    }

    //定休日を削除
    @Transactional
    public void deleteByRestaurantId(Integer id){
        regularHolidayRepository.deleteByRestaurantId(id);
    }

    //DBから取得した情報をEntityからFormに詰め替える
    private List<RegularHolidayForm> setRegularHolidaysForm(List<RegularHoliday> results){
        List<RegularHolidayForm> regularHolidays = new ArrayList<>();

        for(RegularHoliday result : results){
            RegularHolidayForm regularHoliday = new RegularHolidayForm();
            regularHoliday.setRestaurantId(result.getRestaurantId());
            regularHoliday.setRegularHoliday(result.getRegularHoliday());
            regularHolidays.add(regularHoliday);
        }

        return regularHolidays;
    }

    //Formの値をEntityに詰め替える
    private RegularHoliday setRegularHolidaysEntity(RegularHolidayForm regularHolidayForm){
        RegularHoliday regularHolidays = new RegularHoliday();
        regularHolidays.setRegularHoliday(regularHolidayForm.getRegularHoliday());
        regularHolidays.setRestaurantId(regularHolidayForm.getRestaurantId());

        return regularHolidays;
    }
}
