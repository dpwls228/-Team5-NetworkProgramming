package network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import network.LobbyClient;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import java.net.*;
import java.util.*;

public class LobbyServer extends JFrame{
	
	private Socket sock;
	ExecutorService executorService; // thread pool 필드 선언
	ServerSocket serverSocket; // serversocket 필드 선언
	List<Client> connections = new Vector<Client>(); // 연결된 클라이언트를 저장할 connections 필드 선언
	Vector<String> serverMember = new Vector<String>();
	
	public LobbyServer() {
		startServer();
	}
	
	void startServer() {
		executorService = Executors.newFixedThreadPool(100);
		
		// 포트 넘버 받아오기
		File serverfile = new File("./serverInfo.txt");
		BufferedReader br;
		int nPort = 0;
		
		try {
 			FileReader reader = new FileReader(serverfile);
 			br = new BufferedReader(reader);
 			String portData = br.readLine();
 			nPort = Integer.parseInt(portData.split(" ")[1]);		// 파일 형식   PORT: 9999
 		} catch (IOException e) {
 			nPort = 1024;
 		}
		
		try {
			serverSocket = new ServerSocket(); //serverSocket 객체 생성
			serverSocket.bind(new InetSocketAddress("localhost", nPort)); // 127.0.0.1:7121과 binding
		} catch(Exception e) {
			if(!serverSocket.isClosed()) {//예외가 발생했을때 serversocket이 닫혀있지 않으면
				stopServer(); // 호출(startServer를 종료)
			}
			return;
		}
		
		Runnable runnable = new Runnable() { // 연결 수락 작업을 Runnable로 정의
			@Override
			public void run() { // 메소드를 재정의
				System.out.print("Start server\n");
				while(true) { // 연결을 무한히 기다림
					try {
						Socket socket = serverSocket.accept(); // 클라이언트의 연결요청을 기다리고, accept 메소드 호출
						String message = "[Accept Connect: " + socket.getRemoteSocketAddress()  + ": " + Thread.currentThread().getName() + "]";
						System.out.print(message + "\n");
						Client client = new Client(socket); // 클라이언트 객체 생성
						connections.add(client); // 생성된 클라이언트 객체를 connections 컬렉션에 추가
						System.out.print("[Connect Count: " + connections.size() + "]\n");
						//컬렉션에 저장된 클라이언트 객체의 수를 출력
					} catch (Exception e) {
						if(!serverSocket.isClosed()) { // 예외발생시 서버소켓이 닫혀있지않으면
							stopServer();  // startServer를 종료하고
						}
						break; // 연결대기를 끝냄
					}
				}
			}
		};
		
		executorService.submit(runnable); // 스레드풀 작업 스레드에서 연결 수락 작업을 처리하기 위해 submit 호출
	}

	void stopServer() {
		try {
			Iterator<Client> iterator = connections.iterator();// connections 컬렉션에서 반복자를 얻어냄
			while(iterator.hasNext()) { // 반복자를 반복함
				Client client = iterator.next(); // 반복자로부터 클라이언트를 하나씩 얻음
				client.socket.close(); // 클라이언트가 가지고 있는 socket을 닫음
				iterator.remove();// connections 컬렉션에서 클라이언트 제거
			}
		
			if(serverSocket!=null && !serverSocket.isClosed()) { // serversocket이 NULL이 아니고 닫혀있지 않다면
				serverSocket.close(); // 닫기
			}
		
			if(executorService!=null && !executorService.isShutdown()) { //executorservice가 NULL이 아니고 닫혀있지 않다면
				executorService.shutdown(); //executorservice 종료
			}
			
			System.out.print("Stop server\n");
		} catch (Exception e) { }
	}
	
	class Client { //내부 클래스 선언
		Socket socket; // 필드선언
		Client(Socket socket){ // 클라이언트 생성자 생성
			this.socket = socket; // 매개값으로 받은 socket을 필드값으로 저장
			receiveData(); // 메소드 호출
		}
		
		void receiveData() {
			Runnable runnable = new Runnable() { // 클라이언트로부터 데이터를 받는 작업을 Runnable로 정의
				@Override
				public void run() { // 메소드 재정의
					try {
						StringTokenizer st; // data를 나누기 위해 stringtokenizer 사용
						while(true) { // 작업 반복
							byte[] Data = new byte[100]; // 받은 데이터를 저장할 공간 선언
							String btNAME = "";// 버튼이름
							String ID = "";  // 아이디
							String CHAT = ""; // 채팅 내용
							//String name = ClientLogin.name;
							String name = null;
							
							InputStream isDATA = socket.getInputStream(); // socket으로부터 inputstream을 얻음
							int rbcData = isDATA.read(Data);  //inputstream의 read메소드 호출. 클라이언트가 데이터를 보내기 전까지 블로킹되고,
							// 데이터를 받으면 Data에 저장한 후 받은 바이트 개수를 rbcData에 저장
							if(rbcData == -1) {
								throw new IOException();
							}
							String message = "[Request handle: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							System.out.print(message + "\n");
							String DATA = new String(Data, 0, rbcData, "UTF-8"); // UTF-8로 디코딩한 문자열을 DATA에 저장
							System.out.println("Server receive DATA:" + DATA);
							//txLog.append("Server receive DATA:" + DATA + "\n");
							st = new StringTokenizer(DATA, " ");
							while(st.hasMoreTokens()) {//토큰이 존재하지 않을때 까지
								btNAME = st.nextToken();
								ID = st.nextToken();
								CHAT = st.nextToken();
								name = ID;
							}
							
							if(btNAME.equals("Send") && CHAT.equals("LoginD")) {
								//name = ID;
								serverMember.add(ID);
								CHAT = "";
							}
							//btNAME.equals("Send")
							else {
								for(Client client : connections) { // connections에 저장된 클라이언트 하나하나에게
									client.sendData(btNAME, ID, CHAT, serverMember);  // 버튼이름, id, 채팅내용, vector serverMember Elements를 매개값으로 넘겨줌
								}
							
							}
							System.out.println("Name : " + name);
							System.out.println("serverMemver : " + serverMember);
							
							
						}
					} catch(IOException e) {
						try {
							connections.remove(Client.this); //connections 컬렉션에서 예외가 발생한 클라이언트 제거
							String message = "[Client Disconnected: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							System.out.print(message + "\n");
							socket.close();
						} catch (IOException e2) {
							System.out.print(e2.getMessage() + "\n");
						}
					}
				}
			};
			
			executorService.submit(runnable);// 스레드풀에서 작업처리를 위해 submit 호출
		}
		
		void sendData(String Name, String ID, String Chat, Vector<String> serverMember) {
			Runnable runnable = new Runnable() { // 데이터를 클라이언트로 보내는 작업을 Runnable로 생성
				@Override
				public void run() { //run 재정의
					try {
						String data = Name + " " + ID + " " + Chat + " " + serverMember;
						// 매개값으로 받은 데이터들을 client에서 stringtokenizer사용을 위해 사이사이에 구분자 $$를 삽입
						System.out.println("Server send DATA:" + data); // 확인용출력
						//txLog.append("Server send DATA:" + data + "\n");
						byte[] DATA = data.getBytes("UTF-8");// 보낼 문자열로부터 UTF-8로 인코딩한 바이트 배열을 얻는다.
						OutputStream osData = socket.getOutputStream(); // socket에서 출력스트림을 얻음
						osData.write(DATA);// 바이트 배열을 매개값으로 write 호출
						osData.flush(); // 출력 스트림의 내부 버퍼를 비우기 위해 호출
					} catch(Exception e) {
						try {
							String message = "[Client communication error: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							System.out.print(message + "\n");
							connections.remove(Client.this);//connections 컬렉션에서 예외가 발생한 클라이언트 제거
							socket.close(); // socket닫음
						} catch (IOException e2) {}
					}
				}
			};
			
			executorService.submit(runnable);
		}
	}
	
	public static void main(String[] args) throws IOException {
		LobbyServer ls = new LobbyServer();
	}
}