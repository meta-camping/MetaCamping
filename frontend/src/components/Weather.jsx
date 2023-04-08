import React, { useState, useEffect } from 'react';
import ApiService from "../services/ApiService";
import locationImage from "../images/location.gif"
import sunny from "../images/weather/sunny.gif";
import manycloud from "../images/weather/manycloud.gif";
import cloud from "../images/weather/cloud.gif"
import snow from "../images/weather/snow.gif"
import snowrain from "../images/weather/snowrain.gif"
import shower from "../images/weather/shower.gif"
import rain from "../images/weather/rain.gif"

function Weather(props) {
    //현재 시간 날씨 예보에 쓰이는 변수
    const [TMP, setTMP] = useState("0"); // 1시간 기온 (℃)
    const [TMN, setTMN] = useState("0.0"); // 일 최저기온 (℃)
    const [TMX, setTMX] = useState("0.0"); // 일 최고기온 (℃)
    const [REH, setREH] = useState("00"); // 습도 (%)
    const [POP, setPOP] = useState("00"); // 강수확률 (%)
    const [SKY, setSKY] = useState(""); // 하늘상태 맑음(1), 구름많음(3), 흐림(4)
    const [PTY, setPTY] = useState(""); // 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    const [today, setToday] = useState("00"); // 오늘 날짜
    const [image, setImage] = useState(null); // 이미지

    //2일간 날씨 예보에 쓰이는 변수
    const [todaydata, setTodaydata] = useState(null); // 오늘 데이터
    const [tomorrowdata, setTomorrowdata] = useState(null); // 내일 데이터
    const hours = ["0000", "0100", "0200", "0300", "0400", "0500", "0600", "0700", "0800", "0900", "1000", "1100", "1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900", "2000", "2100", "2200", "2300"];
    const [istoday, setIstoday] = useState("오늘 날씨");
    const [todayImagedata, setTodayImagedata] = useState(null); // 오늘 날씨 이미지
    const [tomorrowImagedata, setTomorrowImagedata] = useState(null); // 내일 날씨 이미지

    const [coordinate, setCoordinate] = useState(''); // api 요청에 보낼 경도 위도

    const changeData = () => {
        if (istoday==="오늘 날씨") {
            setIstoday("내일 날씨");
        } else {
            setIstoday("오늘 날씨");
        }
    }

    useEffect(() => {
        ApiService.weather(coordinate.x,coordinate.y).then((response) => {
            //현재 시간 날씨 예보에 쓰이는 변수 설정
            setTMP(response.data.TMP)
            setTMN(response.data.TMN)
            setTMX(response.data.TMX)
            setREH(response.data.REH)
            setPOP(response.data.POP)
            setToday(response.data.todayTime)

            if(response.data.SKY==="1") {
                setSKY("맑음")
            } else if(response.data.SKY==="3") {
                setSKY("구름많음")
            } else if(response.data.SKY==="4") {
                setSKY("흐림")
            }

            if(response.data.PTY==="0") {
                if(response.data.SKY==="1") {
                    setImage(sunny)
                } else if(response.data.SKY==="3") {
                    setImage(cloud)
                } else if(response.data.SKY==="4") {
                    setImage(manycloud)
                }
            } else if(response.data.PTY==="1") {
                setImage(rain)
                setPTY("비")
            } else if(response.data.PTY==="2") {
                setImage(snowrain)
                setPTY("비/눈")
            }else if(response.data.PTY==="3") {
                setImage(snow)
                setPTY("눈")
            }else if(response.data.PTY==="4") {
                setImage(shower)
                setPTY("소나기")
            }

            //2일간 날씨 예보에 쓰이는 변수 설정
            setTodaydata(response.data.Today);
            setTomorrowdata(response.data.Tomorrow);

            const TodayImages = hours.map((hour) => {
                const hourData = response.data.Today[hour];
                const { PTY, SKY } = hourData;

                if (PTY === "0") {
                    if (SKY === "1") {
                        return sunny;
                    } else if (SKY === "3") {
                        return cloud;
                    } else if (SKY === "4") {
                        return manycloud;
                    }
                } else if (PTY === "1") {
                    return rain;
                } else if (PTY === "2") {
                    return snowrain;
                } else if (PTY === "3") {
                    return snow;
                } else if (PTY === "4") {
                    return shower;
                } else {
                    return null;
                }
            });
            setTodayImagedata(TodayImages);

            const TomorrowImages = hours.map((hour) => {
                const hourData = response.data.Tomorrow[hour];
                const { PTY, SKY } = hourData;

                if (PTY === "0") {
                    if (SKY === "1") {
                        return sunny;
                    } else if (SKY === "3") {
                        return cloud;
                    } else if (SKY === "4") {
                        return manycloud;
                    }
                } else if (PTY === "1") {
                    return rain;
                } else if (PTY === "2") {
                    return snowrain;
                } else if (PTY === "3") {
                    return snow;
                } else if (PTY === "4") {
                    return shower;
                } else {
                    return null;
                }
            });
            setTomorrowImagedata(TomorrowImages);
        }).catch(error => console.log("날씨 API 요청 실패 " + error))
    }, [coordinate.x,coordinate.y])

    useEffect(() => {
        setCoordinate(ApiService.dfs_xy_conv("toXY",props.location.latitude,props.location.longitude))
    }, [props])

    return (
        <div className="api_content" >

            {/*현재 시간 날씨 예보*/}
            <div style={{display: "flex", height: "80px"}}>
                <span style={{fontSize: "60px"}}>{TMP}°</span>
                <div style={{width: "70px", fontSize: "16px", marginTop: "40px"}}>
                    <span style={{color: "blue", fontSize: "16px"}}>{TMN}°</span>
                    <span>/ </span>
                    <span style={{color: "red", fontSize: "20px"}}>{TMX}°</span>
                </div>
                <div style={{width: "150px"}}><img src={image} alt="날씨" width="80" height="80" style={{float: "right"}}/></div>
            </div>
            <div style={{height: "20px", fontSize: "16px"}}>
                <div>{SKY} {PTY}</div>
            </div>
            <div style={{margin: "5px 0px"}}>
                <span>습도: {REH}% / </span>
                <span>강수확률: {POP}%</span>
            </div>
            <span>{today}</span>
            <div style={{display: "flex"}}>
                <img src={locationImage} alt="장소" width="40" height="40"/>
                <div style={{height: "40px", lineHeight: "50px"}}>{props.addressName}</div>
            </div>

            {/*오늘 내일 날씨 예보*/}
            <div style={{textAlign: "center"}}>
                <input type="button" className="changebutton" onClick={changeData} value={istoday}/>
                <div className="swiper-container">
                    <div className="swiper-wrapper">
                        <div className="swiper-slide" style={{height: "180px"}}>
                            <div className="weather_graph_out">
                                <div className="weather_graph_in">
                                    {
                                        (istoday === "오늘 날씨" && todaydata !== null) ? hours.map((hour, index)=> (
                                                <div key={index} className="weather_graph_box"><span style={{fontSize: "25px"}}>{todaydata[hour]["TMP"]}°</span>
                                                    <div><img src={todayImagedata[index]} alt="날씨" width="25" height="25"/></div>
                                                    <div style={{width: "40px", marginTop: "5px"}}>강수확률</div>
                                                    <div style={{width: "40px", marginTop: "5px"}}>{todaydata[hour]["POP"]}%</div>
                                                    <div style={{width: "40px", marginTop: "5px"}}>{hour.substr(0, 2)}:00</div>
                                                </div>
                                            )) : <div></div>
                                    }
                                    {
                                        (istoday === "내일 날씨" && tomorrowdata !== null) ? hours.map((hour, index)=> (
                                                <div key={index} className="weather_graph_box"><span style={{fontSize: "25px"}}>{tomorrowdata[hour]["TMP"]}°</span>
                                                    <div><img src={tomorrowImagedata[index]} alt="날씨" width="25" height="25"/></div>
                                                    <div style={{width: "40px", marginTop: "5px"}}>강수확률</div>
                                                    <div style={{width: "40px", marginTop: "5px"}}>{tomorrowdata[hour]["POP"]}%</div>
                                                    <div style={{width: "40px", marginTop: "5px"}}>{hour.substr(0, 2)}:00</div>
                                                </div>
                                            )) : <div></div>
                                    }
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}

export default Weather;