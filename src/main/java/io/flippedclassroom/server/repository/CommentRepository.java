package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select * from tb_comment where user_id=?1", nativeQuery = true)
    List<Comment> findAllByUser(Long user_id);

    List<Comment> findAllByCourse(Course course);


    List<Comment> findAllByReply(Long commentId);
}
