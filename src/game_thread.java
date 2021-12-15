import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.net.*;

import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class AESencryption {

	public static String alg = "AES/CBC/PKCS5Padding";
	private final String key = "10241048102410481024104810241048";
	private final String iv = key.substring(0, 16); // 16byte

	public String encrypt(String text) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
		SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
		byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public String decrypt(String cipherText) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
		SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

		byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
		byte[] decrypted = cipher.doFinal(decodedBytes);
		return new String(decrypted, "UTF-8");
	}
}

public class game_thread extends Thread {
	BufferedReader in = null;
	BufferedWriter out = null;

	Socket socket = null;
	game_server server = null;

	Vector<game_thread> auser; // �뿰寃곕맂 紐⑤뱺 �겢�씪�씠�뼵�듃
	Vector<game_thread> wuser; // ��湲곗떎�뿉 �엳�뒗 �겢�씪�씠�뼵�듃
	Vector<game_room> room; // �깮�꽦�맂 Room

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

			AESencryption encoder = new AESencryption();

			String outputMessage = null;
			outputMessage = String.valueOf(inputMessage);

			System.out.println(inputMessage + "Connected!");

			out.write(inputMessage + "\n");
			out.flush();
			if (inputMessage.equalsIgnoreCase("0")) {

			} else if (inputMessage.equals("1")) {
				inputMessage = in.readLine();
				System.out.println("Client send1: " + inputMessage);
				path = Paths.get(PathPointer + "/" + inputMessage);
				System.out.println(path + " " + Files.exists(path) + inputMessage);
				if (!Files.exists(path)) {
					out.write("ENID" + "\n");
					out.flush();
				} else {
					out.write("CRID" + "\n");
					System.out.println("ID is correct as " + inputMessage);
					out.flush();
					String UserID = inputMessage;
					try {

						File UID = new File(PathPointer + "/" + inputMessage + "/data.txt");
						inputMessage = in.readLine();
						FileReader pw = new FileReader(UID);
						BufferedReader pwReader = new BufferedReader(pw);

						String pwLine = pwReader.readLine();

						String ptoken[] = inputMessage.split(" ");
						System.out.println("Client send2: " + inputMessage);
						System.out.println(inputMessage + " TK1: " + ptoken[0] + "TK2:" + ptoken[1]);
						String pwstring = encoder.encrypt(ptoken[1]);
						System.out.println(pwLine + " @ " + pwstring);
						if (encoder.decrypt(pwLine).equals(encoder.decrypt(pwstring)) == true) {
							System.out.println("Login complete as ID of " + UserID);
							out.write("LEND " + UserID + "\n");
							out.flush();
							out.write("LEND UI:" + UserID + " Email: " + pwReader.readLine() + " Username: "
									+ pwReader.readLine() + "\n");
							out.flush();
						} else {
							System.out.println("Login INcomplete as ID of " + UserID);
							out.write("ENPW" + "\n");
							out.flush();
						}

					} catch (Exception e) {
						e.getStackTrace();
					} finally {

					}

				}
			} else if (inputMessage.equals("2")) {
				inputMessage = in.readLine();
				path = Paths.get(PathPointer + "/" + inputMessage);
				System.out.println(path + " " + Files.exists(path) + inputMessage);
				if (Files.exists(path)) {
					out.write("EIDE" + "\n");
					out.flush();
				} else {
					out.write("CRID" + "\n");
					System.out.println("ID is correct as " + inputMessage);
					out.flush();
					String UserID = inputMessage;
					File UID = new File(PathPointer + "/" + inputMessage);
					UID.mkdir();
					try {

						UID = new File(PathPointer + "/" + inputMessage + "/data.txt");
						UID.createNewFile();

						inputMessage = in.readLine();
						PrintWriter UWriter = new PrintWriter(new FileWriter(UID));
						String pwEcd = encoder.encrypt(inputMessage);

						UWriter.println(pwEcd);

						System.out.println(inputMessage + " Printed!");
						out.write("PWDN" + "\n"); // password done
						out.flush();

						inputMessage = in.readLine();
						UWriter.println(inputMessage);
						System.out.println(inputMessage + " Printed!");
						out.write("UNDN" + "\n"); // Username done
						out.flush();

						inputMessage = in.readLine();
						UWriter.println(inputMessage);
						out.write("EMDN" + "\n"); // Email done
						out.flush();

						UWriter.close();
						System.out.println(inputMessage + " Printed!");
						out.write("CIID" + "\n");
						out.flush();

					} catch (Exception e) {
						e.printStackTrace();
					} finally {

					}
				}
				/* 방 생성 */
			} else if (inputMessage.equalsIgnoreCase("3")) {
				myRoom = new game_room(); // 새로운 Room 객체 생성 후 myRoom에 초기화
				myRoom.title = nickname; // 방 제목을 닉네임으로 설정
				myRoom.count++; // 방의 인원수 하나 추가

				room.add(myRoom); // room 배열에 myRoom을 추가

				myRoom.ccu.add(this); // myRoom의 접속인원에 클라이언트 추가
				wuser.remove(this); // 대기실 접속 인원에서 클라이언트 삭제

				out.write("3" + "//OKAY");
				System.out.println("[Server] " + "방 '" + nickname + "' 생성");

				sendWait(roomInfo()); // 대기실 접속 인원에 방 목록을 전송
				sendRoom(roomUser()); // 방에 입장한 인원에 방 인원 목록을 전송
			} // 방 생성 if문

			/* 방 입장 */
			else if (inputMessage.equalsIgnoreCase("4")) {
				for (int i = 0; i < room.size(); i++) { // 생성된 방의 개수만큼 반복
					game_room r = room.get(i);
					if (r.title.equals(nickname)) { // 방 제목이 같고

						if (r.count < 2) { // 방 인원수가 2명보다 적을 때 입장 성공
							myRoom = room.get(i); // myRoom에 두 조건이 맞는 i번째 room을 초기화
							myRoom.count++; // 방의 인원수 하나 추가

							wuser.remove(this); // 대기실 접속 인원에서 클라이언트 삭제
							myRoom.ccu.add(this); // myRoom의 접속 인원에 클라이언트 추가

							sendWait(roomInfo()); // 대기실 접속 인원에 방 목록을 전송
							sendRoom(roomUser()); // 방에 입장한 인원에 방 인원 목록을 전송

							out.write("4" + "//OKAY");
							System.out.println("[Server] " + "방 '" + myRoom.count + "' 입장");
						}

						else { // 방 인원수가 2명 이상이므로 입장 실패
							out.write("4" + "//FAIL");
							System.out.println("[Server] " + "방 '" + myRoom.count + "' 인원초과");
						}
					}

					else { // 같은 방 제목이 없으니 입장 실패
						out.write("4" + "//FAIL");
						System.out.println("[Server] " + "방 '" + myRoom.count + "' 입장오류");
					}
				}
			} // 방 입장 if문

			/* 프로그램 종료 */
			else if (inputMessage.equalsIgnoreCase("5")) {
				auser.remove(this); // 전체 접속 인원에서 클라이언트 삭제
				wuser.remove(this); // 대기실 접속 인원에서 클라이언트 삭제

				sendWait(connectedUser()); // 대기실 접속 인원에 전체 접속 인원을 전송
			} // 프로그램 종료 if문

			/* 방 퇴장 */
			else if (inputMessage.equalsIgnoreCase("6")) {
				myRoom.ccu.remove(this); // myRoom의 접속 인원에서 클라이언트 삭제
				myRoom.count--; // myRoom의 인원수 하나 삭제
				wuser.add(this); // 대기실 접속 인원에 클라이언트 추가

				System.out.println("[Server] " + "방 '" + myRoom.title + "' 퇴장");

				if (myRoom.count == 0) { // myRoom의 인원수가 0이면 myRoom을 room 배열에서 삭제
					room.remove(myRoom);
				}

				if (room.size() != 0) { // 생성된 room의 개수가 0이 아니면 방에 입장한 인원에 방 인원 목록을 전송
					sendRoom(roomUser());

				}

				sendWait(roomInfo()); // 대기실 접속 인원에 방 목록을 전송
				sendWait(connectedUser()); // 대기실 접속 인원에 전체 접속 인원을 전송
			} // 방 퇴장 if문

			/* tictaetoc */
			else if (inputMessage.equalsIgnoreCase("7")) {
				for (int i = 0; i < myRoom.ccu.size(); i++) { // myRoom의 인원수만큼 반복

					if (!myRoom.ccu.get(i).nickname.equals(nickname)) { // 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
						// game_gui game = new game_gui();
						// myRoom.ccu.get(i).out.write("7" + "//" + m[1] + "//" + m[2] + "//" + m[3]);
					}
				}
			} // Tictactoe if문

			/* 승리 및 전적 업데이트 */
			else if (inputMessage.equalsIgnoreCase("8")) {
				System.out.println("[Server] " + nickname + " 승리");

				if (db.winRecord(nickname)) { // 전적 업데이트가 성공하면 업데이트 성공을 전송
					out.write("10" + "//OKAAAAY");
				} else { // 전적 업데이트가 실패하면 업데이트 실패를 전송
					out.write("10" + "//FAIL");
				}

				for (int i = 0; i < myRoom.ccu.size(); i++) { // myRoom의 인원수만큼 반복
					/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
					if (!myRoom.ccu.get(i).nickname.equals(nickname)) {
						myRoom.ccu.get(i).out.write("9" + "//");

						if (db.loseRecord(myRoom.ccu.get(i).nickname)) { // 전적 업데이트가 성공하면 업데이트 성공을 전송
							myRoom.ccu.get(i).out.write("10" + "//OKAY");
						} else { // 전적 업데이트가 실패하면 업데이트 실패를 전송
							myRoom.ccu.get(i).out.write("10" + "//FAIL");
						}
					}
				}
			} // 승리 및 전적 업데이트 if문

			/* 패배, 기권 및 전적 업데이트 */
			else if (inputMessage.equalsIgnoreCase("9")) {
				if (myRoom.count == 1) { // 기권을 했는데 방 접속 인원이 1명일 때 전적 미반영을 전송
					out.write("10" + "//NO");
				}

				else if (myRoom.count == 2) { // 기권 및 패배를 했을 때 방 접속 인원이 2명일 때
					out.write("9" + "//");

					if (db.loseRecord(nickname)) { // 전적 업데이트가 성공하면 업데이트 성공을 전송
						out.write("10" + "//OKAY");
					} else { // 전적 업데이트가 실패하면 업데이트 실패를 전송
						out.write("10" + "//FAIL");
					}

					for (int i = 0; i < myRoom.ccu.size(); i++) { // myRoom의 인원수만큼 반복

						/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
						if (!myRoom.ccu.get(i).nickname.equals(nickname)) {
							myRoom.ccu.get(i).out.write("8" + "//");

							if (db.winRecord(myRoom.ccu.get(i).nickname)) { // 전적 업데이트가 성공하면 업데이트 성공을 전송
								myRoom.ccu.get(i).out.write("10" + "//OKAY");
							} else { // 전적 업데이트가 실패하면 업데이트 실패를 전송
								myRoom.ccu.get(i).out.write("10" + "//FAIL");
							}
						}
					}
				} // 패배, 기권 및 전적 업데이트 if문
			} // while문
		} catch (

		IOException e) {
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
