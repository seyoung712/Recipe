package com.example.teamproject1.service;

import com.example.teamproject1.entity.User;
import com.example.teamproject1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service //서비스 선언
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public int dupCheck(String userId) {
        int dupCheckCnt = 0;
        User user = userRepository.findByUserId(userId);
        if ( user != null && user.getUserId() != null  ) dupCheckCnt = 1;
        return dupCheckCnt;
    }

    public void authSend(String phone, String authNo) {
        userRepository.deleteAuthNo(phone);
        userRepository.insertAuthNo(phone, authNo);
    }

    public int checkAuthNo(String phone, String authNo) {
        int checkCnt = userRepository.checkAuthNo(phone, authNo);
        return checkCnt;
    }

    public void insertUser(String userId, String userNm, String userPw, String phone, String email) {
        userRepository.insertUser(userId, userNm, userPw, phone, email);
    }

    public User getLoginUser(String userId, String userPw) {
        User user = userRepository.getLoginUserId(userId, userPw);
        return user;
    }
}
