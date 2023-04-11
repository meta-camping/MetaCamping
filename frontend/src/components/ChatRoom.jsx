import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
<<<<<<< HEAD
import axios from 'axios';
import Stomp from 'stompjs';
import { useParams, useNavigate } from 'react-router-dom';

function ChatRoom( {match} ) {
const [stompClient, setStompClient] = useState(null);
const [messages, setMessages] = useState([]);
const [newMessage, setNewMessage] = useState('');
const [connected, setConnected] = useState(false);
const {room_id} = useParams();
const subscribeUrl = `/topic/${room_id}`;
const publishUrl = `/app/hello/${room_id}`;
const username = "테스트유저" //멤버 병합 시 수정
const navigate = useNavigate();

//회원 중복 참여 확인
const userCheck = () => {    
  axios.post("http://localhost:8080/chat/room/user-check",{
    room_id:room_id,
    member_id:username
  })
  .then(res => {
    console.log(res.data);
  })
  .catch(
      error => console.error(error)
      )    
} 
useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws-stomp');
    const stompClient = Stomp.over(socket);

    if (username.trim() !== '') { // username이 입력되면 stomp.connect 호출. 회원 정보 추가 시 유저 확인으로 변경
      stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe(subscribeUrl, function (greeting) {
          setMessages((messages) => [...messages, JSON.parse(greeting.body)]);
        });
        stompClient.send(publishUrl, {}, JSON.stringify({ 
          room_id: room_id,
          type: 'ENTER',
          sender: username,
          message: newMessage
        }));
        setStompClient(stompClient);
      });
    }


return () => {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log('Disconnected');
};
}, [username, subscribeUrl]);

//채팅방 나가기
const exit = () => {
  stompClient.unsubscribe(subscribeUrl);
  setConnected(false);
  setStompClient(null);
  navigate('/chat/list');
}


const handleSend = () => {
stompClient.send(publishUrl, {}, JSON.stringify({ 
  room_id: room_id,
  type: 'TALK',
  sender: username,
  message: newMessage }));
setNewMessage('');
};

return (
<div>
<h1>Chat Room {room_id}</h1>
<div>
{messages.map((msg, idx) => (
<div key={idx}>
<strong>{msg.content}</strong>
</div>
))}
</div>
<div>
<input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
<button onClick={handleSend}>Send</button>
</div>
<button onClick={exit}>채팅방 나가기</button>
</div>
);
}

=======
import Stomp from 'stompjs';
import { useParams } from 'react-router-dom';

function ChatRoom( {match} ) {
const [stompClient, setStompClient] = useState(null);
const [messages, setMessages] = useState([]);
const [newMessage, setNewMessage] = useState('');
const [connected, setConnected] = useState(false);
const {room_id} = useParams();
const subscribeUrl = `/topic/${room_id}`;
const publishUrl = `/app/hello/${room_id}`;
const username = "테스트유저" //멤버 병합 시 수정


useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws-stomp');
    const stompClient = Stomp.over(socket);

    if (username) { // username이 입력되면 stomp.connect 호출
      stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe(subscribeUrl, function (greeting) {
          setMessages((messages) => [...messages, JSON.parse(greeting.body)]);
        });
        stompClient.send(publishUrl, {}, JSON.stringify({ 
          room_id: room_id,
          type: 'ENTER',
          sender: username,
          message: newMessage
        }));
        setStompClient(stompClient);
      });
    }


return () => {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log('Disconnected');
};
}, [username, subscribeUrl]);



const handleSend = () => {
stompClient.send(publishUrl, {}, JSON.stringify({ 
  room_id: room_id,
  type: 'TALK',
  sender: username,
  message: newMessage }));
setNewMessage('');
};



return (
<div>
<h1>Chat Room {room_id}</h1>
<div>
{messages.map((msg, idx) => (
<div key={idx}>
<strong>{msg.content}</strong>
</div>
))}
</div>
<div>
<input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
<button onClick={handleSend}>Send</button>
</div>
</div>
);
}

export default ChatRoom;


/*import React, { useState } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useParams } from 'react-router-dom';

function ChatRoom({match}) {
  const [stompClient, setStompClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const {room_id} = useParams();
  
  const subscribeUrl = `/topic/${room_id}`;
  const publishUrl = '/app/hello';
  
  const setConnected = (connected) => {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    if (connected) {
      document.getElementById('conversation').style.display = 'block';
    } else {
      document.getElementById('conversation').style.display = 'none';
    }
    document.getElementById('greetings').innerHTML = '';
  };
  
  const connect = () => {
    const socket = new SockJS('http://localhost:8080/ws-stomp');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
      setConnected(true);
      console.log('Connected: ' + frame);
      stompClient.subscribe(subscribeUrl, function (greeting) {
        showGreeting(JSON.parse(greeting.body).content);
      });
      setStompClient(stompClient);
    });
  };

  const disconnect = () => {
    if (stompClient !== null) {
      stompClient.disconnect();
    }
    setConnected(false);
    console.log('Disconnected');
  };

  const sendName = () => {
    stompClient.send(publishUrl, {}, JSON.stringify({ 'name': document.getElementById('name').value }));
  };

  const showGreeting = (message) => {
    const greetings = document.getElementById('greetings');
    const tr = document.createElement('tr');
    const td = document.createElement('td');
    td.innerText = message;
    tr.appendChild(td);
    greetings.appendChild(tr);
  };

  return (
    <div>
      <form>
        <div className="form-group">
          <label htmlFor="connect">WebSocket connection:</label>
          <button id="connect" className="btn btn-default" type="button" onClick={connect}>Connect</button>
          <button id="disconnect" className="btn btn-default" type="button" onClick={disconnect} disabled>Disconnect</button>
        </div>
      </form>
      <form>
        <div className="form-group">
          <label htmlFor="name">What is your name?</label>
          <input type="text" id="name" className="form-control" placeholder="Your name here..." />
        </div>
        <button id="send" className="btn btn-default" type="button" onClick={sendName}>Send</button>
      </form>
      <table id="conversation" className="table table-striped" style={{ display: 'none' }}>
        <thead>
          <tr>
            <th>Greetings</th>
          </tr>
        </thead>
        <tbody id="greetings"></tbody>
      </table>
    </div>
  );
}

export default ChatRoom;
*/

/*
import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stomp-websocket';
import { useParams } from 'react-router-dom';

function ChatRoom({ match }) {
  const [stompClient, setStompClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const { room_id } = useParams();
  const subscribeUrl = `http://localhost:8080/topic/${room_id}`;
  const publishUrl = `http://localhost:8080/app/chat/${room_id}`;

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws-stomp');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      stompClient.subscribe(subscribeUrl, (message) => {
        const msgBody = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, msgBody])
        console.log("stompClient.subscribe",messages);
      });

      const enterMessage = {
        type: 'ENTER',
        sender: 'User',
        message: '',
        room_id: room_id,
      };

      stompClient.send(subscribeUrl, {}, JSON.stringify(enterMessage));
      setStompClient(stompClient);
    });

    return () => {
      stompClient.disconnect();
    };
  }, [room_id]);

  const handleSend = () => {
    const chatMessage = {
      type: 'TALK',
      sender: 'User',
      message: newMessage,
      roomId: room_id,
    };

    if (chatMessage.message === ''){
      alert("메세지를 입력해주세요.")
    }
    else{
      stompClient.send(publishUrl, {}, JSON.stringify(chatMessage));
      setNewMessage('');
    }
  };

  return (
    <div>
      <h1>Chat Room {room_id}</h1>
      <div>
        {messages.map((msg, idx) => (
          <div key={idx}>
            <strong>{msg.sender}</strong>: {msg.message}
          </div>
        ))}
      </div>
      <div>
        <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
        <button onClick={handleSend}>Send</button>
      </div>
    </div>
  );
}

>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742
export default ChatRoom;
*/