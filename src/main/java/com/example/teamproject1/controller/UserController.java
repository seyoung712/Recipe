package com.example.teamproject1.controller;

import com.example.teamproject1.dto.ArticleForm;
import com.example.teamproject1.dto.CommentDTO;
import com.example.teamproject1.entity.Article;
import com.example.teamproject1.repository.ArticleRepository;
import com.example.teamproject1.repository.UserRepository;
import com.example.teamproject1.service.CommentService;
import com.example.teamproject1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@Slf4j //로깅
public class UserController {

    @Autowired
    private UserService userService;


    //등록안내페이지
    @GetMapping("/view/join/info")
    public String info() {
        return "view/join/info";
    }

    //등록페이지
    @GetMapping("/view/join/reg")
    public String reg() {
        return "view/join/reg";
    }

    // 중복체크
    @ResponseBody
    @GetMapping("/view/join/dupCheck")
    public Map<String, String> dupCheck(@RequestParam Map<String, String> param) {
        Map<String, String> map = new HashMap<String, String>();
        String userId = param.get("userId");
        int dupCnt = userService.dupCheck(userId);
        map.put("dupCheckCnt", String.valueOf(dupCnt));
        return map;
    }

    // 인증번호 전송
    @ResponseBody
    @GetMapping("/view/join/authSend")
    public Map<String, String> authSend(@RequestParam Map<String, String> param) {
        Map<String, String> map = new HashMap<String, String>();
        String phone = param.get("phone");
        String authNo = numberGen(4, 1);
        System.out.println("인증번호 :::::: " + authNo);
        userService.authSend(phone, authNo);
        map.put("result", "1");
        return map;
    }

    // 인증번호 체크
    @ResponseBody
    @GetMapping("/view/join/checkAuthNo")
    public Map<String, String> checkAuthNo(@RequestParam Map<String, String> param) {
        Map<String, String> map = new HashMap<String, String>();
        String phone = param.get("phone");
        String authNo = param.get("authNo");
        int checkCnt = userService.checkAuthNo(phone,authNo);
        map.put("result", String.valueOf(checkCnt));
        return map;
    }

    // 가입처리
    @ResponseBody
    @PostMapping("/view/join/joinOk")
    public Map<String, String> joinOk(@RequestParam Map<String, String> param) {
        Map<String, String> map = new HashMap<String, String>();
        String userId = param.get("userId");
        String userNm = param.get("userNm");
        String userPw = param.get("userPw");
        String phone = param.get("phone");
        String email = param.get("email");
        userService.insertUser(userId, userNm, userPw, phone, email);
        map.put("result", "1");
        return map;
    }

    private String numberGen(int len, int dupCd ) {

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<len;i++) {

            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            if(dupCd==1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            }else if(dupCd==2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if(!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i-=1;
                }
            }
        }
        return numStr;
    }



}
