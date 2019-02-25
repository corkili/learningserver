package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
