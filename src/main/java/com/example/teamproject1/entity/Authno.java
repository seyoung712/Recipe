package com.example.teamproject1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@AllArgsConstructor //리팩토링
@NoArgsConstructor //디폴트 생성자 추가
@ToString //리팩토링
@Getter //id 값을 redirect
public class Authno {

    @Id
    private String phone; // 전화번호

    @Column
    private String authNo; // 인증번호



}
