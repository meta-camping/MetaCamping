import React, { useState, useEffect } from 'react';
import Weather from "./Weather";
import Dust from "./Dust";
const { kakao } = window;

function CampingMap({lan, lng}) {
    const [map, setMap] = useState('');
    const [location, setLocation] = useState({ latitude: '', longitude: ''}); // 위도, 경도

    const [locationData, setLocationData] = useState({
        addressName: '', // 현재 주소
        sidoName: '', // 도 이름
        sggName: '', // 시 이름
        stationName: '', // 지역 이름
        umdName: '' // 동 이름 << stationName과 값은 같은데 근처 측정소 찾는 api에서 필요한 값
    });

    // 지도가 준비된 후에 마커와 인포윈도우를 표시합니다
    useEffect(() => {
        if (map && location.latitude && location.longitude) {
            const locPosition = new kakao.maps.LatLng(
                location.latitude,
                location.longitude
            );
            displayMarker(locPosition);
        }
    }, [map, location]);

    // 지도에 마커와 인포윈도우를 표시하는 함수입니다
    const displayMarker = (locPosition) => {
        new kakao.maps.Marker({
            map: map,
            position: locPosition
        });

        // 지도 중심좌표를 접속위치로 변경합니다
        map.setCenter(locPosition);
    }

    useEffect(() => {
        const mapOption = {
            center: new window.kakao.maps.LatLng(33.450701,126.570667),
            level: 5
        };
        const map = new window.kakao.maps.Map(
            document.getElementById("map1"),
            mapOption
        );
        const marker = new window.kakao.maps.Marker({
            position: map.getCenter(),
        });
        marker.setMap(map);
        setMap(map);

        setLocation({
            latitude: lan,
            longitude: lng
        });
    }, []);

    useEffect(() => {
        const geocoder = new window.kakao.maps.services.Geocoder();

        const latlng = new window.kakao.maps.LatLng(location.latitude, location.longitude);
        geocoder.coord2RegionCode(latlng.getLng(), latlng.getLat(), (result, status) => {
            if (status === window.kakao.maps.services.Status.OK) {
                if (result.length > 0) {
                    setLocationData(prevState => ({...prevState, addressName: result[1].address_name}))

                    if(result[0].region_3depth_name==="송도동"){
                        setLocationData(prevState => ({...prevState, sidoName: result[0].region_1depth_name}))
                        setLocationData(prevState => ({...prevState, stationName: "송도"})) //송도동으로 검색하면 포항 송도동만 나와서 예외처리

                        if(result[1].region_3depth_name==="송도4동"||result[1].region_3depth_name==="송도5동"){ //송도4동, 송도5동은 근처 측정소 api 파라미터로 줄 수 없음
                            setLocationData(prevState => ({...prevState, umdName: "송도2동"}))
                            setLocationData(prevState => ({...prevState, sggName: result[0].region_2depth_name}))
                        } else {
                            setLocationData(prevState => ({...prevState, umdName: result[1].region_3depth_name})) //송도1동, 송도2동, 송도3동
                            setLocationData(prevState => ({...prevState, sggName: result[0].region_2depth_name}))
                        }
                    }else {
                        setLocationData(prevState => ({...prevState, sidoName: result[0].region_1depth_name}))
                        setLocationData(prevState => ({...prevState, stationName: result[0].region_3depth_name}))
                        setLocationData(prevState => ({...prevState, umdName: result[0].region_3depth_name}))
                        setLocationData(prevState => ({...prevState, sggName: result[0].region_2depth_name}))
                    }
                }
            }
        });
    }, [location]);

    return (
        <div style={{display: "flex"}}>
            <div style={{ marginRight: "30px"}}>
                <div id="map1" style=
                    {{width: "350px", height: "250px", border: "1px solid rgb(207, 207, 207)", borderRadius: "30px" , marginBottom: "20px",
                        boxShadow: "rgb(207, 207, 207) 0px 0px 13px", position: "relative", overflow: "hidden", left: "10px"}}>
                </div>
                <Dust sidoName={locationData.sidoName} stationName={locationData.stationName} umdName={locationData.umdName} sggName={locationData.sggName}/>
            </div>
            <Weather addressName={locationData.addressName} location={location}/>
        </div>
    )
}

export default CampingMap;