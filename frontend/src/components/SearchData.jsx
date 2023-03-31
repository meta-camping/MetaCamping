import React, {useState} from 'react';
import axios from 'axios';

function SearchData() {
    const [dust, setDust] = useState([]);
    const [city, setCity] = useState("서울");

    const handleSelectCity = (e) => {
        setCity(e.target.value);
    };
    const showAllList = () => {
        axios.get('api/dust/showAllList')
            .then((response) => {
                setDust(response.data)
            }).catch(error => console.log(error))
    }
    const SelectCity = () => {
        axios.get('/api/dust/showList', {
            params: {
                sido_name: city
            }
        })
            .then((response) => {
                setDust(response.data)
            }).catch(error => console.log(error))
    }

    return (
        <div style={{flexDirection: "row", marginLeft: "100px"}}>
            <form className="container" >
                <div className="mb-3" style={{display: "flex", marginTop: "30px"}}>
                    <label className="form-label" style={{lineHeight: "40px"}}>전체데이터 조회</label><br/>
                    <button type="button" className="btn btn-primary" onClick={showAllList} style={{width: "80px", height: "40px", marginLeft: "20px"}}>Submit</button><br/>
                    <label className="form-label" style={{marginLeft: "30px", lineHeight: "40px", marginRight: "10px"}}>도시(데이터검색)</label>
                    <table>
                        <tbody>
                        <td>
                            <select id="div_apv_sq" className="form-control" onChange={handleSelectCity}>
                                <option value="서울">서울</option>
                                <option value="부산">부산</option>
                                <option value="대구">대구</option>
                                <option value="인천">인천</option>
                                <option value="광주">광주</option>
                                <option value="대전">대전</option>
                                <option value="울산">울산</option>
                                <option value="경기">경기</option>
                                <option value="강원">강원</option>
                                <option value="충북">충북</option>
                                <option value="충남">충남</option>
                                <option value="전북">전북</option>
                                <option value="전남">전남</option>
                                <option value="경북">경북</option>
                                <option value="경남">경남</option>
                                <option value="제주">제주</option>
                                <option value="세종">세종</option>
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
                    <th scope="col">ID</th>
                    <th scope="col">도시</th>
                    <th scope="col">지역</th>
                    <th scope="col">미세먼지 농도</th>
                    <th scope="col">미세먼지 등급</th>
                    <th scope="col">초미세먼지 농도</th>
                    <th scope="col">초미세먼지 등급</th>
                </tr>
                </thead>
                {dust.map(prop => {
                    return (
                        <tbody key={prop.id}>
                        <tr>
                            <td>{prop.id}</td>
                            <td>{prop.sido_name}</td>
                            <td>{prop.station_name}</td>
                            <td>{prop.pm10Value}</td>
                            <td>{prop.pm10Grade}</td>
                            <td>{prop.pm25Value}</td>
                            <td>{prop.pm25Grade}</td>
                        </tr>
                        </tbody>
                    )
                })}
            </table>
        </div>
    )
}
export default SearchData;
