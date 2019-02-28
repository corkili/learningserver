package com.corkili.learningserver.service.impl;

import com.corkili.learningserver.bo.Message;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.MessageRepository;
import com.corkili.learningserver.repo.UserRepository;
import com.corkili.learningserver.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<Message, com.corkili.learningserver.po.Message> implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult createMessage(Message message, byte[] imageData) {
        if (StringUtils.isBlank(message.getContent())) {
            return recordErrorAndCreateFailResultWithMessage("create message error: content is empty");
        }
        if (message.getReceiverId() == null || !userRepository.existsById(message.getReceiverId())) {
            return recordErrorAndCreateFailResultWithMessage("create message error: receiver not exists");
        }
        if (message.getSenderId() == null || !userRepository.existsById(message.getSenderId())) {
            return recordErrorAndCreateFailResultWithMessage("create message error: sender not exists");
        }
        if (message.isImage() && !ImageUtils.storeImage(message.getContent(), imageData)) {
            return recordErrorAndCreateFailResultWithMessage("create message error: store image failed");
        }
        Optional<Message> messageOptional = create(message);
        if (!messageOptional.isPresent()) {
            if (message.isImage()) {
                ImageUtils.deleteImage(message.getContent());
            }
            return recordErrorAndCreateFailResultWithMessage("create message error: create message failed");
        }
        message = messageOptional.get();
        return ServiceResult.successResult("create message success", Message.class, message);
    }

    @Override
    public ServiceResult findAllMessage(Long receiverId, Long senderId, boolean reverse) {
        List<com.corkili.learningserver.po.Message> allMessagePO;
        if (receiverId != null && senderId != null) {
            allMessagePO = messageRepository.findAllByReceiverIdAndSenderId(receiverId, senderId);
            if (reverse) {
                allMessagePO.addAll(messageRepository.findAllByReceiverIdAndSenderId(senderId, receiverId));
            }
        } else if (receiverId != null){
            allMessagePO = messageRepository.findAllByReceiverId(receiverId);
            if (reverse) {
                allMessagePO.addAll(messageRepository.findAllBySenderId(receiverId));
            }
        } else if (senderId != null) {
            allMessagePO = messageRepository.findAllBySenderId(senderId);
            if (reverse) {
                allMessagePO.addAll(messageRepository.findAllByReceiverId(senderId));
            }
        } else {
            allMessagePO = new LinkedList<>();
        }
        List<Message> allMessage = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.Message messagePO : allMessagePO) {
            Optional<Message> messageOptional = po2bo(messagePO);
            i++;
            if (!messageOptional.isPresent()) {
                errId.append(messagePO.getId());
                if (i != allMessagePO.size()) {
                    errId.append(",");
                }
            } else {
                Message message = messageOptional.get();
                allMessage.add(message);
                // cache
                putToCache(entityName() + message.getId(), message);
            }
        }
        String msg = "find all forumTopic success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all message warn: transfer message po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allMessage);
    }

    @Override
    public ServiceResult deleteMessage(Long messageId) {
        if (!delete(messageId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete message success");
        }
        return ServiceResult.successResultWithMesage("delete message success");
    }


}
