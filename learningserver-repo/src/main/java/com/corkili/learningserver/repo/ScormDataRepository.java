package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.ScormData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScormDataRepository extends JpaRepository<ScormData, Long> {

    List<ScormData> findAllByScormIdAndItemAndLearnerId(Long scormId, String item, Long learnerId);

    List<ScormData> findAllByScormIdAndLearnerId(Long scormId, Long learnerId);

}
