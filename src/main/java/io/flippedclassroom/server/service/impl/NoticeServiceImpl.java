package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Notice;
import io.flippedclassroom.server.repository.NoticeRepository;
import io.flippedclassroom.server.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 * 通知服务
 *
 * @author HASEE
 * @create 2018-09-18 9:24
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeRepository noticeRepository;


    @Override
    public Notice findById(Long id) {
        return noticeRepository.findOne(id);
    }

    @Override
    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public List<Notice> save(Iterable<Notice> iterable) {
        return noticeRepository.save(iterable);
    }

    @Override
    public void delete(Notice notice) {
        noticeRepository.delete(notice);
    }

    @Override
    public void deleteById(Long id) {
        noticeRepository.delete(id);
    }

    @Override
    public void delete(Iterable<Notice> iterable) {
        noticeRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        noticeRepository.deleteAll();
    }

    @Override
    public List<Notice> getUnReadNotice(long course_id, long user_id) {
        return noticeRepository.getUnReadNotice(course_id,user_id);
    }

    @Override
    public boolean updateNotice(long notice_id, long user_id) {
        return (noticeRepository.updateNotice(notice_id,user_id)>1)?true:false;
    }
}