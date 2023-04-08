import React, { useState, useEffect } from 'react';
import logo from "../images/notion.png";
import finedust2 from "../images/dust/finedust.png";
import ultrafinedust2 from "../images/dust/ultrafinedust.png";
import axios from "axios";

function SelectMap() {
    const [map, setMap] = useState(null);
    const [marker, setMarker] = useState(null);

    const [sidoName, setSidoName] = useState(''); //도 이름
    const [sggName, setSggName] = useState(''); // 시 이름
    const [stationName, setStationName] = useState(''); // 지역 이름
    const [umdName, setUmdName] = useState(''); // 동 이름 << stationName과 값은 같은데 근처 측정소 찾는 api에서 필요한 값

    //마지막 데이터베이스에 찾아오는 지역 이름(측정소 이름)
    const [measuringStation, setMeasuringStation] = useState('');

    const [tmx,setTmx] = useState('');
    const [tmy,setTmy] = useState('');

    //현재 위치 가져와서 미세먼지 조회
    const [name, setName] = useState("--");
    const [pm10Value, setPm10Value] = useState("--");
    const [pm10Grade, setpm10Grade] = useState("--");
    const [pm25Value, setPm25Value] = useState("--");
    const [pm25Grade, setpm25Grade] = useState("--");

    useEffect(() => {
        const mapOption = {
            center: new window.kakao.maps.LatLng(37.48370806396488,127.01200750943013),
            level: 10
        };
        const map = new window.kakao.maps.Map(
            document.getElementById("map2"),
            mapOption
        );
        const marker = new window.kakao.maps.Marker({
            position: map.getCenter(),
        });
        marker.setMap(map);

        setMap(map);
        setMarker(marker);

        window.kakao.maps.event.addListener(map, "click", function (mouseEvent) {
            const latlng = mouseEvent.latLng;

            marker.setPosition(latlng);
            const geocoder = new window.kakao.maps.services.Geocoder();

            geocoder.coord2RegionCode(latlng.getLng(), latlng.getLat(), (result, status) => {
                if (status === window.kakao.maps.services.Status.OK) {
                    if (result.length > 0) {

                        console.log(result)
                        const roadAddress = result[0].address_name;
                        const address = result[1].address_name;
                        const sidoname = result[0].region_1depth_name;
                        const sggName = result[0].region_2depth_name;
                        const stationname = result[0].region_3depth_name;

                        console.log(`2번째 지도 법정동 주소: ${roadAddress}`);
                        console.log(`2번째 지도 행정동 주소: ${address}`);
                        console.log(`2번째 지도 시 이름: ${sidoname}`)
                        console.log(`2번째 지도 구 이름: ${sggName}`)
                        console.log(`2번째 지도 지역 이름: ${stationname}`)

                        if(result[0].region_3depth_name==="송도동"){
                            setSidoName(result[0].region_1depth_name);
                            setStationName("송도"); //송도동으로 검색하면 포항 송도동만 나와서 예외처리
                            if(result[1].region_3depth_name==="송도4동"||result[1].region_3depth_name==="송도5동"){ //송도4동, 송도5동은 근처 측정소 api 파라미터로 줄 수 없음
                                setUmdName("송도2동"); //송도4동, 송도5동과 제일 가까운게 송도2동
                                setSggName(result[0].region_2depth_name)
                            } else {
                                setUmdName(result[1].region_3depth_name); //송도1동, 송도2동, 송도3동
                                setSggName(result[0].region_2depth_name)
                            }
                        }else {
                            setSidoName(result[0].region_1depth_name);
                            setSggName(result[0].region_2depth_name);
                            setUmdName(result[0].region_3depth_name);
                            setStationName(result[0].region_3depth_name);
                        }
                    }
                }
            });
        });
    }, [sidoName, sggName, stationName]);

    //좌표를 가지고 TM_X TM_Y 좌표로 변환
    useEffect(() => {
        axios.get('/api/dust/convert', {
            params: {
                station_name: stationName
            }
        })
            .then((response) => {
                console.log(response.data.result.response.body.items);
                console.log("도시이름: " + sidoName + "(좌표 후보와 일치하는 데이터 선택)");
                console.log("시구이름: " + sggName + "(좌표 후보와 일치하는 데이터 선택)");
                console.log("지역이름: " + stationName + "(좌표 후보와 일치하는 데이터 선택)");
                //결과에서 현재 내 위치에 맞는 tmX, tmY 좌표를 선택(왜냐하면 같은 읍면동 이름을 같는 도시가 존재)
                if(response.data.result.response.body.items===[]){
                    alert("데이터가 없습니다.")
                } else {
                    for(var i=0; i<response.data.result.response.body.items.length;i++){
                        console.log("tmX,tmY 좌표 후보: " + response.data.result.response.body.items[i].sidoName)
                        if((response.data.result.response.body.items[i].sidoName===sidoName)&&(response.data.result.response.body.items[i].sggName===sggName)){
                            setTmx(response.data.result.response.body.items[i].tmX);
                            setTmy(response.data.result.response.body.items[i].tmY);
                        }
                    }
                }
            }).catch(error => console.log("tmX,tmY 좌표변환 api 오류."))
    },[sidoName, sggName, stationName])
    //tmX, tmY 저장후 측정소 이름 조회
    useEffect(() => {
        axios.get('/api/dust/search/measuringstation', {
            params: {
                tmX: tmx,
                tmY: tmy
            }
        })
            .then((response) => {
                console.log("tmx: " + tmx +"/tmy: " + tmy)
                console.log("측정소 기준: " + response.data.result.response.body.items[0].stationName);
                    setMeasuringStation(response.data.result.response.body.items[0].stationName)
            })
    },[tmx,tmy])

    //측정소 이름으로 미세먼지 데이터 조회
    useEffect(() => {
        axios.get('/api/dust/searchStation', {
            params: {
                station_name: measuringStation
            }
        })
            .then((response) => {
                    setName(stationName);
                    setPm10Value(response.data[0].pm10Value);
                    setPm25Value(response.data[0].pm25Value);
                    if(response.data[0].pm10Grade==="1") {
                        setpm10Grade("좋음")
                    }else if (response.data[0].pm10Grade==="2") {
                        setpm10Grade("보통")
                    }else if (response.data[0].pm10Grade==="3") {
                        setpm10Grade("나쁨")
                    }else if (response.data[0].pm10Grade==="4") {
                        setpm10Grade("매우나쁨")
                    }else {
                        setpm10Grade("통신오류")
                    }
                    if(response.data[0].pm25Grade==="1") {
                        setpm25Grade("좋음")
                    }else if (response.data[0].pm25Grade==="2") {
                        setpm25Grade("보통")
                    }else if (response.data[0].pm25Grade==="3") {
                        setpm25Grade("나쁨")
                    }else if (response.data[0].pm25Grade==="4") {
                        setpm25Grade("매우나쁨")
                    }else {
                        setpm25Grade("통신오류")
                    }
            }).catch(error => console.log(error))
    },[measuringStation])

    return (
        <div style={{marginLeft: "80px"}}>
            <div id="map2" style={{
                width: '400px',
                height: '300px'
            }}></div>
            도시이름: <input value={sidoName} readOnly/><br/>
            지역이름: <input value={stationName} readOnly/>
            <div className="api_title">
                미세먼지·초미세먼지
                <span style={{margin: "0px 5px"}}>|</span>
                <span style={{marginRight: "10px", color: "#b6b6b6"}}>{name} 기준</span><img src={logo} alt="미세먼지 안내" width="20" height="20"/>
            </div>
            <div></div>
            <div className="api_content" style={{display: "flex", flexDirection: "row"}}>
                <div
                    style={{display: "flex", width: "50%", justifyContent: "center", alignItems: "center", flexDirection: "column"}}>
                    <img src={finedust2} alt="미세먼지" width="55" height="55"/>
                    <span>{pm10Value}㎍/㎥</span>
                    <span style={{fontSize: "15px", margin: "2px 0px"}}>{pm10Grade}</span>
                </div>
                <div style={{border: "1px solid rgb(221, 221, 221)", backgroundColor: "rgb(221, 221, 221)"}}></div>
                <div
                    style={{display: "flex", width: "50%", justifyContent: "center", alignItems: "center", flexDirection: "column"}}>
                    <img src={ultrafinedust2} alt="초미세먼지" width="60" height="60"/>
                    <span>{pm25Value}㎍/㎥</span>
                    <span style={{fontSize: "15px", margin: "2px 0px"}}>{pm25Grade}</span>
                </div>
            </div>
        </div>
    )
}

export default SelectMap;