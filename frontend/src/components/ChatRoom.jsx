
import React, { useState, useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useParams } from 'react-router-dom';

function ChatRoom({ match }) {
  const [stompClient, setStompClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const sender = "test_member" // 멤버 기능 추가 되면 가져오기 & 중복검사 하는 함수 추가
  const { room_id } = useParams();
  const subscribeUrl = `http:localhost:8080/topic`;
  const publishUrl = `http:localhost:8080/app/chat`;
  useEffect(() => {
    // WebSocket 연결 설정
   
    const stompClient = new Client({
        brokerURL: `ws://localhost:8080/ws-stomp`
      });

    stompClient.webSocketFactory = function() {
    
        return new SockJS(`http://localhost:8080/ws-stomp`)
};
     // stompClient.activate();
    
    stompClient.onConnect = () => {
        console.log("stomp-websoket is connect!")
      // room_id를 pathVariable로 가지고 있는 토픽에 구독
      stompClient.subscribe(subscribeUrl, (message) => {
        const msgBody = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, msgBody]);
        console.log("setMessages:",setMessages)
        console.log("msgBody:",msgBody)
      });

      // 입장 메시지 전송
      const enterMessage = {
        type: 'ENTER',
        sender: 'User',
        //message: `${sender}님이 입장했습니다.`, =>  back에서 구현
        room_id: room_id,
      };

      stompClient.publish({ 
        destination: publishUrl,
        body: JSON.stringify(enterMessage)
      });
      console.log(JSON.stringify(enterMessage))
      setStompClient(stompClient);
    };

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [room_id]);

  const handleSend = () => {
    // 메시지 전송
    const chatMessage = {
      type: 'TALK',
      sender: 'User',
      message: newMessage,
      room_id: room_id,
    };

    if (chatMessage.message === ''){
      alert("메세지를 입력해주세요.")
    }
    else{
    stompClient.publish({ 
        destination: publishUrl,
        body: JSON.stringify(chatMessage)
      });
    console.log(JSON.stringify(chatMessage))
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
export default ChatRoom;
