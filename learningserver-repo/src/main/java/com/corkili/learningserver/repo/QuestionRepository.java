package com.corkili.learningserver.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByAuthorId(Long authorId);

    List<Question> findAllByAuthorIdAndQuestionContaining(Long authorId, String keyword);

    List<Question> findAllByAuthorIdAndQuestionTypeIn(Long authorId, Collection<Question.Type> questionTypes);

}
