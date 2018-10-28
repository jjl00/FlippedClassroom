package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Homework;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserHomework;
import io.flippedclassroom.server.repository.HomeworkRepository;
import io.flippedclassroom.server.repository.UserHomeworkRepository;
import io.flippedclassroom.server.service.BaseService;
import io.flippedclassroom.server.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 * 课后作业实现类
 *
 * @author HASEE
 * @create 2018-09-23 13:56
 */
@Service
public class HomeworkServiceImpl implements HomeworkService {
    @Autowired
    HomeworkRepository homeworkRepository;
    @Autowired
    UserHomeworkRepository userHomeworkRepository;
    @Override
    public List<Homework> getByCourseAndTitle(long title_id, long course_id) {
        return homeworkRepository.findByTitleAndCourseId(title_id,course_id);
    }

    @Override
    public List<String> getTitleByCourse(long course_id) {
        return homeworkRepository.findTitles(course_id);
    }


    public void commit(List<Integer> homework_ids,User user) {
        List<Homework> list=homework_ids.parallelStream().map(homeworkRepository::findOne).collect(Collectors.toList());
        List<UserHomework> list1=new ArrayList<>();
        list.forEach((homework)->{
            UserHomework userHomework=new UserHomework();
            userHomework.setCommit_date(new Date(System.currentTimeMillis()))
                    .setUser(user).setHomework(homework).set_right(false);
            list1.add(userHomework);
        });
        userHomeworkRepository.save(list1);
    }
    public List<Homework> getWrongAnswer(long user_id,long course_id){
        List<Integer> list=userHomeworkRepository.findWrongAnswer(course_id,user_id);
        List<Homework> list1=list.parallelStream().map((a)->homeworkRepository.findOne(a)).collect(Collectors.toList());
        return list1;
    }

    @Override
    public Homework findById(Long id) {
        return homeworkRepository.findOne(id.intValue());
    }

    @Override
    public Homework save(Homework homework) {
        return homeworkRepository.save(homework);
    }

    @Override
    public List<Homework> save(Iterable<Homework> iterable) {
        return homeworkRepository.save(iterable);
    }

    @Override
    public void delete(Homework homework) {
        homeworkRepository.delete(homework);
    }

    @Override
    public void deleteById(Long id) {
        homeworkRepository.delete(id.intValue());
    }

    @Override
    public void delete(Iterable<Homework> iterable) {
        homeworkRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        homeworkRepository.deleteAll();
    }
}