package com.example.teamproject1.controller;

import com.example.teamproject1.entity.User;
import com.example.teamproject1.repository.LoginRepository;
import com.example.teamproject1.repository.UserRepository;
import com.example.teamproject1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j //로깅
public class LoginController {

    @Autowired
    private UserService userService;


    @GetMapping("/view/login/login")
    public String login() {
        return "view/login/login";
    }

    @ResponseBody
    @PostMapping("/view/join/loginProc")
    public Map<String, String> loginProc(@RequestParam Map<String, String> param, HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        String userId = param.get("userId");
        String userPw = param.get("userPw");
        User user = userService.getLoginUser(userId, userPw);
        if ( user != null && user.getUserId() != null ) {
            map.put("result", "1");
            HttpSession session = request.getSession();  //세션기능
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userNm", user.getUserNm());
            session.setAttribute("phone", user.getPhone());
            session.setAttribute("email", user.getEmail());
        } else {
            map.put("result", "0");
        }

        return map;
    }

}
