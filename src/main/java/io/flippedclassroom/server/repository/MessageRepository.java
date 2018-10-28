package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.im.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
	Message findById(Long id);

	List<Message> findByDate(String date);

	List<Message> findByUser(User user);

	void deleteById(Long messageId);
}
