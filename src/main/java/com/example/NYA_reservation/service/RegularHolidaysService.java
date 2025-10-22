package com.example.NYA_reservation.service;

import com.example.NYA_reservation.controller.form.RegularHolidaysForm;
import com.example.NYA_reservation.repository.RegularHolidaysRepository;
import com.example.NYA_reservation.repository.entity.RegularHoliday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegularHolidaysService {
    @Autowired
    RegularHolidaysRepository regularHolidaysRepository;

    //定休日情報を全件取得
    public List<RegularHolidaysForm> findAllRegularHolidays(){
        List<RegularHoliday> results = regularHolidaysRepository.findAll();
        return setRegularHolidaysForm(results);
    }

    //restaurantIdで定休日情報を取得
    public List<RegularHolidaysForm> findRegularHolidaysByRestaurantId(Integer restaurantId){
        List<RegularHoliday> result = regularHolidaysRepository.findRegularHolidaysByRestaurantId(restaurantId);
        return setRegularHolidaysForm(result);
    }

    //定休日を登録
    public void addRegularHolidays(List<RegularHolidaysForm> regularHolidays){
        for(RegularHolidaysForm regularHoliday : regularHolidays){
            RegularHoliday rh = setRegularHolidaysEntity(regularHoliday);
            regularHolidaysRepository.saveAndFlush(rh);
        }
    }

    //定休日を削除
    @Transactional
    public void deleteByRestaurantId(Integer id){
        regularHolidaysRepository.deleteByRestaurantId(id);
    }

    //DBから取得した情報をEntityからFormに詰め替える
    private List<RegularHolidaysForm> setRegularHolidaysForm(List<RegularHoliday> results){
        List<RegularHolidaysForm> regularHolidays = new ArrayList<>();

        for(RegularHoliday result : results){
            RegularHolidaysForm regularHoliday = new RegularHolidaysForm();
            regularHoliday.setRestaurantId(result.getRestaurantId());
            regularHoliday.setRegularHoliday(result.getRegularHoliday());
            regularHolidays.add(regularHoliday);
        }

        return regularHolidays;
    }

    //Formの値をEntityに詰め替える
    private RegularHoliday setRegularHolidaysEntity(RegularHolidaysForm regularHolidaysForm){
        RegularHoliday regularHolidays = new RegularHoliday();
        regularHolidays.setRegularHoliday(regularHolidaysForm.getRegularHoliday());
        regularHolidays.setRestaurantId(regularHolidaysForm.getRestaurantId());

        return regularHolidays;
    }
}
