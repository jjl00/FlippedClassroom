package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 描述:
 * 文件dao
 *
 * @author HASEE
 * @create 2018-09-11 13:15
 */
public interface MyFileReposiory extends JpaRepository<MyFile, Long> {
    @Query(value = "select * from tb_file where course_id=?1 and file_format=?2",nativeQuery = true)
    List<MyFile> assertReapt(long course_id,String file_format);

    List<MyFile> findByCourse(Course course);
}