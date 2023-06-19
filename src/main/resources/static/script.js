let socket = null;
let stompClient = null;
let privateStompClient = null;

socket = new SockJS("/websocket")
stompClient = Stomp.over(socket);
stompClient.debug = null;
stompClient.connect({}, frame => {
    stompClient.subscribe("/all/messages",
        result => displayMessage(JSON.parse(result.body))
    );
});

socket = new SockJS("/websocket")
privateStompClient = Stomp.over(socket);
privateStompClient.debug = null;
privateStompClient.connect({}, frame => {
    privateStompClient.subscribe("/user/specific",
        result => displayMessage(JSON.parse(result.body))
    );
});

function sendMessage(message) {
    stompClient.send('/app/application', {},
        JSON.stringify({ text: message })
    );
}

function sendPrivateMessage(message, to) {
    stompClient.send("/app/private", {},
        JSON.stringify({ "text" : message, "to" : to })
    );
}

function displayMessage(message) {
    $("#messages").append(`<tr><td> ${message.text} </td></tr>`);
}

$(function() {
    $("#sendForm").on("submit", (e) => {
        const message = $("#text")[0].value;

        sendMessage(message);
        e.target.reset();
        e.preventDefault();
    });

    $("#sendFormPrivate").on("submit", (e) => {
        const message = $("#textPrivate")[0].value;
        const to = $("#to")[0].value;

        sendPrivateMessage(message, to);
        e.target.reset();
        e.preventDefault();
    })
});
