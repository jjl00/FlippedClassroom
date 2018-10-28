package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Homework;
import io.flippedclassroom.server.entity.User;

import java.util.List;

/**
 * 描述:
 * homework服务接口
 *
 * @author HASEE
 * @create 2018-09-23 13:33
 */
public interface HomeworkService extends BaseService<Homework>{
    List<Homework> getByCourseAndTitle(long title_id,long course_id);

    List<String> getTitleByCourse(long course_id);

    public void commit(List<Integer> homework_ids,User user);

    List<Homework> getWrongAnswer(long user_id,long course_id);
}