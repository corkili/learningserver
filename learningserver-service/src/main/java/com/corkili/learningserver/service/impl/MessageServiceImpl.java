package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Message;
import com.corkili.learningserver.repo.MessageRepository;
import com.corkili.learningserver.service.MessageService;

@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<Message, com.corkili.learningserver.po.Message> implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Optional<Message> po2bo(com.corkili.learningserver.po.Message messagePO) {
        Optional<Message> superOptional = super.po2bo(messagePO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        Message message = superOptional.get();
        message.setContent(messagePO.getContent());
        return Optional.of(message);
    }

    @Override
    public Optional<com.corkili.learningserver.po.Message> bo2po(Message message) {
        Optional<com.corkili.learningserver.po.Message> superOptional = super.bo2po(message);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.Message messagePO = superOptional.get();
        messagePO.setContent(message.getFormatContent());
        return Optional.of(messagePO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.Message, Long> repository() {
        return messageRepository;
    }

    @Override
    protected String entityName() {
        return "message";
    }

    @Override
    protected Message newBusinessObject() {
        return new Message();
    }

    @Override
    protected com.corkili.learningserver.po.Message newPersistObject() {
        return new com.corkili.learningserver.po.Message();
    }
}
