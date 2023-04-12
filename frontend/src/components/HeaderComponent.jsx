import React, { Component } from 'react';
import camping from '../images/camping.png';

class HeaderComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {

        }
    }
    render() {
        return (
            <div className="container">
                <header className="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">

                    <div className="main_title"> <img src={camping} alt="logo" /><span style={{fontSize: "40px" ,marginLeft: "20px", lineHeight: "40px"}}>Meta Camping</span></div>

                    <ul className="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
                        <li><a href="/" className="nav-link px-2 link-secondary" style={{fontSize: "20px"}}>Home</a></li>
                        <li><a href="#" className="nav-link px-2 link-dark" style={{fontSize: "20px"}}>Profile</a></li>
                        <li><a href="/chat/list" className="nav-link px-2 link-dark" style={{fontSize: "20px"}}>Chatting</a></li>
                        <li><a href="/board" className="nav-link px-2 link-dark" style={{fontSize: "20px"}}>Notice</a></li>
                        <li><a href="#" className="nav-link px-2 link-dark" style={{fontSize: "20px"}}>About</a></li>
                    </ul>

                    <div className="col-md-3 text-end">
                        <a href="/login" className="btn btn-outline-primary me-2">Login</a>
                        <a href="/register" className="btn btn-primary">Sign-up</a>
                    </div>
                </header>
            </div>
        )
    }
}

export default HeaderComponent;