/*
 * Copyright (c) 2018 - 2019 Maishapay
 * All rights reserved
 * Contact: contact@maishapay.online
 * Website: http://www.maishapay.online
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Maishapay developers at contact@maishapay.online.
 */

package com.maishapay.smssync.data.entity.mapper;

import com.maishapay.smssync.data.entity.Message;
import com.maishapay.smssync.domain.entity.MessageEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class MessageDataMapper {

    @Inject
    public MessageDataMapper() {
        // Do nothing
    }

    public MessageEntity map(Message message) {
        MessageEntity messageEntity = null;
        if (message != null) {
            messageEntity = new MessageEntity();
            messageEntity.setId(message.getId());
            messageEntity.setMessageBody(message.getMessageBody());
            messageEntity.setMessageDate(message.getMessageDate());
            messageEntity.setMessageFrom(message.getMessageFrom());
            messageEntity.setMessageUuid(message.getMessageUuid());
            messageEntity.setSentResultMessage(message.getSentResultMessage());
            messageEntity.setSentResultCode(message.getSentResultCode());
            messageEntity.setMessageType(MessageEntity.Type.valueOf(message.getMessageType().name()));
            messageEntity.setStatus(MessageEntity.Status.valueOf(message.getStatus().name()));
            messageEntity.setDeliveryResultCode(message.getDeliveryResultCode());
            messageEntity.setDeliveryResultMessage(message.getDeliveryResultMessage());
        }
        return messageEntity;
    }

    public Message map(MessageEntity messageEntity) {
        Message message = null;
        if (messageEntity != null) {
            message = new Message();
            message.setId(messageEntity.getId());
            message.setMessageBody(messageEntity.getMessageBody());
            message.setMessageDate(messageEntity.getMessageDate());
            message.setMessageFrom(messageEntity.getMessageFrom());
            message.setMessageUuid(messageEntity.getMessageUuid());
            message.setSentResultMessage(messageEntity.getSentResultMessage());
            message.setSentResultCode(messageEntity.getSentResultCode());
            message.setMessageType(map(messageEntity.getMessageType()));
            message.setStatus(map(messageEntity.getStatus()));
            message.setDeliveryResultCode(messageEntity.getDeliveryResultCode());
            message.setDeliveryResultMessage(messageEntity.getDeliveryResultMessage());
        }
        return message;
    }

    public List<MessageEntity> map(List<Message> messageList) {
        List<MessageEntity> messageEntityList = new ArrayList<>();
        MessageEntity messageEntity;
        for (Message message : messageList) {
            messageEntity = map(message);
            if (messageEntity != null) {
                messageEntityList.add(messageEntity);
            }
        }
        return messageEntityList;
    }

    public List<Message> unmap(List<MessageEntity> messageList) {
        List<Message> messages = new ArrayList<>();
        Message message;
        for (MessageEntity messageEntity : messageList) {
            message = map(messageEntity);
            if (message != null) {
                messages.add(message);
            }
        }
        return messages;
    }

    public Message.Status map(MessageEntity.Status status) {
        return Message.Status.valueOf(status.name());
    }

    public Message.Type map(MessageEntity.Type type) {
        return type != null ? Message.Type.valueOf(type.name()) : Message.Type.PENDING;
    }
}