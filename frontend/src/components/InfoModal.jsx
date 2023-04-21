import React from 'react';
import {Modal, Button} from "react-bootstrap";
import CampingMap from './CampingMap';


function InfoModal({ info, show, handleClose }) {

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
                <Button variant="primary" onClick={handleClose}>
                    나중에 수정
                </Button>
            </Modal.Footer>
        </Modal>
    )
}
export default InfoModal;