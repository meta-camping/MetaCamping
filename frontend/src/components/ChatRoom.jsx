import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import axios from 'axios';
import Stomp from 'stompjs';
import '../styles/ChatRoom.css'
import { useParams, useNavigate, useLocation } from 'react-router-dom';

function ChatRoom( {match} ) {

  //list에서 넘어오는 데이터
  const {room_id} = useParams();
  const location = useLocation();
  const userCheck = location.state?.userCheck || "";
  console.log("@@@@@@@@@@@",userCheck)
  const [stompClient, setStompClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [connected, setConnected] = useState(false);
  const subscribeUrl = `/topic/${room_id}`;
  const publishUrl = `/app/hello/${room_id}`;
  const username = "테스트유저" //멤버 병합 시 수정
  const navigate = useNavigate();

  useEffect(() => {


    const socket = new SockJS('http://localhost:8080/ws-stomp');
    const stompClient = Stomp.over(socket);

    if (username.trim() !== '' ) {
      stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        axios.post(`/chat/room/getMessage`, {
          room_id: room_id,
          member_id: username
        })
            .then(response => {
              const messages = response.data;
              setMessages(messages);
            })
            .catch(error => {
              console.error(error);
            });
        stompClient.subscribe(subscribeUrl, function (greeting) {
          setMessages((messages) => [...messages, JSON.parse(greeting.body)]);
        });
        setStompClient(stompClient);
        if (userCheck === "가능") {
          stompClient.send(publishUrl, {}, JSON.stringify({
            room_id: room_id,
            type: 'ENTER',
            sender: username,
            message: username+'님이 입장했습니다.'
          }));
        }

      });
    }

    return () => {
      if (stompClient !== null) {
        stompClient.disconnect();
      }
      setConnected(false);
      console.log('Disconnected');
    };
  }, [username, subscribeUrl,userCheck]);

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
              <div key={idx} className={`chat-bubble ${msg.sender === username ? 'self' : 'others'}`}>
                <span><strong>{msg.sender}</strong> | {msg.create_time}</span>
                <div className="bubble">
                  <span>{msg.content}</span>
                </div>
              </div>
          ))}
        </div>
        <div className="chat-input">
          <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
          <button onClick={handleSend}>Send</button>
        </div>
        <button className="exit-button" onClick={exit}>채팅방 나가기</button>
      </div>
  );
}

export default ChatRoom;