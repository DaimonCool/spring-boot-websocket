'use strict';

let usernamePage = document.querySelector('#username-page');
let chatPage = document.querySelector('#chat-page');
let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let connectingElement = document.querySelector('.connecting');
let loadingElement = document.querySelector('.loading');

let stompClient = null;
let username = null;

let colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

let page = 1;
let messagesNum = 30;


function connect(event) {

    username = document.querySelector('#name').value.trim();
    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        let socket = new SockJS('/javatechie');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    if(username === 'dima' || username === 'kolya') {
        stompClient.subscribe('/topic/public/10', onMessageReceived);

        // Tell your username to the server
        stompClient.send("/app/chat.register/10",
            {},
            JSON.stringify({sender: username, content: username + " joined!", type: 'JOIN'})
        )
    } else {
        stompClient.subscribe('/topic/public/1', onMessageReceived);

        // Tell your username to the server
        stompClient.send("/app/chat.register/1",
            {},
            JSON.stringify({sender: username, content: username + " joined!", type: 'JOIN'})
        )
    }

    connectingElement.classList.add('hidden');
    loadingElement.classList.remove('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function send(event) {
    let messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        let chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        if(username === 'dima' || username === 'kolya') {
            stompClient.send("/app/chat.send/10", {}, JSON.stringify(chatMessage));
        } else {
            stompClient.send("/app/chat.send/1", {}, JSON.stringify(chatMessage));
        }
        messageInput.value = '';
    }
    event.preventDefault();
}

function typing() {
    if(stompClient) {
        let chatMessage = {
            sender: username,
            content: null,
            type: 'TYPING'
        };

        if(username === 'dima' || username === 'kolya') {
            stompClient.send("/app/chat.send/10", {}, JSON.stringify(chatMessage));
        } else {
            stompClient.send("/app/chat.send/1", {}, JSON.stringify(chatMessage));
        }
    }
}


function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    if(message.type === 'JOIN') {
        if(message.sender === username){
            message.messages.forEach(function(innerMessage){
                loadingElement.classList.add('hidden');
                addMessage(innerMessage, true)
            })
        } else {
            addMessage(message, true);
        }
    } else {
        addMessage(message, true);
    }

}

//let typingTimeout;
const typingTimeoutMap = new Map();
function addMessage(message, isLoadingMessages, currentScroll = 0){

    let typingTimeoutUserKey = message.sender + "-timeout";
    let typingTimeout;
    if(typingTimeoutMap.has(typingTimeoutUserKey)) {
        typingTimeout = typingTimeoutMap.get(typingTimeoutUserKey);
    } else {
        typingTimeoutMap.set(typingTimeoutUserKey, typingTimeout);
    }


    let typingId = message.sender + "-typing";
    let messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
    } else if(message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
    } else if (message.type === 'TYPING' && message.sender != username) {

        let typingLi = document.getElementById(typingId);

        if(typingLi != null){
            clearTimeout(typingTimeout);
            typingTimeout = setTimeout(function(){
                let typingElement = document.getElementById(typingId);
                if(typingElement != null) {
                    typingElement.remove();
                }
            }, 1000)
            typingTimeoutMap.set(typingTimeoutUserKey, typingTimeout);

            return;
        }
        messageElement.classList.add('event-message');
        message.content = message.sender + ' is typing';
        messageElement.id = typingId;


        typingTimeout = setTimeout(function(){
            let typingElement = document.getElementById(typingId);
            if(typingElement != null) {
                typingElement.remove();
            }
        }, 1000)
        typingTimeoutMap.set(typingTimeoutUserKey, typingTimeout);


    } else {
        messageElement.classList.add('chat-message');

        let avatarElement = document.createElement('i');
        let avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);


    }

        if(message.content != null){
            let textElement = document.createElement('p');
            let messageText = document.createTextNode(message.content);
            textElement.appendChild(messageText);

            messageElement.appendChild(textElement);

            if(isLoadingMessages){
                messageArea.appendChild(messageElement);

                if((messageArea.scrollTop === (messageArea.scrollHeight - messageArea.offsetHeight)) ||
                (message.sender === username)) {
                    messageArea.scrollTop = messageArea.scrollHeight;
                }

            } else {
                messageArea.insertBefore(messageElement, messageArea.firstChild);
                messageArea.scrollTop = messageArea.scrollHeight - currentScroll;
            }

            if(message.type === 'CHAT') {
                let typingElement = document.getElementById(typingId);
                if(typingElement != null) {
                    typingElement.remove();
                }
                clearTimeout(typingTimeout);
            }

       }

}

function getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    let index = Math.abs(hash % colors.length);
    return colors[index];
}

$("#messageArea").on("scroll",function() {
    let contentDiv = $(this);
    contentDiv.addClass("scrolling");

    if(contentDiv.scrollTop() === 0) {
        loadMessages();
        contentDiv.removeClass("nottop");
     } else {
        contentDiv.addClass("nottop");
     }

});

async function loadMessages() {
    loadingElement.classList.remove('hidden');
    let response = await fetch("/chat/1/messages?page=" + page + "&messagesNum=" + messagesNum);

    if (response.ok) {
      page++;
      messagesNum++;

      let messages = await response.json();
      messages = messages.reverse();
      let currentScroll = messageArea.scrollHeight
      messages.forEach(message => addMessage(message, false, currentScroll));

    } else {
      alert("Ошибка HTTP: " + response.status);
    }
    loadingElement.classList.add('hidden');
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', send, true)
messageInput.addEventListener('input', typing, false)
messageInput.addEventListener('input', typing, false)
