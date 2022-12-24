package com.example.teamproject1.controller;

import com.example.teamproject1.dto.ArticleForm;
import com.example.teamproject1.dto.CommentDTO;
import com.example.teamproject1.entity.Article;
import com.example.teamproject1.entity.Comment;
import com.example.teamproject1.repository.ArticleRepository;
import com.example.teamproject1.service.CommentService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j //로깅
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired //스프링부트가 미리 생성해놓은 객체를 가져다가 자동 연결!
    private CommentService commentService;

    //게시판
    @GetMapping("/view/Community")
    public String newArticleForm() {
        return "view/Community";
    }

    //등록 기능 (데이터 입력 및 저장)
    //Community.mustache
    @PostMapping("/view/new")
    public String createArticle(ArticleForm form) {
        log.info(form.toString());

        //dto->entity
        Article article = form.toEntity();
        log.info(article.toString());
        //entity->db 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());

        return "redirect:/articles/" + saved.getId(); // redirect:글을 등록하면 방금 작성한 글을 확인
    }

    //조회 기능 (=> http://localhost:8080/articles/1)
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        log.info("id="+id);

        //id를 데이터로 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDTO> commentDTOS = commentService.comments(id);
        //가져온 데이터 모델에 등록
        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDTOS", commentDTOS);
        //페이지 설정
        return "view/show";
    }

    //목록 조회 (=> http://localhost:8080/articles)
    @GetMapping("/articles")
    public String index(Model model) {
        //모든 article 가져오기
        List<Article> articleEntityList = articleRepository.findAll();
        //뷰로 전달
        model.addAttribute("articleList",articleEntityList);
        //페이지 설정
        return "view/index";
    }

    //글 수정
    @GetMapping("/articles/{id}/articleEdit")
    public String articleEdit(@PathVariable Long id, Model model) {
        //수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        //모델에 데이터 등록
        model.addAttribute("article",articleEntity);
        //페이지 설정
        return "view/articleEdit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form) {
        log.info(form.toString());

        //dto-> entity
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());
        //entity->db 저장
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        if(target != null){
            articleRepository.save(articleEntity);
        }
        //리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    //글 삭제
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("삭제 요청");

        //삭제 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());
        //대상 삭제
        if(target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제가 완료되었습니다.");
        }
        //리다이렉트
        return "redirect:/articles";
    }
}
