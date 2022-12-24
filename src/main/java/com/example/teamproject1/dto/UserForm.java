package com.example.teamproject1.dto;

import com.example.teamproject1.entity.User;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor //리팩토링
@ToString //리팩토링
public class UserForm {

    private String userId;
    private String userNm;
    private String userPw;
    private String phone;
    private String certifi;
    private String email;
    private String rgiDate;


    public User toEntity() {
        return new User(userId,userNm, userPw, phone, certifi, email,rgiDate);
    }//entity

}
