import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, InputGroup, FormControl, ListGroup } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from "react-router-dom";

function ChatRoomList() {

    const token = localStorage.getItem("key");
    const navigate = useNavigate();

    const [chatRooms, setChatRooms] = useState([]);
    const [camping,setCamping] = useState([]);

    useEffect(() => {    
        axios.get("http://localhost:8080/chat/list")
        .then(res => {
          console.log(res.data);
          setChatRooms(res.data);
        })
        .catch(
            error => console.error(error)
            )    
      }, []);

    //-----------------채팅방 상세 화면으로 넘어가는 함수----------------

      const handleClick = (room_id) => {
        navigate(`/chat/room/${room_id}`)
      };

     //------------------------ 채팅방 생성 ----------------
     /* 
     검색창 만들기! 
     검색 : DB에서 캠핑장 정보확인 
     검색 내용 있으면 => 백단에서 DB로부터 캠핑장 이름, x좌표, y좌표 채팅방DTO에 설정하고 채킹방 생성 and 채팅방 입장
     검색 내용 없으면 => 캠핑장 정보가 없음을 알림
     */

     const SearchCamp = () => {

        useEffect(() => {    
            axios.get("http://localhost:8080/chat/is-camping")
            .then(res => {
              console.log(res.data);
              setCamping(res.data);
            })
            .catch(
                error => console.error(error)
                )    
          }, []);

     }
       
        const addChatRoom = async () => {
            if(token == null){
                alert('로그인 후 글을 작성해주세요.')
            }
            else{
            //글 등록버튼을 누르면 일단 유저인지 확인해야함. 세션에 있는 어떤걸 기준으로 인증을 할건지??
            alert("캠핑장 검색 기능 구현되면 코드 수정. 지금은 postman으로만 생성 확인")
            //navigate('/')
            }
        };
    
    return (
        <>

    <div>
        <h2 className="text-center" style={{marginBottom: "30px"}}>채팅방</h2>
        <div className="row">
            <table className="table table-striped table-bordered">
                <thead>
                    <tr style={{fontSize: "20px"}}>
                        <th> 캠핑장 </th>
                        <th> 최근 채팅 시간 </th>
                    </tr>
                </thead>
                <tbody>
                    {
                        //map 함수를 이용해서 백엔드에서 받아온 posts의 데이터를 출력한다.
                        chatRooms.map(
                            chatRooms =>
                        <tr key = {chatRooms.room_id} onClick={() => handleClick(chatRooms.room_id)}>
                            <td>{chatRooms.room_name}</td>
                            <td>{chatRooms.updated_time}</td>
                            </tr>
                        )
                    }
                </tbody>
            </table>
        </div>
        <span> {chatRooms.length}개의 채팅방이 있습니다.</span>
        <Button style={{float: "right"}} onClick={addChatRoom}>채팅방 생성</Button>
    </div>




    </>
    )
}

export default ChatRoomList;