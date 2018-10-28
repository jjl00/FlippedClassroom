package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.MyFile;

import java.util.List;

/**
 * 描述:
 * 文件service
 *
 * @author HASEE
 * @create 2018-09-11 13:17
 */
public interface MyFileService extends BaseService<MyFile> {
    boolean assertReapt(long course_id,String file_format,String file_position);

    List<MyFile> findByCourseId(long course_id);
}