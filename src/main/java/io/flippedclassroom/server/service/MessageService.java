package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.im.Message;

import java.util.List;

public interface MessageService extends BaseService<Message> {
	List<Message> findMessagesByDate(String date);

	List<Message> findMessagesByUser(User user);
}
