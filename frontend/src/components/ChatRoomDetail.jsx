import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ChatRoomDetail = ({ match }) => {
  const [room, setRoom] = useState(null);

  useEffect(() => {
    const fetchRoom = async () => {
      const response = await axios.get(`http://localhost:8080/chat/room/${match.params.room_id}`,{headers: {
        'Accept': 'application/json'
      }
    });
      setRoom(response.data);
      console.log(response.data);
    };

    fetchRoom();
  }, [match.params.room_id]);

  if (!room) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h2>{room.room_name}</h2>
      {/* 채팅방 화면 구성을 여기에 추가하십시오 */}
    </div>
  );
};

export default ChatRoomDetail;