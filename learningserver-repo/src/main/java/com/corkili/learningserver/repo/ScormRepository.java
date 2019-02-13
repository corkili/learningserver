package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Scorm;

public interface ScormRepository extends JpaRepository<Scorm, Long> {

}
