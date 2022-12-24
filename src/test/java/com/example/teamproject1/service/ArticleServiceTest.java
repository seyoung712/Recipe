package com.example.teamproject1.service;

import com.example.teamproject1.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ArticleServiceTest {

    @Autowired ArticleService articleService;

    @Test
    void index() {
        //예상 시나리오
        Article a = new Article(1L, "자유글","이마트 천안점 영업시간","몇시까지죠","2022-11-04");
        Article b = new Article(2L, "레시피 공유","낙지볶음","맛있다","2022-11-05");
        Article c = new Article(3L, "정보 공유","바나나 오래 보관하는 법","알려드려요","2022-11-06");
        List<Article> expected = new ArrayList<Article>(Arrays.asList(a, b, c));

        //실제 결과
        List<Article> articles = articleService.index();

        //비교
        assertEquals(expected.toString(), articles.toString());

    }
}