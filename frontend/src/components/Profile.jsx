import { React, useState } from "react";
import axios from "axios";
import "../styles/Profile.css";
import { useRecoilState } from "recoil";
import { userState } from "../recoil/user";
import { tokenState } from "../recoil/token";
import {useNavigate} from "react-router-dom";

function Profile() {
    const [token,setToken] = useRecoilState(tokenState);
    const [user,setUser] = useRecoilState(userState);
    const navigate = useNavigate();

    const [passwordisEditing, setPasswordIsEditing] = useState(false);
    const [nicknameisEditing, setNicknameIsEditing] = useState(false);

    const [inputPw, setInputPw] = useState("");
    const [inputPw1, setInputPw1] = useState("");
    const [inputPw2, setInputPw2] = useState("");
    const [inputNn1, setInputNn1] = useState("");

    const handleInputPw = (e) => {
        setInputPw(e.target.value);
    };

    const handleInputPw1 = (e) => {
        setInputPw1(e.target.value);
    };

    const handleInputPw2 = (e) => {
        setInputPw2(e.target.value);
    };

    const handleInputNn1 = (e) => {
        setInputNn1(e.target.value);
    };

    const axiosConfig = {
        headers:{
            Authorization: token
        }
    }
    const axiosBody = {
        username: user.username,
        password: inputPw,
        nickname: user.nickname,
        upadate_password: inputPw1,
        upadate_nickname: inputNn1
    }
    const UpdatePasswordCheck = () => {
        axios
            .post("/api/v1/user/updatePassword", axiosBody, axiosConfig)
            .then((res) => {
                if(res.data==="비밀번호 수정 완료"){
                    setToken(null);
                    setUser(null);
                    navigate("/");
                    alert("비밀번호 수정 완료. 다시 로그인 해주세요");
                }
                if(res.data==="잘못된 접근입니다"){
                    alert("잘못된 접근입니다");
                }
                if(res.data==="기존 비밀번호를 확인하세요"){
                    alert("기존 비밀번호를 확인하세요");
                }
            })
            .catch(error => alert("비밀번호 수정 실패"))
    };

    const UpdateNicknameCheck = () => {
        axios
            .post("/api/v1/user/updateNickname", axiosBody, axiosConfig)
            .then((res) => {
                if(res.data==="닉네임 수정 완료"){
                    setToken(null);
                    setUser(null);
                    navigate("/");
                    alert("닉네임 수정 완료. 다시 로그인 해주세요");
                }
                if(res.data==="이미 존재하는 닉네임 입니다"){
                    alert("이미 존재하는 닉네임 입니다");
                }
            })
            .catch(error => alert("닉네임 수정 실패"))
    };

    function UpdatePasswordcheckAll() {
        if (!CheckPassword(inputPw, inputPw1, inputPw2)) {
            return false;
        }

        UpdatePasswordCheck();
    }

    function UpdateNicknamecheckAll() {
        if (!CheckNickname(inputNn1)) {
            return false;
        }

        UpdateNicknameCheck();
    }

    // 공백확인 함수
    function checkExistData(value, dataName) {
        if (value === "") {
            alert(dataName + " 입력해주세요!");
            return false;
        }
        return true;
    }

    function CheckPassword(password, password1, password2) {
        //비밀번호가 입력되었는지 확인하기
        if (!checkExistData(password, "기존 비밀번호를")) return false;
        if (!checkExistData(password1, "수정할 비밀번호를")) return false;
        if (!checkExistData(password2, "비밀번호 확인을")) return false;
        if (!CheckSamePassword(password1,password2)) return false;

        var passwordRegExp = /^[a-zA-z0-9]{2,16}$/; //비밀번호 유효성 검사
        if (passwordRegExp.test(password1) === false) {
            alert(
                "비밀번호는 영문 대소문자와 숫자 2 ~ 16자리로 입력해야합니다!"
            );
            return false;
        }
        return true; //확인이 완료되었을 때
    }

    function CheckNickname(nickname) {
        //닉네임이 입력되었는지 확인하기
        if (!checkExistData(nickname, "닉네임을")) return false;

        var nicknameRegExp = /^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2,6}$/; //닉네임 유효성 검사
        if (!nicknameRegExp.test(nickname)) {
            alert("닉네임은 한글 2 ~ 6자리로 입력해야합니다!");
            setInputNn1("");
            return false;
        }
        return true; //확인이 완료되었을 때
    }

    function CheckSamePassword(password,password2) {
        if (password !== password2) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }
        return true; //확인이 완료되었을 때
    }

    const passwordStartEditingHandler = () => {
        if (nicknameisEditing === true) {
            setNicknameIsEditing(false);
        }
        setPasswordIsEditing(true);
    };

    const passwordEndEditingHandler = () => {
        UpdatePasswordcheckAll();
        setPasswordIsEditing(false);
    };

    const passwordEditingCancleHandler = () => {
        setPasswordIsEditing(false);
    };

    const nicknameStartEditingHandler = () => {
        if (passwordisEditing === true) {
            setPasswordIsEditing(false);
        }
        setNicknameIsEditing(true);
    };

    const nicknameEndEditingHandler = () => {
        UpdateNicknamecheckAll();
        setNicknameIsEditing(false);
    };
    const nicknameEditingCancleHandler = () => {
        setNicknameIsEditing(false);
    };


    return (
        <div>
            <div>
                <h2 className="text-center" style={{marginBottom: "30px"}}>프로필</h2>
            </div>
            {passwordisEditing ? (
                <div className="profile_content" style={{float:"none", margin:"0 auto"}}>
                    기존 비밀번호:
                    <input
                        className="inputbox"
                        type="password"
                        name="existing_password"
                        value={inputPw}
                        placeholder="기존 비밀번호를 입력하세요"
                        onChange={handleInputPw}
                        minLength={2}
                        maxLength={16}
                        required
                    />
                    <br />
                    비밀번호 변경:
                    <input
                        className="inputbox"
                        type="password"
                        name="modified_password"
                        value={inputPw1}
                        placeholder="변경할 비밀번호를 입력하세요"
                        onChange={handleInputPw1}
                        minLength={2}
                        maxLength={16}
                        required
                    />
                    <br />
                    비밀번호 확인:
                    <input
                        className="inputbox"
                        type="password"
                        name="modified_password2"
                        value={inputPw2}
                        placeholder="변경할 비밀번호를 입력하세요"
                        onChange={handleInputPw2}
                        minLength={2}
                        maxLength={16}
                        required
                    />
                    <br />
                    <button
                        type="button"
                        className="w-100 btn loginBtn btn-lg btn-danger"
                        onClick={passwordEditingCancleHandler}
                    >
                        비밀번호 변경취소
                    </button>
                    <button
                        type="button"
                        className="w-100 btn loginBtn btn-lg btn-success"
                        onClick={passwordEndEditingHandler}
                    >
                        비밀번호 변경완료
                    </button>
                    <br />
                </div>
            ) : (
                <div>
                    <button
                        type="button"
                        className="w-100 btn loginBtn btn-lg btn-primary"
                        onClick={passwordStartEditingHandler}
                    >
                        비밀번호 변경
                    </button>
                    <br />
                </div>
            )}
            {nicknameisEditing ? (
                <div className="profile_content" style={{float:"none", margin:"50px auto"}}>
                    기존 닉네임: {user.nickname}
                    <br />
                    닉네임 변경:
                    <input
                        className="inputbox"
                        type="text"
                        name="modified_nickname"
                        value={inputNn1}
                        placeholder="변경할 닉네임을 입력하세요"
                        onChange={handleInputNn1}
                        minLength={2}
                        maxLength={6}
                        required
                    />
                    <br />
                    <button
                        type="button"
                        className="w-100 btn loginBtn btn-lg btn-danger"
                        onClick={nicknameEditingCancleHandler}
                    >
                        닉네임 변경취소
                    </button>
                    <button
                        type="button"
                        className="w-100 btn loginBtn btn-lg btn-success"
                        onClick={nicknameEndEditingHandler}
                    >
                        닉네임 변경완료
                    </button>
                    <br />
                </div>
            ) : (
                <div>
                    <button
                        type="button"
                        className="w-100 btn loginBtn btn-lg btn-primary"
                        onClick={nicknameStartEditingHandler}
                    >
                        닉네임 변경
                    </button>
                    <br />
                </div>
            )}
        </div>
    );
}

export default Profile;
