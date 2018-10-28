package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.im.Message;
import io.flippedclassroom.server.repository.MessageRepository;
import io.flippedclassroom.server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public List<Message> findMessagesByDate(String date) {
		return messageRepository.findByDate(date);
	}

	@Override
	public List<Message> findMessagesByUser(User user) {
		return messageRepository.findByUser(user);
	}

	@Override
	public Message findById(Long id) {
		return messageRepository.findById(id);
	}

	@Override
	public Message save(Message message) {
		return messageRepository.save(message);
	}

	@Override
	public List<Message> save(Iterable<Message> iterable) {
		return messageRepository.save(iterable);
	}

	@Override
	public void delete(Message message) {
		messageRepository.delete(message);
	}

    @Override
    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
	public void delete(Iterable<Message> iterable) {
		messageRepository.delete(iterable);
	}

	@Override
	public void deleteAll() {
		messageRepository.deleteAll();
	}
}
