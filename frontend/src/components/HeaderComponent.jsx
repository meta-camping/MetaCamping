import React from 'react';
import camping from '../images/camping.png';
import axios from "axios";
import {useRecoilState} from "recoil";
import { tokenState } from "../recoil/token";
import { userState } from "../recoil/user";
import { useNavigate } from "react-router-dom";

function HeaderComponent() {
    const [token,setToken] = useRecoilState(tokenState);
    const [user,setUser] = useRecoilState(userState);
    const navigate = useNavigate();

    const logout = () => {
        setToken(null);
        navigate("/");
        alert("로그아웃 성공");
    }

    const goProfile = () => {
        axios.get("/api/v1/user/profile", {
            headers:{
                Authorization: token
            }
        })
            .then((res) => {
                setUser(res.data);
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

                <div className="col-md-3 text-end">
                    {!token ?
                        <a href="/login" className="btn btn-outline-primary me-2">Login</a>
                    :
                        <button className="btn btn-outline-primary me-2" onClick={logout}>Logout</button>}
                    <a href="/register" className="btn btn-primary">Sign-up</a>
                </div>
            </header>
        </div>
    )
}

export default HeaderComponent;