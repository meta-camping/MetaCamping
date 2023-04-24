import React from 'react';
import {Modal, Button} from "react-bootstrap";
import CampingMap from './CampingMap';
import axios from 'axios';
import { useNavigate,useLocation } from 'react-router-dom';
import { useRecoilState } from "recoil";
import { userState } from "../recoil/user";

function InfoModal({ info, show, handleClose }) {

    const [user,setUser] = useRecoilState(userState);
    console.log(user.nickname)
    const username = user.nickname
    const navigate = useNavigate();
    const [roomId,setRoomId] = useState('');

    const inToChatRoom = () => {
        
        //'채팅방 입장하기' 버튼 클릭 시
        //방 존재 유무(=info.roomId 변수로 roomId에 저장되어서 모달이 열림) 확인 후 없으면 생성 / 있으면 입장
        if (info.roomId === '' || undefined){
            
            axios.post(`/chat/create`, {
                locationX: info.campingCoordinateX,
                locationY: info.campingCoordinateY,
                roomName: info.campingName
              })

              .then(res => {
                const roomId = res.data.roomId
                navigate(`/chat/room/${roomId}`, {state: {
                    roomName: info.campingName,
                    userCheck: "가능"
                    } });
              })
              
              .catch(err => {
                console.error(err);
              });

            }else{
                axios.post("/chat/room/user-check", {
                    roomId: info.roomId,
                    memberId: username})

                    .then(res => {
                        const userCheck= res.data
                        navigate(`/chat/room/${info.roomId}`,{ state : { 
                            roomName: info.campingName,
                            userCheck: userCheck
                        } })
                    })

                    .catch(err =>{
                        console.log(err)
                    })

                
                
            }

        }
    return (
        <Modal show={show} onHide={handleClose} size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
            <Modal.Header closeButton style={{backgroundColor: "#0D6EFE",color: "#FFFFFF"}}>
                <Modal.Title style={{fontWeight: "bold",marginLeft: "Auto"}}>
                        {info.campingName}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <CampingMap lan={info.campingCoordinateX} lng={info.campingCoordinateY} campingAddress={info.campingAddress}/>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="primary" onClick={inToChatRoom}>
                    채팅방 입장하기
                </Button>
            </Modal.Footer>
        </Modal>
    )
}
export default InfoModal;