import java.net.Socket;

public class game_user {
	private int id; 			// Unique ID
	private game_room room; 		// 유저가 속한 룸이다.
	private Socket sock; 		// 소켓 object
	private String nickName;	// 닉네임

	// 게임에 관련된 변수 설정
	// ...
	//

	private game_info.Location playerLocation; // 게임 정보
	private game_info.Status playerStatus; // 게임 정보

	public game_user() { // 아무런 정보가 없는 깡통 유저를 만들 때
	}

    /**
     * 유저 생성
     * @param nickName 닉네임
     */
    public game_user(String nickName) { // 닉네임 정보만 가지고 생성
		this.nickName = nickName;
	}

    /**
     * 방에 입장시킴
     * @param room  입장할 방
     */
    public void enterRoom(game_room room) {
		room.enterUser(this); // 룸에 입장시킨 후
		this.room = room; // 유저가 속한 방을 룸으로 변경한다.(중요)
	}

    /**
     * 방에서 퇴장
     * @param room 퇴장할 방
     */
    public void exitRoom(game_room room){
        this.room = null;
        // 퇴장처리(화면에 메세지를 준다는 등)
        // ...
    }

	public void setPlayerStatus(game_info.Status status) { // 유저의 상태를 설정
		this.playerStatus = status;
	}

	public void setPlayerLocation(game_info.Location location) { // 유저의 위치를 설정
		this.playerLocation = location;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public game_room getRoom() {
        return room;
    }

    public void setRoom(game_room room) {
        this.room = room;
    }

    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public game_info.Location getPlayerLocation() {
        return playerLocation;
    }

    public game_info.Status getPlayerStatus() {
        return playerStatus;
    }
}
