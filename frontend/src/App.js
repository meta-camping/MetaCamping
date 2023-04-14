import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import ListBoardComponent from './components/ListBoardComponent';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import CreateBoardComponent from "./components/CreateBoardComponent";
import ReadBoardComponent from "./components/ReadBoardComponent";
import ChatRoomList from './components/ChatRoomList';
import ChatRoom from "./components/ChatRoom"
import Main from "./components/Main";
import Register from "./components/Register";
import Login from "./components/Login";


function App() {
  return (
      <div>
          <Router>
          <HeaderComponent/>
          <div className="container">
              <Routes>
                  <Route path = "/" element = {<Main />} />
                  <Route path = "/board" element = {<ListBoardComponent />} />
                  <Route path = "/register" element = {<Register />} />
                  <Route path = "/login" element = {<Login />} />
                  <Route path = "/create-board/:postId" element = {<CreateBoardComponent />} />
                  <Route path = "/read-board/:postId" element = {<ReadBoardComponent />} />
                  <Route path = "/chat/list" element = {<ChatRoomList />} />
                  <Route path = "/chat/room/:room_id" element = {<ChatRoom />} />
              </Routes>
          </div>
          <FooterComponent/>
        </Router>
      </div>
  );
}

export default App;