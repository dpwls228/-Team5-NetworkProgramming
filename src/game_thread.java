import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;
import java.net.*;

import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class game_thread extends Thread {
	BufferedReader in = null;
	BufferedWriter out = null;

	Socket socket = null;
	game_server server = null;

	Vector<game_thread> auser; // 연결된 모든 클라이언트
	Vector<game_thread> wuser; // 대기실에 있는 클라이언트
	Vector<game_room> room; // 생성된 Room

	Scanner inputstream = null;

	game_room myRoom;
	game_file db;
	String nickname = null;

	public game_thread(Socket sock, game_server server) {
		this.socket = sock;
		this.server = server;

		auser = server.alluser;
		wuser = server.waituser;
		room = server.room;
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String inputMessage;
			inputMessage = in.readLine();
			if (inputMessage.equals("GAME")) {
				System.out.println("Client added" + inputMessage);
				out.write("START\n");
				out.flush();
			} else {
				System.out.println("WRONG Client added");
				socket.close();
			}

			String PathPointer = "./Server";
			File ServerPointer = null;
			Path path = Paths.get(PathPointer);

			if (!Files.exists(path) && !Files.isDirectory(path)) {
				ServerPointer = new File(PathPointer);
				ServerPointer.mkdir();
			}
			PathPointer = "./Server/Users";
			path = Paths.get(PathPointer);

			if (!Files.exists(path) && !Files.isDirectory(path)) {
				ServerPointer = new File(PathPointer);
				ServerPointer.mkdir();
			}

			System.out.println("First loop");
			inputMessage = in.readLine();
			
			String outputMessage = null;
			outputMessage = String.valueOf(inputMessage);

			System.out.println(inputMessage + "Connected!");
			
			out.write(inputMessage + "\n");
			out.flush();
			if(inputMessage.equalsIgnoreCase("3")) {
				myRoom = new game_room();	//새로운 Room 객체 생성 후 myRoom에 초기화
				myRoom.title = myRoom.count;	//방 제목을 m[1]로 설정
				myRoom.count++;			//방의 인원수 하나 추가
				
				room.add(myRoom);		//room 배열에 myRoom을 추가
				
				myRoom.ccu.add(this);	//myRoom의 접속인원에 클라이언트 추가
				wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
				
				out.write("3" + "//OKAY");
				System.out.println("[Server] "+"방 '" + myRoom.count+ "' 생성");
				
				sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
				sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
			}  //방 생성 if문
		
			/* 방 입장 */
			else if(inputMessage.equalsIgnoreCase("4")) {
				for(int i=0; i<room.size(); i++) {	//생성된 방의 개수만큼 반복
					game_room r = room.get(i);
					if(r.title == r.count) {	//방 제목이 같고
						
						if(r.count < 2) {			//방 인원수가 2명보다 적을 때 입장 성공
							myRoom = room.get(i);	//myRoom에 두 조건이 맞는 i번째 room을 초기화
							myRoom.count++;			//방의 인원수 하나 추가
							
							wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
							myRoom.ccu.add(this);	//myRoom의 접속 인원에 클라이언트 추가
							
							sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
							sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
							
							out.write("4" + "//OKAY");
							System.out.println("[Server] "+"방 '" + myRoom.count+ "' 입장");
						}
						
						else {	//방 인원수가 2명 이상이므로 입장 실패
							out.write("4" + "//FAIL");
							System.out.println("[Server] "+"방 '" + myRoom.count+ "' 인원초과");
						}
					}
					
					else {	//같은 방 제목이 없으니 입장 실패
						out.write("4" + "//FAIL");
						System.out.println("[Server] "+"방 '" + myRoom.count+ "' 입장오류");
					}
				}
			} //방 입장 if문
			
			/* 프로그램 종료 */
			else if(inputMessage.equalsIgnoreCase("5")) {
				auser.remove(this);		//전체 접속 인원에서 클라이언트 삭제
				wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
				
				sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
			} //프로그램 종료 if문
			
			/* 방 퇴장 */
			else if(inputMessage.equalsIgnoreCase("6")) {
				myRoom.ccu.remove(this);	//myRoom의 접속 인원에서 클라이언트 삭제
				myRoom.count--;				//myRoom의 인원수 하나 삭제
				wuser.add(this);			//대기실 접속 인원에 클라이언트 추가
				
				System.out.println("[Server] " +"방 '" + myRoom.title + "' 퇴장");
				
				if(myRoom.count==0) {	//myRoom의 인원수가 0이면 myRoom을 room 배열에서 삭제
					room.remove(myRoom);
				}
				
				if(room.size() != 0) {	//생성된 room의 개수가 0이 아니면 방에 입장한 인원에 방 인원 목록을 전송
					sendRoom(roomUser());
					
				}
				
				sendWait(roomInfo());		//대기실 접속 인원에 방 목록을 전송
				sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
			} //방 퇴장 if문
			
			/* tictaetoc */
			else if(inputMessage.equalsIgnoreCase("7")) {
				for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
					
					if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
						//myRoom.ccu.get(i).out.write("7" + "//" + m[1] + "//" + m[2] + "//" + m[3]);
					}
				}
			}  //오목 if문
			
			/* 승리 및 전적 업데이트 */
			else if(inputMessage.equalsIgnoreCase("8")) {
				System.out.println("[Server] " + nickname + " 승리");
				
				if(db.winRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
					out.write("10" + "//OKAAAAY");
				} else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
					out.write("10" + "//FAIL");
				}
				
				for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
					/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
					if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
						myRoom.ccu.get(i).out.write("9" + "//");
						
						if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
							myRoom.ccu.get(i).out.write("10" + "//OKAY");
						} else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
							myRoom.ccu.get(i).out.write("10" + "//FAIL");
						}
					}
				}
			}  //승리 및 전적 업데이트 if문
			
			/* 패배, 기권 및 전적 업데이트 */
			else if(inputMessage.equalsIgnoreCase("9")) {
				if(myRoom.count==1) {	//기권을 했는데 방 접속 인원이 1명일 때 전적 미반영을 전송
					out.write("10" + "//NO");
				}
				
				else if(myRoom.count==2) {	//기권 및 패배를 했을 때 방 접속 인원이 2명일 때
					out.write("10" + "//");
					
					if(db.loseRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
						out.write("10" + "//OKAY");
					} else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
						out.write("10" + "//FAIL");
					}
					
					for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
						
						/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
							myRoom.ccu.get(i).out.write("8" + "//");
							
							if(db.winRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
								myRoom.ccu.get(i).out.write("10" + "//OKAY");
							} else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
								myRoom.ccu.get(i).out.write("10" + "//FAIL");
							}
						}
					}
			}  //패배, 기권 및 전적 업데이트 if문
		}  //while문
	}catch(

	IOException e)
	{
		System.out.println("[Server] 입출력 오류 > " + e.toString());
	}
} // run()
	/* 현재 존재하는 방의 목록을 조회하는 메소드 */

	String roomInfo() {
		String msg = "11" + "//";

		for (int i = 0; i < room.size(); i++) {
			msg = msg + room.get(i).title + " : " + room.get(i).count + "@";
		}
		return msg;
	}

	/* 클라이언트가 입장한 방의 인원을 조회하는 메소드 */
	String roomUser() {
		String msg = "12" + "//";

		for (int i = 0; i < myRoom.ccu.size(); i++) {
			msg = msg + myRoom.ccu.get(i).nickname + "@";
		}
		return msg;
	}

	/* 접속한 모든 회원 목록을 조회하는 메소드 */
	String connectedUser() {
		String msg = "13" + "//";

		for (int i = 0; i < auser.size(); i++) {
			msg = msg + auser.get(i).nickname + "@";
		}
		return msg;
	}

	/* 대기실에 있는 모든 회원에게 메시지 전송하는 메소드 */
	void sendWait(String m) {
		for (int i = 0; i < wuser.size(); i++) {
			try {
				wuser.get(i).out.write(m);
			} catch (IOException e) {
				wuser.remove(i--);
			}
		}
	}

	/* 방에 입장한 모든 회원에게 메시지 전송하는 메소드 */
	void sendRoom(String m) {
		for (int i = 0; i < myRoom.ccu.size(); i++) {
			try {
				myRoom.ccu.get(i).out.write(m);
			} catch (IOException e) {
				myRoom.ccu.remove(i--);
			}
		}
	}
	}
	// CCUser 클래스
