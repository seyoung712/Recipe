package com.example.teamproject1.service;

import com.example.teamproject1.dto.ArticleForm;
import com.example.teamproject1.entity.Article;
import com.example.teamproject1.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service //서비스 선언
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if (article.getId() != null){
            return null; //id를 설정하여 넣어준다면, 반환 X -> 즉 id없이 요청을 보내야 함
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        //수정용 엔티티 생성
        Article article = dto.toEntity();
        log.info("id : {}, article: {}", id, article.toString());

        //대상 엔티티 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //잘못된 요청 처리(대상이 없거나, id가 다른 경우)
        if(target == null || id != article.getId()){
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null;
        }

        //업데이트
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        //대상 에티티 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //잘못된 요청 처리 (대상이 없는 경우)
        if(target == null){
            return null;
        }

        //대상 삭제 후 응답 반환
        articleRepository.delete(target);
        return target;
    }

/*
    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        //dto -> entity
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());

        //entity -> db저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        //강제 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결재 실패!")
        );

        //결과값 반환
        return articleList;
    }
*/
}
