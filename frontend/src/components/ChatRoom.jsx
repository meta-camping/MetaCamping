import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import axios from 'axios';
import Stomp from 'stompjs';
import { useParams, useNavigate, useLocation } from 'react-router-dom';

function ChatRoom( {match} ) {
  const location = useLocation();
  const userCheck = location.state?.userCheck;
const [stompClient, setStompClient] = useState(null);
const [messages, setMessages] = useState([]);
const [newMessage, setNewMessage] = useState('');
const [connected, setConnected] = useState(false);
const {room_id} = useParams();
const subscribeUrl = `/topic/${room_id}`;
const publishUrl = `/app/hello/${room_id}`;
const username = "테스트유저" //멤버 병합 시 수정
const navigate = useNavigate();
useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws-stomp');
    const stompClient = Stomp.over(socket);

    if (username.trim() !== '' && userCheck === 1) { // username이 입력되면 stomp.connect 호출. 회원 정보 추가 시 JWT 확인으로 변경
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
          message: '입장'
        }));
        setStompClient(stompClient);
      });
    }else if(username.trim() !== '' && userCheck === 2){
      setConnected(true);
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
<strong>{msg.send_time} {msg.sender} {msg.content} </strong>

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

export default ChatRoom;