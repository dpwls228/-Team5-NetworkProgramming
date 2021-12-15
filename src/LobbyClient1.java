

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
	Socket socket; // Ŭ���̾�Ʈ ����� ���� socket �ʵ� ����
	String serverIP=null;
	int nPort = 0;
	
	void startClient() {
		// ������ Server ���� �о����
		int cnt = 0;
		
		File serverfile = new File(".\\serverInfo.txt");
		BufferedReader br;

 		try {
 			FileReader reader = new FileReader(serverfile);
 			br = new BufferedReader(reader);
 			String Data = br.readLine();
 			
 			while(Data != null) {
 				if(cnt == 0) {
 					nPort = Integer.parseInt(Data.split(" ")[1]);		// ���� ����   PORT: 9999
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
 		
 		// ������ ����
		Thread thread = new Thread() {
			@Override
			public void run() { // run �޼ҵ� ������
				try {
					socket = new Socket(); // socket ����
					socket.connect(new InetSocketAddress(serverIP, nPort)); // ����ȣ��Ʈ, nPort ��Ʈ�� �����û
					chattingLog.append("Connected at: " + socket.getRemoteSocketAddress() + "\n");
				} catch(Exception e) {
					chattingLog.append("CONNECT ERROR\n");
					if(!socket.isClosed()) { // ���ܹ߻������� ������ �������� ������
						stopClient();  // startClient ����
					}
				}
				receiveData();  // ȣ��
			}
		};
		
		thread.start(); // �۾������� ����
	}
	
	void stopClient() {
		try {
			chattingLog.append("DISCONNECTED\n");
			if(socket!=null && !socket.isClosed()) { // socket �ʵ尡 NULL�� �ƴϰ� �������� ������
				socket.close(); // socket�ݱ�
				Arrays.fill(member, null); // ������ ������ �������� ��� elements clear
			}
		} catch (IOException e) {}
	}
	
	void receiveData() {
		System.out.println("Start reciveData");
		try {
			byte[] Data = new byte[100];
			String btNAME = null; // ��ư�̸�
			String ID = null;   // ID
			String CHAT = null;   // ä�� ����
			String serverMember = null;  // server���� ������ member
			StringTokenizer st, st2;
			// data���� ��ư�̸�, ���̵�, ä�ó���, member�� �ѹ� ������ member�� �ٽ� �������ϱ� ������ stringtokenizer�ΰ� ����
			InputStream isData = socket.getInputStream();  // socket���κ��� inputstream�� ����
			int rbcData = isData.read(Data);  // inputstream�� read �޼ҵ� ȣ��
			chattingLog.append(isData + "\n" + rbcData);
			if(rbcData == -1){
				throw new IOException();
			}
			
			String DATA = new String(Data, 0, rbcData, "UTF-8"); // �����͸� �޾����� UTF-8�� ���ڵ��� ���ڿ��� ����
			System.out.println("Client receive DATA: " + DATA); // Ȯ�ο����
			st = new StringTokenizer(DATA, " "); // DATA�� "&&" �����ڷ� ����
			
			while(st.hasMoreTokens()) { // ��ū�� ������ ����
				System.out.println("tokens");
				btNAME = st.nextToken();
				ID = st.nextToken();
				CHAT = st.nextToken();
				serverMember = st.nextToken();
			}
			
			System.out.println(btNAME + " " + ID + " " + CHAT + " " + serverMember);
			//serverMember = "[A, B, C, D]";
			String serverMemberTemp= serverMember.replace("[", "").toString(); // "[" -> ����
			serverMemberTemp = serverMemberTemp.replace("]", "").toString();   // "]" -> ����
			serverMemberTemp = serverMemberTemp.replace(", ", " ").toString();// ", " -> $$
			st2 = new StringTokenizer(serverMemberTemp, " ");
			int i = 0;
			System.out.println(serverMember + " " + st2);
			while(st2.hasMoreTokens()) { // ��ū�� ����������
				Member[i] = st2.nextToken();
				i++;
			}
			if((btNAME.equals("Send") || btNAME.equals("chatField")) && !ID.equals(null)) { // ��ư�̸��� send�ų� chatField�̰� ID�� NULL�� �ƴҶ�
					chattingLog.append("> " + ID + ": " + CHAT + "\n");
			}
			else if(btNAME.equals("Quit")) {
				stopClient();
			}
			
		} catch (Exception e) {
			stopClient();//���ܹ߻��ϸ� ȣ��
		}	
	}
	
	void sendData(String Name, String ID, String Chat) {
		Thread thread = new Thread() { // �۾������� ����
			@Override
			public void run() { // run ������
				try {
					String data = Name + " " + ID + " " + Chat;
					System.out.println("Client sendDATA: " + data); //������ Ȯ�ο� ���
					byte[] DATA = data.getBytes("UTF-8"); // ���� ���ڿ����� UTF-8�� ���ڵ��� ����Ʈ �迭�� ��´�.
					OutputStream osData = socket.getOutputStream(); // socket���� ��� ��Ʈ���� ����
					osData.write(DATA); // ����Ʈ�迭�� �Ű������� write�޼ҵ� ȣ��
					osData.flush(); // ��½�Ʈ���� ���۸� ��쵵�� ȣ��
				} catch(Exception e) {
					stopClient(); // ���ܹ߻��ϸ� ȣ��
				}
			}
		};
		
		thread.start();
	}
	
	public LobbyClient1() {
		JFrame jf = new JFrame("Lobby"); // JFrameŸ��Ʋ ����
        jf.setSize(900, 600); // â ũ�� ����
        jf.setLayout(null);
        
        //Members ���� ���� �� ����
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
        
        
        //Chatting ���� ���� �� ����
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
        //�׼Ǹ����� �߰�
        sendB.addActionListener(myBTListener);
	}
	
	public static void main(String[] args) {
		LobbyClient1 lc = new LobbyClient1();
	}
	
	
	class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			String name = e.getActionCommand(); // ��ư�� ������ ��ư�̸��� name�� ����
			
			
			if(checkInfo == false) {
				loginID = chat.getText();
			}
			
			if(checkInfo == true) {// ���� �������� ture�̸�
				Chat = chat.getText();
				chattingLog.append("[" + loginID + "]" + Chat + "\n");
				sendData(name, loginID, Chat);
				chat.setText("");
			}
			
			if(name.equals("Send") && checkInfo == false) { // ��ư�̸��� send, chatField���
				if(loginID.equals("")) { // id�� ��ĭ�϶�
					chattingLog.append("You need to login first.\n");
					chat.setText("");// �ؽ�Ʈ�ʵ� �ʱ�ȭ
				}
				else {
					chattingLog.append(loginID + " Entered chatting.\n");
					checkInfo = true;
					sendData(name, loginID, Chat);
					Chat = null;
					chat.setText("");// �ؽ�Ʈ�ʵ� �ʱ�ȭ
					
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