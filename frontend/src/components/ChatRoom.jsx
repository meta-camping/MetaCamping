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

  /**
   * 이전 경로에서 넘어오는 데이터
   */
  const { roomId } = useParams(); //채팅방 UUID
  const location = useLocation(); 
  const [userCheck,setUserCheck] = useState(`${location.state?.userCheck}`); //채팅방 입장 전, 회원의 채팅방 기참여여부
  const prevUserCheckRef = useRef('');

  // useEffect(() => {
  //   prevUserCheckRef.current = userCheck;
  // }, [userCheck]);
  
  /**
   * Recoil에 담긴 데이터 (유저 관련)
   */
  const [user,setUser] = useRecoilState(userState); //유저 정보
  const [token,setToken] = useRecoilState(tokenState); //유저 Authentication
  const [nowLocation,setNowLocation] = useRecoilState(locationState); //유저 현재 위치
  const username = user.nickname
  const navigate = useNavigate();
  
  /**
   * DB에서 조회해오는 데이터
   */
  const [userList, setUserList] =useState([]); //유저리스트
  const [roomInfo,setRoomInfo]= useState({}); //채팅방 정보 (roomId, roomName, locationX,locationY ...)

  /**
   * 웹소켓 연결 관련 변수
   */
  const [stompClient, setStompClient] = useState(null); // 채팅 서버 연결 정보
  const [connected, setConnected] = useState(false); //연결 여부를 확인
  const subscribeUrl = `/topic/${roomId}`; //접속 시 채팅 서버 연결 경로
  const publishUrl = `/app/chat/${roomId}`; // 메세지 보내는 경로

  /**
   * 화면에서 비동기적으로 메세지를 처리하는 변수들
   */
  const [messages, setMessages] = useState([]); //채팅방 내 메세지들을 담는 변수
  const [newMessage, setNewMessage] = useState(''); //새로운 메세지를 입력하는 변수
  
  /**
   * UI 관련
   */
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const messageEndRef = useRef(null);


  useEffect(() => {

    //새로운 메세지가 발행되면 화면을 아래로 내려줌
    messageEndRef.current.scrollIntoView({ behavior: 'smooth' });

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
            locationX: nowLocation.latitude,
            locationY: nowLocation.longitude,
            createdTime: moment().format('YYYY-MM-DD HH:mm:ss')
          }), () => {
            // Enter type 메세지가 전송되면 해당 userList 유저 정보가 DB에 편입된다.
            // 따라서 변경된 userList에서 다시 한번 user-check를 수행해 상태를 업데이트한다.
            axios.get(`/api/chat/room/${roomInfo.roomId}/${username}/user-check`)
              .then(res => {
                console.log(res.data);
                setUserCheck(res.data);
                console.log("업데이트 된",userCheck);
              })
              .catch(err => {
                console.log(err);
              });
          });      
        }
        if (userCheck === "구독 유저") {
          axios.get(`/api/chat/room/${roomId}/${username}/before-messages`)
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