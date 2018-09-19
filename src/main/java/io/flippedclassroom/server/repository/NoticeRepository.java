package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 描述:
 * 通知数据访问层
 *
 * @author HASEE
 * @create 2018-09-18 9:25
 */
public interface NoticeRepository extends JpaRepository<Notice,Long> {

    @Query(value = "select a.notice_id,a.notice_type,a.notice_content,a.notice_date,a.originator,a.course_id " +
            "from tb_notice a,tb_user_notice b where " +
            "a.notice_id=b.notice_id and b.is_read=0 and a.course_id=?1 and b.user_id=?2 ",nativeQuery = true)
    List<Notice> getUnReadNotice(long course_id,long user_id);

    @Query(value = "update tb_user_notice set is_read=1 where notice_id=?1 and user_id=?2 ",nativeQuery = true)
    @Modifying
    @Transactional
    int updateNotice(long notice_id,long user_id);




}