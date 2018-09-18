package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;

import java.util.List;

public interface CommentService extends BaseService<Comment> {
    List<Comment> findCommentsByUser(User user);

    List<Comment> findCommentsByCourse(Course course);

    List<Comment> findReplyTo(Long commentId);

    void deleteById(Long commentId);
}
