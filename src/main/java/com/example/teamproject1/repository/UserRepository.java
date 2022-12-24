package com.example.teamproject1.repository;

import com.example.teamproject1.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    @Override
    ArrayList<User> findAll();

    @Query(value = "SELECT * FROM USER WHERE user_id  = :userId", nativeQuery = true)
    User findByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM AUTHNO WHERE phone  = :phone", nativeQuery = true)
    void deleteAuthNo(String phone);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO AUTHNO ( PHONE, AUTH_NO ) VALUES ( :phone, :authNo )", nativeQuery = true)
    void insertAuthNo(String phone, String authNo);

    @Query(value = "SELECT COUNT(*) CNT FROM AUTHNO WHERE PHONE  = :phone AND auth_no = :authNo", nativeQuery = true)
    int checkAuthNo(String phone, String authNo);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO USER ( USER_ID, USER_NM, USER_PW, PHONE, EMAIL ) VALUES ( :userId, :userNm, :userPw, :phone, :email )", nativeQuery = true)
    void insertUser(String userId, String userNm, String userPw, String phone, String email);

    @Query(value = "SELECT * FROM USER WHERE user_id  = :userId AND user_pw = :userPw", nativeQuery = true)
    User getLoginUserId(String userId, String userPw);
}
