package com.example.teamproject1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@AllArgsConstructor //리팩토링
@NoArgsConstructor //디폴트 생성자 추가
@ToString //리팩토링
@Getter //id 값을 redirect
public class User {

    @Id
    private String userId; // 사용자 아이디

    @Column
    private String userNm; //사용자명

    @Column
    private String userPw; //비밀번호

    @Column
    private String phone; //전화번호

    @Column
    private String certifi; //인증

    @Column
    private String email; //이메일

    @Column
    private String rgiDate; //등록일

    public LocalDate getRgiDate() {
        Date date = new Date();
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();

        String rgiDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        LocalDate Date = LocalDate.parse(rgiDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Date;
    }

}
