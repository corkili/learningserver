package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.CourseWork;
import com.corkili.learningserver.po.WorkQuestion;

public interface WorkQuestionRepository extends JpaRepository<WorkQuestion, Long> {

    List<WorkQuestion> findWorkQuestionsByBelongCourseWork(CourseWork courseWork);

}
