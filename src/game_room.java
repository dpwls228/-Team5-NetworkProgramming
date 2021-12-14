import java.util.Vector;

public class game_room {
	Vector<game_thread> ccu;
	int title;
	int count = 0;
	
	game_room() {	//Room 객체 생성 시 접속(입장)한 클라이언트 객체에 대한 정보를 Room에 저장한다.
		ccu = new Vector<>();
	}
}
