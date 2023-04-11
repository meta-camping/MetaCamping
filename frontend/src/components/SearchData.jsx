import React, {useState} from 'react';
import axios from 'axios';
import InfoModal from "./InfoModal";
import {Button} from 'react-bootstrap';
import search from "../images/search.png"
import locationImage from "../images/location.gif";

function SearchData() {
    const [camping, setCamping] = useState([]);
    const [city, setCity] = useState("강원도");

    const [modalHandle,setModalHandle] = useState(false);
    const [selectedInfo, setSelectedInfo] = useState({location:'', position:''});


    const handleSelectCity = (e) => {
        setCity(e.target.value);
    };
    const showAllList = () => {
        axios.get('/api/camping/showAllList')
            .then((response) => {
                setCamping(response.data)
            }).catch(error => console.log(error))
    }
    const SelectCity = () => {
        axios.get('http://localhost:8080/api/camping/showList', {
            params: {
                city_name: city
            }
        })
            .then((response) => {
                setCamping(response.data)
            }).catch(error => console.log(error))
    }

    const openModal = (campingName, campingAddress, campingCoordinateX, campingCoordinateY) => {
        setSelectedInfo({campingName: campingName, campingAddress: campingAddress,
            campingCoordinateX: campingCoordinateX, campingCoordinateY: campingCoordinateY});
        setModalHandle(true);
    };

    return (
        <div style={{marginLeft: "100px"}}>
            <form>
                <div className="mb-3 search" style={{display: "flex"}}>
                    <Button type="button" className="mb-3 search" onClick={showAllList} style={{fontSize:"20px",width: "200px",marginBottom: "20px"}}>
                        전체 캠핑장 조회
                        <img src={search} alt="검색" width="25" height="25" style={{float: "right"}}/>
                    </Button><br/>
                    <div style={{display: "flex", marginLeft: "280px"}}>
                        <table>
                            <tbody>
                            <td>
                                <select className="form-control" style={{fontSize:"20px", width: "140px", marginBottom: "20px"}} onChange={handleSelectCity}>
                                    <option value="강원도">강원도</option>
                                    <option value="경기도">경기도</option>
                                    <option value="경상북도">경상북도</option>
                                    <option value="경상남도">경상남도</option>
                                    <option value="광주광역시">광주광역시</option>
                                    <option value="대구광역시">대구광역시</option>
                                    <option value="대전광역시">대전광역시</option>
                                    <option value="부산광역시">부산광역시</option>
                                    <option value="서울특별시">서울특별시</option>
                                    <option value="세종특별자치시">세종특별자치시</option>
                                    <option value="울산광역시">울산광역시</option>
                                    <option value="인천광역시">인천광역시</option>
                                    <option value="전라북도">전라북도</option>
                                    <option value="전라남도">전라남도</option>
                                    <option value="제주특별자치도">제주특별자치도</option>
                                    <option value="충청북도">충청북도</option>
                                    <option value="충청남도">충청남도</option>
                                </select>
                            </td>
                            </tbody>
                        </table>
                        <Button type="button" onClick={SelectCity} style={{fontSize:"20px",width: "170px",marginLeft: "20px", marginBottom: "20px"}}>
                            시별로 조회
                            <img src={search} alt="검색"width="25" height="25" style={{float: "right"}}/>
                        </Button>
                    </div>
                </div>
            </form>

            <table className="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>번호</th>
                    <th>주소</th>
                    <th>이름</th>
                    <th>위도</th>
                    <th>경도</th>
                </tr>
                </thead>
                <tbody>
                {camping.map(
                    prop =>
                        <tr key={prop.id}>
                            <td>{prop.num}</td>
                            <td><a onClick = {() => openModal(prop.name, prop.address, prop.wgs84_x, prop.wgs84_y)}>{prop.address}</a></td>
                            <td><a onClick = {() => openModal(prop.name, prop.address, prop.wgs84_x, prop.wgs84_y)}>{prop.name}</a></td>
                            <td>{prop.wgs84_x}</td>
                            <td>{prop.wgs84_y}</td>
                        </tr>
                )}
                </tbody>
            </table>
            {
                modalHandle && <InfoModal info={selectedInfo} show={modalHandle} handleClose={() => setModalHandle(false)} />

            }
            </div>
    )
}
export default SearchData;
