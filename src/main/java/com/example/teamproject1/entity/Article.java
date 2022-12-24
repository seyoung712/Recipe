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
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@AllArgsConstructor //리팩토링
@NoArgsConstructor //디폴트 생성자 추가
@ToString //리팩토링
@Getter //id 값을 redirect
public class Article {

    @Id
    @GeneratedValue
    private Long id; //번호

    @Column
    private String division; //구분

    @Column
    private String title; //제목

    @Column
    private String content; //내용

    @Column
    private String rgiDate; //등록일

    public LocalDate getRgiDate() {
        Date date = new Date();
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();

        String rgiDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        LocalDate Date = LocalDate.parse(rgiDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Date;
    }


    public void patch(Article article) {
        if(article.title != null)
            this.title = article.title;
        if(article.content != null)
            this.content = article.content;
    }
}
