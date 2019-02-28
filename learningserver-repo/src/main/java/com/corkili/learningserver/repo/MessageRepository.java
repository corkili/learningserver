package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByReceiverIdAndSenderId(Long receiverId, Long senderId);

    List<Message> findAllByReceiverId(Long receiverId);

    List<Message> findAllBySenderId(Long senderId);

}
