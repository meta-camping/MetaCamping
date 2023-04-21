import {React, useState, useEffect }from 'react';
import camping from '../images/camping.png';
import axios from "axios";
import {useRecoilState} from "recoil";
import { tokenState } from "../recoil/token";
import { useNavigate } from "react-router-dom";
import {userState} from "../recoil/user";
import useDidMountEffect from "../useDidMountEffect";

function HeaderComponent() {
    const [token,setToken] = useRecoilState(tokenState);
    const [user,setUser] = useRecoilState(userState);

    /**바로 user.nickname을 사용하면 로그인시 값이 들어오기전에 HeaderComponent에서 user.nickname을 참조해서 오류 발생
    따라서 token을 인식하는 useEffect를 사용해서 nickname에 user.nickname을 넣어준다.*/
    const [nickname,setNickname] = useState('');
    const navigate = useNavigate();

    const logout = () => {
        setToken(null);
        navigate("/");
        alert("로그아웃 성공");
    }

    useEffect(() => {
        setNickname(user.nickname);
    }, [token])

    const goProfile = () => {
        axios.get("/api/v1/user", {
            headers:{
                Authorization: token
            }
        })
            .then((res) => {
                navigate('/profile');
            })
            .catch(error => alert("로그인이 필요합니다"));
    };

    return (
        <div className="container">
            <header className="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">

                <div className="main_title">
                    <a href="/" className="nav-link px-2" style={{fontSize: "40px", lineHeight: "40px"}}>
                        <img src={camping} alt="logo" />
                        <span style={{marginLeft: "20px"}}>Meta Camping</span>
                    </a>
                </div>

                <ul className="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0" style={{fontSize: "20px"}}>
                    <li><a href="/" className="nav-link px-2 link-secondary" >Home</a></li>
                    <li><button className="nav-link px-2 link-dark" onClick={goProfile} style={{ border: "none", backgroundColor: "transparent"}}>Profile</button></li>
                    <li><a href="/chat/list" className="nav-link px-2 link-dark" >Chatting</a></li>
                    <li><a href="/board" className="nav-link px-2 link-dark" >Notice</a></li>
                    <li><a href="#" className="nav-link px-2 link-dark" >About</a></li>
                </ul>

                <div className="col-md-3 text-end" >
                    {!token ?
                        <a href="/login" className="btn btn-outline-primary me-2">Login</a>
                    :   <>
                        <span className="profile_content" style={{marginRight: "20px"}}>{nickname}</span>
                        <button className="btn btn-outline-primary me-2" onClick={logout}>Logout</button>
                        </>
                        }
                    <a href="/register" className="btn btn-primary">Sign-up</a>
                </div>
            </header>
        </div>
    )
}

export default HeaderComponent;