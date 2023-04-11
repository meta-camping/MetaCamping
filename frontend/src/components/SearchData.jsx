import React, {useState} from 'react';
import axios from 'axios';
import InfoModal from "./InfoModal";

function SearchData() {
    const [camping, setCamping] = useState([]);
    const [city, setCity] = useState("강원도");

    const [modalHandle,setModalHandle] = useState(false);
    const [selectedInfo, setSelectedInfo] = useState({location:'', position:''});


    const handleSelectCity = (e) => {
        setCity(e.target.value);
    };
    const showAllList = () => {
        axios.get('http://localhost:8080/api/camping/showAllList')
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
        <div style={{flexDirection: "row", marginLeft: "100px"}}>
            <form className="container" >
                <div className="mb-3" style={{display: "flex", marginTop: "30px"}}>
                    <label className="form-label" style={{lineHeight: "40px"}}>전체 캠핑장 조회</label><br/>
                    <button type="button" className="btn btn-primary" onClick={showAllList} style={{width: "80px", height: "40px", marginLeft: "20px"}}>Submit</button><br/>
                    <label className="form-label" style={{marginLeft: "30px", lineHeight: "40px", marginRight: "10px"}}>시별로 검색</label>
                    <table>
                        <tbody>
                        <td>
                            <select id="div_apv_sq" className="form-control" onChange={handleSelectCity}>
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
                        <button type="button" className="btn btn-primary" onClick={SelectCity} style={{width: "80px", height: "40px", marginLeft: "20px"}}>Submit</button>
                        </tbody>
                    </table>
                </div>
            </form>

            <table className="table">
                <thead>
                <tr>
                    <th scope="col">번호</th>
                    <th scope="col">주소</th>
                    <th scope="col">이름</th>
                    <th scope="col">위도</th>
                    <th scope="col">경도</th>
                </tr>
                </thead>
                {camping.map(prop => {
                    return (
                        <tbody key={prop.id}>
                        <tr>
                            <td>{prop.num}</td>
                            <td><a onClick = {() => openModal(prop.name, prop.address, prop.wgs84_x, prop.wgs84_y)}>{prop.address}</a></td>
                            <td><a onClick = {() => openModal(prop.name, prop.address, prop.wgs84_x, prop.wgs84_y)}>{prop.name}</a></td>
                            <td>{prop.wgs84_x}</td>
                            <td>{prop.wgs84_y}</td>
                        </tr>
                        </tbody>
                    )
                })}
            </table>
            {
                modalHandle && <InfoModal info={selectedInfo} show={modalHandle} handleClose={() => setModalHandle(false)} />

            }
            </div>
    )
}
export default SearchData;
