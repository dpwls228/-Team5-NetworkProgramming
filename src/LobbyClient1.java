

import javax.swing.*; 
import javax.swing.event.*;
 
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.nio.channels.MembershipKey;

import java.awt.*;
import java.awt.List;

import java.util.*;

public class LobbyClient1 {
	Socket soc = new Socket();
	JTextArea chattingLog = new JTextArea();
	JTextField chat = new JTextField("");
	JButton[] member = new JButton[100];
	String[] Member = new String[100];
	Vector<String> serverMember = new Vector<String>();
	
	boolean checkInfo = false;
	String loginID = null;
	String Chat = "LoginD";
	
	MyActionListener myBTListener = new MyActionListener();
	Socket socket; // 클라이언트 통신을 위해 socket 필드 선언
	String serverIP=null;
	int nPort = 0;
	
	void startClient() {
		// 연결할 Server 정보 읽어오기
		int cnt = 0;
		
		File serverfile = new File(".\\serverInfo.txt");
		BufferedReader br;

 		try {
 			FileReader reader = new FileReader(serverfile);
 			br = new BufferedReader(reader);
 			String Data = br.readLine();
 			
 			while(Data != null) {
 				if(cnt == 0) {
 					nPort = Integer.parseInt(Data.split(" ")[1]);		// 파일 형식   PORT: 9999
 					cnt++;
 				}
 				else if(cnt == 1) {
 					serverIP = Data.split(" ")[1];
 				}
 				Data = br.readLine();
 			}
 			
 		} catch (Exception e) {
 			serverIP=  "localhost";
 			nPort = 1024;
 		}
 		
 		// 스레드 정의
		Thread thread = new Thread() {
			@Override
			public void run() { // run 메소드 재정의
				try {
					socket = new Socket(); // socket 생성
					socket.connect(new InetSocketAddress(serverIP, nPort)); // 로컬호스트, nPort 포트로 연결요청
					chattingLog.append("Connected at: " + socket.getRemoteSocketAddress() + "\n");
				} catch(Exception e) {
					chattingLog.append("CONNECT ERROR\n");
					if(!socket.isClosed()) { // 예외발생했을때 소켓이 닫혀있지 않으면
						stopClient();  // startClient 종료
					}
				}
				receiveData();  // 호출
			}
		};
		
		thread.start(); // 작업스레드 시작
	}
	
	void stopClient() {
		try {
			chattingLog.append("DISCONNECTED\n");
			if(socket!=null && !socket.isClosed()) { // socket 필드가 NULL이 아니고 닫혀있지 않으면
				socket.close(); // socket닫기
				Arrays.fill(member, null); // 서버와 연결이 끊겼을때 멤버 elements clear
			}
		} catch (IOException e) {}
	}
	
	void receiveData() {
		System.out.println("Start reciveData");
		try {
			byte[] Data = new byte[100];
			String btNAME = null; // 버튼이름
			String ID = null;   // ID
			String CHAT = null;   // 채팅 내용
			String serverMember = null;  // server에서 보내준 member
			StringTokenizer st, st2;
			// data에서 버튼이름, 아이디, 채팅내용, member로 한번 나눈후 member를 다시 나눠야하기 떄문에 stringtokenizer두개 선언
			InputStream isData = socket.getInputStream();  // socket으로부터 inputstream을 받음
			int rbcData = isData.read(Data);  // inputstream의 read 메소드 호출
			chattingLog.append(isData + "\n" + rbcData);
			if(rbcData == -1){
				throw new IOException();
			}
			
			String DATA = new String(Data, 0, rbcData, "UTF-8"); // 데이터를 받았을때 UTF-8로 디코딩한 문자열을 얻음
			System.out.println("Client receive DATA: " + DATA); // 확인용출력
			st = new StringTokenizer(DATA, " "); // DATA를 "&&" 구분자로 나눔
			
			while(st.hasMoreTokens()) { // 토큰이 없을때 까지
				System.out.println("tokens");
				btNAME = st.nextToken();
				ID = st.nextToken();
				CHAT = st.nextToken();
				serverMember = st.nextToken();
			}
			
			System.out.println(btNAME + " " + ID + " " + CHAT + " " + serverMember);
			//serverMember = "[A, B, C, D]";
			String serverMemberTemp= serverMember.replace("[", "").toString(); // "[" -> 공백
			serverMemberTemp = serverMemberTemp.replace("]", "").toString();   // "]" -> 공백
			serverMemberTemp = serverMemberTemp.replace(", ", " ").toString();// ", " -> $$
			st2 = new StringTokenizer(serverMemberTemp, " ");
			int i = 0;
			System.out.println(serverMember + " " + st2);
			while(st2.hasMoreTokens()) { // 토큰이 없을때까지
				Member[i] = st2.nextToken();
				i++;
			}
			if((btNAME.equals("Send") || btNAME.equals("chatField")) && !ID.equals(null)) { // 버튼이름이 send거나 chatField이고 ID가 NULL이 아닐때
					chattingLog.append("> " + ID + ": " + CHAT + "\n");
			}
			else if(btNAME.equals("Quit")) {
				stopClient();
			}
			
		} catch (Exception e) {
			stopClient();//예외발생하면 호출
		}	
	}
	
	void sendData(String Name, String ID, String Chat) {
		Thread thread = new Thread() { // 작업스레드 생성
			@Override
			public void run() { // run 재정의
				try {
					String data = Name + " " + ID + " " + Chat;
					System.out.println("Client sendDATA: " + data); //데이터 확인용 출력
					byte[] DATA = data.getBytes("UTF-8"); // 보낼 문자열에서 UTF-8로 인코딩한 바이트 배열을 얻는다.
					OutputStream osData = socket.getOutputStream(); // socket에서 출력 스트림을 얻음
					osData.write(DATA); // 바이트배열을 매개값으로 write메소드 호출
					osData.flush(); // 출력스트림의 버퍼를 비우도록 호출
				} catch(Exception e) {
					stopClient(); // 예외발생하면 호출
				}
			}
		};
		
		thread.start();
	}
	
	public LobbyClient1() {
		JFrame jf = new JFrame("Lobby"); // JFrame타이틀 지정
        jf.setSize(900, 600); // 창 크기 설정
        jf.setLayout(null);
        
        //Members 영역 지정 및 구성
        JPanel membersPanel = new JPanel();
        membersPanel.setLayout(null);
        membersPanel.setBounds(20, 10, 310, 510);
        jf.add(membersPanel);
        JLabel titleMembers = new JLabel("Members");
        titleMembers.setBounds(0, 0, 310, 20);
        membersPanel.add(titleMembers);
        JPanel memButtonP = new JPanel();
        memButtonP.setBounds(0, 20, 310, 450);
        membersPanel.add(memButtonP);
        JButton quit = new JButton("Quit");
        quit.setBounds(0, 480, 310, 20);
        membersPanel.add(quit);
        
        
        System.out.println(Member);
        if(Member.length != 0) {
        	for (int i = 0; i < Member.length; i++) {
        		if(Member.equals("")) {
        			break;
        		}
        		memButtonP.add(member[i] = new JButton(Member[i]));
        		member[i].addActionListener(myBTListener);
        	}
        }
        
        
        //Chatting 영역 지정 및 구성
        JPanel chattingPanel = new JPanel();
        chattingPanel.setLayout(null);
        chattingPanel.setBounds(350, 10, 520, 510);
        jf.add(chattingPanel);
        
        JLabel titleChatting = new JLabel("Chatting");
        titleChatting.setBounds(0, 0, 520, 20);
        chattingPanel.add(titleChatting);
        
        JPanel usersChatP = new JPanel();
        usersChatP.setLayout(null);
        usersChatP.setBounds(0, 20, 520, 500);
        chattingPanel.add(usersChatP);
        
        
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 520, 450);   // chatting room
		usersChatP.add(scrollPane);
		chattingLog.setEditable(false);
		chattingLog.setLineWrap(true);
		scrollPane.setViewportView(chattingLog);
        
		JPanel sendP = new JPanel();
        sendP.setLayout(null);
        sendP.setBounds(0, 450, 520, 40);
        usersChatP.add(sendP);
		
        JButton sendB = new JButton("Send");
        sendB.setBounds(440, 10, 70, 20);
        sendP.add(sendB);
        
        
        chat.setBounds(0, 10, 430, 20);
        sendP.add(chat);

        jf.setVisible(true);
        
        startClient();
        //액션리스너 추가
        sendB.addActionListener(myBTListener);
	}
	
	public static void main(String[] args) {
		LobbyClient1 lc = new LobbyClient1();
	}
	
	
	class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			String name = e.getActionCommand(); // 버튼이 눌리면 버튼이름을 name에 저장
			
			
			if(checkInfo == false) {
				loginID = chat.getText();
			}
			
			if(checkInfo == true) {// 만약 검증값이 ture이면
				Chat = chat.getText();
				chattingLog.append("[" + loginID + "]" + Chat + "\n");
				sendData(name, loginID, Chat);
				chat.setText("");
			}
			
			if(name.equals("Send") && checkInfo == false) { // 버튼이름이 send, chatField라면
				if(loginID.equals("")) { // id가 빈칸일때
					chattingLog.append("You need to login first.\n");
					chat.setText("");// 텍스트필드 초기화
				}
				else {
					chattingLog.append(loginID + " Entered chatting.\n");
					checkInfo = true;
					sendData(name, loginID, Chat);
					Chat = null;
					chat.setText("");// 텍스트필드 초기화
					
				}
			}
			
			//int index = 0;
			if(!name.equals("Send") && !name.equals("Quit")) {
				int index = Arrays.asList(Member).indexOf(name);
				UsersNStartGUI user = new UsersNStartGUI(Member[index]);
				user.setVisible(true);
			}
		}
	}	
}