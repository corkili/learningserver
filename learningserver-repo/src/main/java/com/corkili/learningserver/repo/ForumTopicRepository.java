package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.ForumTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, Long> {

    @Query("select ft.id from ForumTopic ft left join ft.belongCourse ftbc where ftbc.id = ?1")
    List<Long> findAllForumTopicIdByBelongCourseId(Long belongCourseId);

    void deleteAllByBelongCourseId(Long belongCourseId);

    List<ForumTopic> findAllByBelongCourseId(Long belongCourseId);

}
