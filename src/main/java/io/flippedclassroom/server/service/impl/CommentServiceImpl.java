package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.repository.CommentRepository;
import io.flippedclassroom.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment findById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public List<Comment> findCommentsByUser(User user) {
        return commentRepository.findAllByUser(user.getUser_id());
    }

    @Override
    public List<Comment> findCommentsByCourse(Course course) {
        return commentRepository.findAllByCourse(course);
    }

    @Override
    public List<Comment> findReplyTo(Long commentId) {
        return commentRepository.findAllByReply(commentId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> save(Iterable<Comment> iterable) {
        return commentRepository.save(iterable);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public void delete(Iterable<Comment> iterable) {
        commentRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        commentRepository.deleteAll();
    }

    @Override
    public void deleteById(Long commentId) {
        commentRepository.delete(commentId);
    }
}
