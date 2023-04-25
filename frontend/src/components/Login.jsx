import React, { useState } from "react";
import axios from "axios";
import "../styles/Login.css";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { tokenState } from "../recoil/token";
import { userState } from "../recoil/user";
import useDidMountEffect from "../useDidMountEffect";

function Login() {
    const [token,setToken] = useRecoilState(tokenState);
    const [user,setUser] = useRecoilState(userState);

    const navigate = useNavigate();

    const [inputId, setInputId] = useState("");
    const [inputPw, setInputPw] = useState("");

    const axiosConfig = {
        headers:{
            Authorization: "cos"
        }
    }
    const axiosBody = {
        username:inputId,
        password:inputPw
    }

    // input data 의 변화가 있을 때마다 value 값을 변경해서 useState 해준다
    const handleInputId = (e) => {
        setInputId(e.target.value);
    };

    const handleInputPw = (e) => {
        setInputPw(e.target.value);
    };

    // login 버튼 클릭 이벤트
    const LoginCheck = () => {
        axios
            .post("/login", axiosBody, axiosConfig)
            .then((res) => {
                alert("로그인 성공");
                setToken(res.headers.authorization);
            })
            .catch(error => alert("아이디와 비밀번호를 확인하세요"));
    };

    useDidMountEffect(() => {
        (
        axios.get("/api/v1/user/userCheck", {
            headers:{
                Authorization: token
            }
        })
            .then((res) => {
                setUser(res.data);
                navigate("/");
            })
            .catch(error => alert("로그인 실패")))
    }, [token])

    function checkAll() {
        if (!CheckUserId(inputId)) {
            return false;
        }
        if (!CheckPassword(inputPw)) {
            return false;
        }

        LoginCheck();
    }

    // 공백확인 함수
    function checkExistData(value, dataName) {
        if (value === "") {
            alert(dataName + " 입력해주세요!");
            return false;
        }
        return true;
    }

    function CheckUserId(id) {
        //Id가 입력되었는지 확인하기
        if (!checkExistData(id, "아이디를")) return false;

        var idRegExp = /^[a-zA-z0-9]{2,16}$/; //아이디 유효성 검사
        if (!idRegExp.test(id)) {
            alert("아이디는 영문 대소문자와 숫자 2 ~ 16자리로 입력해야합니다!");
            return false;
        }
        return true; //확인이 완료되었을 때
    }

    function CheckPassword(password) {
        //비밀번호가np 입력되었는지 확인하기
        if (!checkExistData(password, "비밀번호를")) return false;

        var passwordRegExp = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}/; //비밀번호 유효성 검사
        if (passwordRegExp.test(password)=== false) {
            alert("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
            return false;
        }
        return true; //확인이 완료되었을 때
    }


    return (
        <body className="text-center">
        <main className="form-signin w-100 m-auto">
            <form>
                <h1 className="h3 mb-3 fw-normal">로그인</h1>
                <div className="form-floating">
                    <input
                        type="text"
                        name="input_id"
                        value={inputId}
                        className="form-control"
                        placeholder="ID"
                        onChange={handleInputId}
                        minLength={2}
                        maxLength={16}
                        required
                    />
                    <label>ID</label>
                </div>
                <div className="form-floating">
                    <input
                        type="password"
                        name="input_pw"
                        value={inputPw}
                        className="form-control"
                        placeholder="Password"
                        onChange={handleInputPw}
                        minLength={2}
                        maxLength={16}
                        required
                    />
                    <label>Password</label>
                </div>

                <button
                    className="w-100 btn loginBtn btn-lg btn-primary"
                    type="button"
                    onClick={checkAll}
                >
                    Log in
                </button>
            </form>
        </main>
        </body>
    );
}

export default Login;
