import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import axios from 'axios';
import Stomp from 'stompjs';
import '../styles/ChatRoom.css';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import moment from 'moment';
import { useRecoilState } from "recoil";
import { userState } from "../recoil/user";

function ChatRoom() {
  const { roomId } = useParams();
  const location = useLocation();
  const [userCheck,setUserCheck] = useState(`${location.state?.userCheck}`);

  const [stompClient, setStompClient] = useState(null);
  const [roomInfo,setRoomInfo]= useState({});
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [connected, setConnected] = useState(false);
  const subscribeUrl = `/topic/${roomId}`;
  const publishUrl = `/app/hello/${roomId}`;

  //멤버
  const [user,setUser] = useRecoilState(userState);
  const username = user.nickname
  const navigate = useNavigate();
  const messageEndRef = useRef(null);

  //유효성 검사
  const [isDisabled, setIsDisabled] = useState(false);

  useEffect(() => {
    messageEndRef.current.scrollIntoView({ behavior: 'smooth' });

    //roomInfo 받아오기
    axios.get(`/chat/room/${roomId}`)
      .then((res) => {
        if (res.data) {
          setRoomInfo(res.data);
        }
      })
      .catch((error) => {
        console.error(error);
      });

    const socket = new SockJS('http://localhost:8080/ws-stomp');
    const stompClient = Stomp.over(socket);

    if (username.trim() !== '' ) {
      stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe(subscribeUrl, function (greeting) {
          setMessages((messages) => [...messages, JSON.parse(greeting.body)]);
        });
        setStompClient(stompClient);

        if(userCheck !== '구독 유저') {
          stompClient.send(publishUrl, {}, JSON.stringify({ 
            roomId: roomId,
            type: 'ENTER',
            sender: username,
            message: username+'님이 입장했습니다.',
            createTime: moment().format('YYYY-MM-DD HH:mm:ss')
          })).then(() => {
            axios.post("/chat/room/user-check", {
              roomId: roomInfo.roomId,
              memberId: username
            }).then(res => {
              setUserCheck(res.data)
              console.log("업데이트 된",userCheck)
            }).catch(err => {
              console.log(err)
            });
          }).catch(err => {
            console.log(err);
          });
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
  }, [roomId, username, subscribeUrl, userCheck]);

  //채팅방 나가기
  const exit = () => {
    stompClient.unsubscribe(subscribeUrl);
    setConnected(false);
    setStompClient(null);
    navigate('/chat/list');
  }
  
   const handleSend = () => {
  
    stompClient.send(publishUrl, {}, JSON.stringify({ 
      roomId: roomId,
      type: 'TALK',
      sender: username,
      createTime: moment().format('YYYY-MM-DD HH:mm:ss'),
      message: newMessage }));

    setNewMessage('');
  
};

return (
<div>
  <h1> <strong>{roomInfo.roomName}</strong> </h1>
  <div>
  {messages.map((msg, idx) => (
    <div key={idx} className={`chat-bubble ${msg.sender === username ? 'self' : 'others'}`}>
      <span><strong>{msg.sender}</strong> | {new Date(msg.createTime).toLocaleString()}</span>
        <div className="bubble">
          <span>{msg.message}</span>
        </div>
    </div>
  ))}
  <div ref={messageEndRef}> </div>
  </div>

<div className="chat-input">
  <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
  <button onClick={handleSend} disabled={isDisabled}>Send</button>

</div>
<button className="exit-button" onClick={exit}>채팅방 나가기</button>
</div>
);
}

export default ChatRoom;