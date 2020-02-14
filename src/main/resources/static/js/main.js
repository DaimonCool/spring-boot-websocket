'use strict';

let usernamePage = document.querySelector('#username-page');
let chatPage = document.querySelector('#chat-page');
let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let connectingElement = document.querySelector('.connecting');

let stompClient = null;
let username = null;

let colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];


function connect(event) {

    username = document.querySelector('#name').value.trim();
    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        let socket = new SockJS('http://EPKZKARW0555:8080/javatechie');
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
            JSON.stringify({sender: username, type: 'JOIN'})
        )
    } else {
        stompClient.subscribe('/topic/public/1', onMessageReceived);

        // Tell your username to the server
        stompClient.send("/app/chat.register/1",
            {},
            JSON.stringify({sender: username, type: 'JOIN'})
        )
    }

    connectingElement.classList.add('hidden');
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
                console.log(innerMessage + "  fda")
                addMessage(innerMessage)
            })
        } else {
            addMessage(message);
        }
    } else {
        addMessage(message);
    }

}

//let typingTimeout;
const typingTimeoutMap = new Map();
function addMessage(message){

    let typingTimeoutUserKey = message.sender + "-timeout";
    let typingTimeout;
    console.log(typingTimeoutMap);
    if(typingTimeoutMap.has(typingTimeoutUserKey)) {
        console.log('has');
        typingTimeout = typingTimeoutMap.get(typingTimeoutUserKey);
    } else {
        console.log('doesnt');
        typingTimeoutMap.set(typingTimeoutUserKey, typingTimeout);
    }


    let typingId = message.sender + "-typing";
    let messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if(message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
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

            messageArea.appendChild(messageElement);
            messageArea.scrollTop = messageArea.scrollHeight;

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

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', send, true)
messageInput.addEventListener('input', typing, false)