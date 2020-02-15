package com.dashko.spring.ws.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessageDTO {
	private String content;
	private String sender;
	private MessageType type;

}
