package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Notice;

import java.util.List;

/**
 * 描述:
 * 通知服务接口
 *
 * @author HASEE
 * @create 2018-09-18 9:21
 */
public interface NoticeService extends BaseService<Notice> {
    List<Notice> getUnReadNotice(long course_id, long user_id);

    boolean updateNotice(long notice_id,long user_id);
}