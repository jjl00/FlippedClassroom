package io.flippedclassroom.server.new_flippedclassroom;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.repository.CourseRepository;
import io.flippedclassroom.server.repository.MyFileReposiory;
import io.flippedclassroom.server.repository.NoticeRepository;
import io.flippedclassroom.server.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewFlippedclassroomApplicationTests {

    @Autowired
    NoticeRepository noticeRepository;
    @Test
    public void contextLoads() {
        System.out.println(noticeRepository.updateNotice(1,5));
    }
}
