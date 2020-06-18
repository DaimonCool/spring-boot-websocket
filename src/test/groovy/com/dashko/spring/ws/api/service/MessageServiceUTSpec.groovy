package com.dashko.spring.ws.api.service

import com.dashko.spring.ws.api.model.Chat
import com.dashko.spring.ws.api.model.Message
import com.dashko.spring.ws.api.model.MessageType
import com.dashko.spring.ws.api.model.dto.ChatMessageDTO
import com.dashko.spring.ws.api.repository.MessageRepository
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDateTime

class MessageServiceUTSpec extends Specification {

    def messageRepository = Mock(MessageRepository)
    def messageService = new MessageService(messageRepository)

    def "should save message"() {
        given: "message dto and chat id prepared"
        def chatMessageDto = new ChatMessageDTO(messageValue: "Hello", sender: "Vasya", type: MessageType.CHAT)
        def chatId = 1

        and: "message entity prepared"
        def message = new Message(messageValue: chatMessageDto.getMessageValue(), sender: chatMessageDto.getSender(),
                chat: new Chat(chatId), messageType: chatMessageDto.getType())

        when: "save message"
        messageService.saveMessage(chatMessageDto, chatId)

        then: "message is saved"
        1 * messageRepository.save(message)
    }

    def "should get last messages by chat id, page and number of messages"() {
        given: "chat id, page and number of messages prepared"
        def chatId = 1
        def page = 0
        def numberOfMessages = 10

        and: "messages prepared"
        def chat = new Chat(chatId)
        def message1 = new Message(id: 1, messageValue: "Hello", sender: "Dima",
                sendDate: LocalDateTime.now(), chat: chat, messageType: MessageType.CHAT)
        def message2 = new Message(id: 2, messageValue: "Hello", sender: "Kolya",
                sendDate: LocalDateTime.now(), chat: chat, messageType: MessageType.CHAT)
        def messages = [message1, message2]

        and: "messages returned by pagination"
        messageRepository.findAllByChatIdOrderBySendDateDesc(chatId, PageRequest.of(page, numberOfMessages)) >> messages

        when: "get last messages by chat id, page and number of messages"
        def lastMessages = messageService.getLastMessages(chatId, page, numberOfMessages)

        then: "last messages returned"
        with(lastMessages[0]) {
            messageValue == message1.getMessageValue()
            sender == message1.getSender()
            type == message1.getMessageType()
        }
        with(lastMessages[1]) {
            messageValue == message2.getMessageValue()
            sender == message2.getSender()
            type == message2.getMessageType()
        }
    }

    def "should save message and return join chat dto when join chat"() {
        given: "chat id, page and number of messages prepared"
        def chatId = 1
        def page = 0
        def numberOfMessages = 10

        and: "messages prepared"
        def chat = new Chat(chatId)
        def message1 = new Message(id: 1, messageValue: "Hello", sender: "Dima",
                sendDate: LocalDateTime.now(), chat: chat, messageType: MessageType.CHAT)
        def message2 = new Message(id: 2, messageValue: "Hello", sender: "Kolya",
                sendDate: LocalDateTime.now(), chat: chat, messageType: MessageType.CHAT)
        def messages = [message1, message2]

        and: "messages returned by pagination"
        messageRepository.findAllByChatIdOrderBySendDateDesc(chatId, PageRequest.of(page, numberOfMessages)) >> messages

        and: "chat message dto prepared"
        def chatMessageDto = new ChatMessageDTO(messageValue: "Dima joined chat", sender: "Dima", type: MessageType.JOIN)

        and: "message entity prepared"
        def message = new Message(messageValue: chatMessageDto.getMessageValue(), sender: chatMessageDto.getSender(),
                chat: new Chat(chatId), messageType: chatMessageDto.getType())

        when: "join chat"
        def joinChatDto = messageService.joinChat(chatMessageDto, chatId, page, numberOfMessages)

        then: "join message is saved"
        1 * messageRepository.save(message)

        and: "join chat dto returned"
        with(joinChatDto) {
            messageValue == chatMessageDto.getMessageValue()
            sender == chatMessageDto.getSender()
            type == chatMessageDto.getType()
        }

        and: "last messages with join message returned"
        with(joinChatDto.messages[0]) {
            messageValue == message1.getMessageValue()
            sender == message1.getSender()
            type == message1.getMessageType()
        }
        with(joinChatDto.messages[1]) {
            messageValue == message2.getMessageValue()
            sender == message2.getSender()
            type == message2.getMessageType()
        }
        with(joinChatDto.messages[2]) {
            messageValue == chatMessageDto.getMessageValue()
            sender == chatMessageDto.getSender()
            type == chatMessageDto.getType()
        }
    }
}
