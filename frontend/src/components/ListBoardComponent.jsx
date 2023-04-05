import React from 'react';
import { useNavigate } from 'react-router-dom';
import BoardService from '../services/BoardService';

function ListBoardComponent() {
    const [boards, setBoards] = React.useState([]);

    React.useEffect(() => {
        BoardService.getBoards().then((res) => {
            setBoards(res.data);
        });
    }, []);

    const navigate = useNavigate();

    const handleCreateBoard = () => {
        navigate('/create-board/_create');
    };

    const handleReadBoard = (postId) => {
        navigate(`/read-board/${postId}`);
    }

    return (
        <div>
            <h2 className="text-center">Boards List</h2>
            <div className ="row">
                <table className="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <th>글 번호</th>
                        <th>제목 </th>
                        <th>작성일 </th>
                        <th>갱신일 </th>
                        <th>조회수</th>
                    </tr>
                    </thead>
                    <tbody>
                    {boards.map(
                        board =>
                            <tr key = {board.postId}>
                                <td>{board.postId}</td>
                                <td><a onClick = {() => handleReadBoard(board.postId)}>{board.title}</a></td>
                                <td>{board.createdTimeString}</td>
                                <td>{board.updatedTimeString}</td>
                                <td>{board.hits}</td>
                            </tr>
                    )}
                    </tbody>
                </table>
            </div>
            <button className = "btn btn-primary" onClick = {handleCreateBoard}>글 쓰기</button>
        </div>
    );
}

export default ListBoardComponent;