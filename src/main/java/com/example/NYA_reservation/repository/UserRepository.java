package com.example.NYA_reservation.repository;

import com.example.NYA_reservation.repository.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByAccount(String account);

    //ユーザー停止有効切り替え
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isStopped = :isStopped, u.updatedDate = :updatedDate WHERE u.id = :id")
    public void changeIsStopped(@Param("id")Integer id, @Param("isStopped")boolean isStopped, @Param("updatedDate") Timestamp ts);
}
