package com.example.teamproject1.dto;

import com.example.teamproject1.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor //리팩토링
@ToString //리팩토링
public class ArticleForm {

    private Long id;
    private String division;
    private String title;
    private String content;
    private String rgiDate;


    public Article toEntity() {
        return new Article(id,division, title, content,rgiDate);
    }//entity

}
