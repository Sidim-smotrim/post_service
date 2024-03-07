package faang.school.postservice.controller;

import faang.school.postservice.dto.comment.CommentDto;
import faang.school.postservice.dto.comment.CommentEditDto;
import faang.school.postservice.service.CommentService;
import faang.school.postservice.validator.CommentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentService commentService;
    private final CommentValidator commentValidator;

    @PostMapping("/{postId}/comment")
    public CommentDto createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
        commentValidator.validateIdIsNotLessOne(postId);
        commentValidator.validateContent(commentDto.getContent());
        return commentService.createComment(postId, commentDto);
    }

    @PutMapping("/comments/{commentId}")
    public CommentDto updateComment(@PathVariable Long commentId,
                                    @RequestBody CommentEditDto commentEditDto) {
        commentValidator.validateIdIsNotLessOne(commentId);
        commentValidator.validateContent(commentEditDto.getContent());
        return commentService.updateComment(commentId, commentEditDto);
    }

    @GetMapping("/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable Long postId) {
        commentValidator.validateIdIsNotLessOne(postId);
        return commentService.getCommentsByPostId(postId);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentValidator.validateIdIsNotLessOne(commentId);
        commentService.deleteComment(commentId);
    }
}