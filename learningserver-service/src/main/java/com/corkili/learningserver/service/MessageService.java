package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.Message;
import com.corkili.learningserver.common.ServiceResult;

public interface MessageService extends Service<Message, com.corkili.learningserver.po.Message> {

    ServiceResult createMessage(Message message, byte[] imageData);

    ServiceResult findAllMessage(Long receiverId, Long senderId, boolean reverse);

    ServiceResult deleteMessage(Long messageId);

}
