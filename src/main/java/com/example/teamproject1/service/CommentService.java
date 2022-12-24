package com.example.teamproject1.service;

import com.example.teamproject1.dto.CommentDTO;
import com.example.teamproject1.entity.Article;
import com.example.teamproject1.entity.Comment;
import com.example.teamproject1.repository.ArticleRepository;
import com.example.teamproject1.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service //컨트롤러 선언
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public List<CommentDTO> comments(Long articleId) {

        //조회 : 댓글 목록
        List<Comment> comments = commentRepository.findByArticleId(articleId);

        //변환 : 엔티티 -> DTO
        List<CommentDTO> dtos = new ArrayList<CommentDTO>();
        for(int i = 0; i < comments.size(); i++) {
            Comment c = comments.get(i);
            CommentDTO dto = CommentDTO.createCommentDTO(c);
            dtos.add(dto);
        }

        //반환
        return dtos;
    }

    @Transactional //트랜잭션 처리 必
    public CommentDTO create(Long articleId, CommentDTO dto) {
        //게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패, 대상 게시글이 없습니다.")); //(못찾았다면)

        //댓글 엔티티 생성 (찾았다면)
        Comment comment = Comment.createComment(dto, article);

        //댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        //DTO로 변경하여 반환
        return CommentDTO.createCommentDTO(created);
    }

    @Transactional
    public CommentDTO update(Long id, CommentDTO dto) {
        //댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패, 대상 댓글이 없습니다."));
        //댓글 수정
        target.patch(dto);

        //DB로 갱신
        Comment updated = commentRepository.save(target);
        //댓글 엔티티를 DTO로 변환 및 반환
        return CommentDTO.createCommentDTO(updated);
    }

    @Transactional
    public CommentDTO delete(Long id) {
        //댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패, 대상이 없습니다."));
        //댓글 DB에서 삭제
        commentRepository.delete(target);
        //삭제 댓글 DTO로 반환
        return CommentDTO.createCommentDTO(target);
    }
}
