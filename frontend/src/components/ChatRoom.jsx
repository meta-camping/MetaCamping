import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import axios from 'axios';
import Stomp from 'stompjs';
import '../styles/ChatRoom.css';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import moment from 'moment';
import { useRecoilState } from "recoil";
import { userState } from "../recoil/user";
import { locationState } from "../recoil/location";
import { tokenState } from '../recoil/token';
import Button from 'react-bootstrap/Button';
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import Offcanvas from 'react-bootstrap/Offcanvas';

function ChatRoom() {
  const { roomId } = useParams();
  const location = useLocation();
  const [userCheck,setUserCheck] = useState(`${location.state?.userCheck}`);
  const [userList, setUserList] =useState([]); //userList.memberId = 닉네임들만 추출!
  const [stompClient, setStompClient] = useState(null);
  const [roomInfo,setRoomInfo]= useState({});
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [connected, setConnected] = useState(false);
  const subscribeUrl = `/topic/${roomId}`;
  const publishUrl = `/app/chat/${roomId}`;

  //멤버
  const [user,setUser] = useRecoilState(userState);
  const [token,setToken] = useRecoilState(tokenState);
  const username = user.nickname
  const navigate = useNavigate();
  const messageEndRef = useRef(null);


  //유효성 검사
  const [isDisabled, setIsDisabled] = useState(false);

  //유저 현재 위치 from Map
  const [nowLocation,setNowLocation] = useRecoilState(locationState);

  //유저리스트 UI 관련
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);


  useEffect(() => {

    messageEndRef.current.scrollIntoView({ behavior: 'smooth' });
    console.log(nowLocation)

    //roomInfo 받아오기
    axios.get(`/api/chat/room/${roomId}`)
      .then((res) => {
        if (res.data) {
          setRoomInfo(res.data);
        }
      })
      .catch((error) => {
        console.error(error);
      });

    //user-List 받아오기
    axios.get(`/api/chat/room/${roomId}/user-list`)
    .then((res) =>{
      if(res.data){
        console.log(res.data)
        setUserList(res.data)
      }
    })


   
    //채팅(웹소켓) 접속 설정
    const socket = new SockJS(`http://127.0.0.1:8080/ws-stomp`);
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
          // 채팅방 입장 전 해당 유저의 채팅방 기참여 여부를 userCheck에 담아 들고 온다.
          // userCheck가 기존에 구독 유저가 아니라면 ENTER type 메세지 자동 전송한다.
          stompClient.send(publishUrl, {}, JSON.stringify({ 
            roomId: roomId,
            type: 'ENTER',
            sender: username,
            message: username+'님이 입장했습니다.',
            locationX:nowLocation.latitude,
            locationY:nowLocation.longitude,
            createdTime: moment().format('YYYY-MM-DD HH:mm:ss')
          })).then(() => {
            //Enter type 메세지가 전송되면 해당 채팅방의 userList 유저 정보가 편입 된다.
            //따라서 변경된 userList에서 다시 한번 user-check를 수행해 상태를 업데이트 한다.
            axios.post(`/api/chat/room/${roomInfo.roomId}/${username}/$user-check`).then(res => {
              setUserCheck(res.data)
              console.log("업데이트 된",userCheck)
            }).catch(err => {
              console.log(err)
            });
          }).catch(err => {
            console.log(err);
          });
        }
        if (userCheck === "구독 유저") {
          axios.get(`/api/chat/room//${roomId}/${username}/before-messages`)
          .then(res => {
            console.log(res.data)
            const previousMessages = res.data;
            setMessages([...previousMessages, ...messages]);
          })
        }
      });
    }else{
      alert("유효하지 않은 접근입니다.")
      navigate("/")
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
    axios.delete(`/api/chat/room/${roomId}/${username}/out`)
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
      createdTime: moment().format('YYYY-MM-DD HH:mm:ss'),
      locationX:nowLocation.latitude,
      locationY:nowLocation.longitude,
      message: newMessage }));

    setNewMessage('');
  
};

const uniqueSenders = new Set();
messages.forEach((msg) => uniqueSenders.add(msg.sender));
return (
  <>
  <h1><strong>{roomInfo.roomName}</strong></h1>
  <div>
    <Button className="user-list" variant="primary" onClick={handleShow}>채팅방 참여 유저</Button>
    <Offcanvas show={show} onHide={handleClose} placement="end">
      <Offcanvas.Header closeButton>
        <Offcanvas.Title><h2><strong>{roomInfo.roomName}</strong></h2></Offcanvas.Title>
      </Offcanvas.Header>
      <Offcanvas.Body>
        <ul>
          <br />
        <h3>참여 유저</h3>
          {userList.map((user, index) => (
            <li key={index}>
              {user.memberId}
            </li>
          ))}
        </ul>
      </Offcanvas.Body>
    </Offcanvas>
  </div>

    <div>
      {messages.map((msg, idx) => (
        <div key={idx} className={`chat-bubble ${msg.sender === username ? 'self' : 'others'}`}>
          <span>
            <strong>{msg.sender}</strong> {msg.nearOrNot && '⛺'}| {new Date(msg.createdTime).toLocaleString()}
          </span>
          <div className="bubble">
            <span>{msg.message}</span>
          </div>
        </div>
      ))}
      <div ref={messageEndRef}></div>
    </div>

    <div className="chat-input">
  <input
    type="text"
    value={newMessage}
    onChange={(e) => setNewMessage(e.target.value)}
    onKeyDown={(e) => {
      if (e.key === 'Enter'&&newMessage.trim() !== '') {
        handleSend();
      }
    }}
  />
  <button onClick={handleSend} disabled={newMessage.trim()===''}>Send</button>
</div>
    
    <button className="exit-button" onClick={exit}>채팅방 나가기</button>

</>
);
}

export default ChatRoom;