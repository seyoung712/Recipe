package com.example.teamproject1.api;

import com.example.teamproject1.dto.CommentDTO;
import com.example.teamproject1.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentApiController {
    @Autowired
    private CommentService commentService;

    //댓글 목록 조회
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDTO>> comments(@PathVariable Long articleId) {
        //서비스에게 위임
        List<CommentDTO> dtos = commentService.comments(articleId);
        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(dtos); //성공한다는 가정하에
    }

    //댓글 생성
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDTO> create(@PathVariable Long articleId, @RequestBody CommentDTO dto) {
        //서비스에게 위임
        CommentDTO createdDTO = commentService.create(articleId, dto);

        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(createdDTO);
    }

    //댓글 수정
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDTO> update(@PathVariable Long id, @RequestBody CommentDTO dto) {
        //서비스에게 위임
        CommentDTO updatedDTO = commentService.update(id, dto);

        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(updatedDTO);
    }

    //댓글 삭제
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDTO> delete(@PathVariable Long id) {
        //서비스에게 위임
        CommentDTO updatedDTO = commentService.delete(id);

        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(updatedDTO);
    }
}
