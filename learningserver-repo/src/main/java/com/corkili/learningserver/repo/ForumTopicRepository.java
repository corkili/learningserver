package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.ForumTopic;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, Long> {

    @Query("select ft.id from ForumTopic ft left join ft.belongCourse ftbc where ftbc.id = ?1")
    List<Long> findAllForumTopicIdByBelongCourseId(Long belongCourseId);

}
