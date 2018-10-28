package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.MyFile;
import io.flippedclassroom.server.repository.MyFileReposiory;
import io.flippedclassroom.server.service.MyFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * 描述:
 * 文件ServiceImpl
 *
 * @author HASEE
 * @create 2018-09-11 13:18
 */
@Service
@Slf4j
public class MyfileServiceImpl implements MyFileService {
    @Autowired
    MyFileReposiory myFileReposiory;

    @Override
    public MyFile findById(Long id) {
        return myFileReposiory.findOne(id);
    }

    @Override
    public MyFile save(MyFile myFile) {
        return myFileReposiory.save(myFile);
    }

    @Override
    public List<MyFile> save(Iterable<MyFile> iterable) {
        return myFileReposiory.save(iterable);
    }

    @Override
    public void delete(MyFile myFile) {
        myFileReposiory.delete(myFile);
    }

    @Override
    public void deleteById(Long id) {
        myFileReposiory.delete(id);
    }

    @Override
    public void delete(Iterable<MyFile> iterable) {
        myFileReposiory.delete(iterable);
    }

    @Override
    public void deleteAll() {
        myFileReposiory.deleteAll();
    }

    @Override
    public boolean assertReapt(long course_id, String file_format, String file_position) {
        List<MyFile> list= myFileReposiory.assertReapt(course_id,file_format);
        boolean tag=false;
        for (MyFile myFile:list){
            if(myFile.getPosition().equals(file_position)){
                tag=true;
                break;
            }
        }
        return tag;
    }

    public List<MyFile> findByCourseId(long course_id){
        return myFileReposiory.findByCourse(new Course().setCourse_id(course_id));
    }
}