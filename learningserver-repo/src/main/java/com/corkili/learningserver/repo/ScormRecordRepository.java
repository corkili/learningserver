package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.ScormRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScormRecordRepository extends JpaRepository<ScormRecord, Long> {

    Optional<ScormRecord> findByScormIdAndLearnerId(Long scormId, Long learnerId);

}
