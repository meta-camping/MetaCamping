// 사용자가 로그인한 후, 해당 사용자가 가입한 채팅방 리스트를 가져옵니다.
import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import axios from 'axios';
import Stomp from 'stompjs';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import ChatRoom from './ChatRoom';

const stompClient = ChatRoom.stompClient
const [messages,setMessages] = ChatRoom.messages;

axios.get("http://localhost:8080/chat/room/user-list")
  .then(res => {
    const userRooms = res.data;
    
    // 이전에 해당 사용자가 구독한 채팅방 정보를 불러옵니다.
    const subscribedRooms = JSON.parse(localStorage.getItem('subscribedRooms')) || [];

    // 이전에 구독한 채팅방을 다시 구독 = 화면이 언마운트 되거나 로그인이 풀려도 구독 유지할 수 있게 됨
    userRooms.forEach(room => {
      if (subscribedRooms.includes(room.room_id)) {
        const subscribeUrl = `/topic/${room.room_id}`;
        stompClient.subscribe(subscribeUrl, function (greeting) {
          setMessages((messages) => [...messages, JSON.parse(greeting.body)]);
        });
      }
    });

  })
  .catch(
      error => console.error(error)
  );