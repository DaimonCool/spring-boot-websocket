package com.dashko.spring.ws.api.controller

import com.dashko.spring.ws.api.model.MessageType
import com.dashko.spring.ws.api.model.dto.ChatMessageDTO
import com.dashko.spring.ws.api.model.dto.JoinChatDTO
import com.dashko.spring.ws.api.service.MessageService
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import spock.lang.Specification

import java.time.LocalDateTime

class ChatControllerUTSpec extends Specification {

    def messageService = Mock(MessageService)
    def chatController = new ChatController(messageService)

    def "should save message when message type is not TYPING"() {
        given: "chat message dto prepared"
        def chatMessageDto = new ChatMessageDTO(messageValue: "Dima joined chat", sender: "Dima",
                sendDate: LocalDateTime.now(), type: MessageType.JOIN)

        and: "chat id prepared"
        def chatId = 1

        when: "save message"
        def returnedChatMessageDto = chatController.sendMessage(chatMessageDto, chatId)

        then: "message is saved"
        1 * messageService.saveMessage(chatMessageDto, chatId)

        and: "chat message dto returned"
        returnedChatMessageDto == chatMessageDto
    }

    def "should not save message when message type is TYPING"() {
        given: "chat message dto prepared"
        def chatMessageDto = new ChatMessageDTO(messageValue: "Dima is typing", sender: "Dima",
                sendDate: LocalDateTime.now(), type: MessageType.TYPING)

        and: "chat id prepared"
        def chatId = 1

        when: "save message"
        def returnedChatMessageDto = chatController.sendMessage(chatMessageDto, chatId)

        then: "message is not saved"
        0 * messageService.saveMessage(chatMessageDto, chatId)

        and: "chat message dto returned"
        returnedChatMessageDto == chatMessageDto
    }

    def "should register user and join chat"() {
        given: "chat message dto prepared"
        def chatMessageDto = new ChatMessageDTO(messageValue: "Dima is joined", sender: "Dima",
                sendDate: LocalDateTime.now(), type: MessageType.JOIN)

        and: "default page and number of messages prepared"
        def page = 0
        def numberOfMessages = 30

        and: "header accessor prepared"
        def headerAccessor = Mock(SimpMessageHeaderAccessor)

        and: "chat id prepared"
        def chatId = 1

        and: "sessionAttributes prepared"
        def sessionAttributes = Mock(Map)

        and: "headerAccessor contains session attributes"
        headerAccessor.getSessionAttributes() >> sessionAttributes

        and: "user join chat"
        1 * messageService.joinChat(chatMessageDto, chatId, page, numberOfMessages) >> new JoinChatDTO()

        when: "register user and join chat"
        def joinChatDto = chatController.register(chatMessageDto, headerAccessor, chatId)

        then: "session attributes are set"
        1 * sessionAttributes.put("username", chatMessageDto.getSender())
        1 * sessionAttributes.put("chatId", chatId)

        and: "join chat dto returned"
        joinChatDto == new JoinChatDTO()
    }

    def "should throw NullPointerException when session attributes null"() {
        given: "chat message dto prepared"
        def chatMessageDto = new ChatMessageDTO(messageValue: "Dima is joined", sender: "Dima",
                sendDate: LocalDateTime.now(), type: MessageType.JOIN)

        and: "header accessor prepared"
        def headerAccessor = Mock(SimpMessageHeaderAccessor)

        and: "chat id prepared"
        def chatId = 1

        and: "sessionAttributes prepared"
        def sessionAttributes = null

        and: "headerAccessor contains session attributes"
        headerAccessor.getSessionAttributes() >> sessionAttributes

        when: "register user and join chat"
        chatController.register(chatMessageDto, headerAccessor, chatId)

        then: "NullPointerException is thrown"
        thrown NullPointerException
    }
}
