 
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
 

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import java.net.*;
import java.util.*;

public class LobbyServer extends JFrame{
	
	private Socket sock;
	ExecutorService executorService; // thread pool �ʵ� ����
	ServerSocket serverSocket; // serversocket �ʵ� ����
	List<Client> connections = new Vector<Client>(); // ����� Ŭ���̾�Ʈ�� ������ connections �ʵ� ����
	Vector<String> serverMember = new Vector<String>();
	
	public LobbyServer() {
		startServer();
	}
	
	void startServer() {
		executorService = Executors.newFixedThreadPool(100);
		
		// ��Ʈ �ѹ� �޾ƿ���
		File serverfile = new File("./serverInfo.txt");
		BufferedReader br;
		int nPort = 0;
		
		try {
 			FileReader reader = new FileReader(serverfile);
 			br = new BufferedReader(reader);
 			String portData = br.readLine();
 			nPort = Integer.parseInt(portData.split(" ")[1]);		// ���� ����   PORT: 9999
 		} catch (IOException e) {
 			nPort = 1024;
 		}
		
		try {
			serverSocket = new ServerSocket(); //serverSocket ��ü ����
			serverSocket.bind(new InetSocketAddress("localhost", nPort)); // 127.0.0.1:7121�� binding
		} catch(Exception e) {
			if(!serverSocket.isClosed()) {//���ܰ� �߻������� serversocket�� �������� ������
				stopServer(); // ȣ��(startServer�� ����)
			}
			return;
		}
		
		Runnable runnable = new Runnable() { // ���� ���� �۾��� Runnable�� ����
			@Override
			public void run() { // �޼ҵ带 ������
				System.out.print("Start server\n");
				while(true) { // ������ ������ ��ٸ�
					try {
						Socket socket = serverSocket.accept(); // Ŭ���̾�Ʈ�� �����û�� ��ٸ���, accept �޼ҵ� ȣ��
						String message = "[Accept Connect: " + socket.getRemoteSocketAddress()  + ": " + Thread.currentThread().getName() + "]";
						System.out.print(message + "\n");
						Client client = new Client(socket); // Ŭ���̾�Ʈ ��ü ����
						connections.add(client); // ������ Ŭ���̾�Ʈ ��ü�� connections �÷��ǿ� �߰�
						System.out.print("[Connect Count: " + connections.size() + "]\n");
						//�÷��ǿ� ����� Ŭ���̾�Ʈ ��ü�� ���� ���
					} catch (Exception e) {
						if(!serverSocket.isClosed()) { // ���ܹ߻��� ���������� ��������������
							stopServer();  // startServer�� �����ϰ�
						}
						break; // �����⸦ ����
					}
				}
			}
		};
		
		executorService.submit(runnable); // ������Ǯ �۾� �����忡�� ���� ���� �۾��� ó���ϱ� ���� submit ȣ��
	}

	void stopServer() {
		try {
			Iterator<Client> iterator = connections.iterator();// connections �÷��ǿ��� �ݺ��ڸ� ��
			while(iterator.hasNext()) { // �ݺ��ڸ� �ݺ���
				Client client = iterator.next(); // �ݺ��ڷκ��� Ŭ���̾�Ʈ�� �ϳ��� ����
				client.socket.close(); // Ŭ���̾�Ʈ�� ������ �ִ� socket�� ����
				iterator.remove();// connections �÷��ǿ��� Ŭ���̾�Ʈ ����
			}
		
			if(serverSocket!=null && !serverSocket.isClosed()) { // serversocket�� NULL�� �ƴϰ� �������� �ʴٸ�
				serverSocket.close(); // �ݱ�
			}
		
			if(executorService!=null && !executorService.isShutdown()) { //executorservice�� NULL�� �ƴϰ� �������� �ʴٸ�
				executorService.shutdown(); //executorservice ����
			}
			
			System.out.print("Stop server\n");
		} catch (Exception e) { }
	}
	
	class Client { //���� Ŭ���� ����
		Socket socket; // �ʵ弱��
		Client(Socket socket){ // Ŭ���̾�Ʈ ������ ����
			this.socket = socket; // �Ű������� ���� socket�� �ʵ尪���� ����
			receiveData(); // �޼ҵ� ȣ��
		}
		
		void receiveData() {
			Runnable runnable = new Runnable() { // Ŭ���̾�Ʈ�κ��� �����͸� �޴� �۾��� Runnable�� ����
				@Override
				public void run() { // �޼ҵ� ������
					try {
						StringTokenizer st; // data�� ������ ���� stringtokenizer ���
						while(true) { // �۾� �ݺ�
							byte[] Data = new byte[100]; // ���� �����͸� ������ ���� ����
							String btNAME = "";// ��ư�̸�
							String ID = "";  // ���̵�
							String CHAT = ""; // ä�� ����
							//String name = ClientLogin.name;
							String name = null;
							
							InputStream isDATA = socket.getInputStream(); // socket���κ��� inputstream�� ����
							int rbcData = isDATA.read(Data);  //inputstream�� read�޼ҵ� ȣ��. Ŭ���̾�Ʈ�� �����͸� ������ ������ ���ŷ�ǰ�,
							// �����͸� ������ Data�� ������ �� ���� ����Ʈ ������ rbcData�� ����
							if(rbcData == -1) {
								throw new IOException();
							}
							String message = "[Request handle: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							System.out.print(message + "\n");
							String DATA = new String(Data, 0, rbcData, "UTF-8"); // UTF-8�� ���ڵ��� ���ڿ��� DATA�� ����
							System.out.println("Server receive DATA:" + DATA);
							//txLog.append("Server receive DATA:" + DATA + "\n");
							st = new StringTokenizer(DATA, " ");
							while(st.hasMoreTokens()) {//��ū�� �������� ������ ����
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
								for(Client client : connections) { // connections�� ����� Ŭ���̾�Ʈ �ϳ��ϳ�����
									client.sendData(btNAME, ID, CHAT, serverMember);  // ��ư�̸�, id, ä�ó���, vector serverMember Elements�� �Ű������� �Ѱ���
								}
							
							}
							System.out.println("Name : " + name);
							System.out.println("serverMemver : " + serverMember);
							
							
						}
					} catch(IOException e) {
						try {
							connections.remove(Client.this); //connections �÷��ǿ��� ���ܰ� �߻��� Ŭ���̾�Ʈ ����
							String message = "[Client Disconnected: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							System.out.print(message + "\n");
							socket.close();
						} catch (IOException e2) {
							System.out.print(e2.getMessage() + "\n");
						}
					}
				}
			};
			
			executorService.submit(runnable);// ������Ǯ���� �۾�ó���� ���� submit ȣ��
		}
		
		void sendData(String Name, String ID, String Chat, Vector<String> serverMember) {
			Runnable runnable = new Runnable() { // �����͸� Ŭ���̾�Ʈ�� ������ �۾��� Runnable�� ����
				@Override
				public void run() { //run ������
					try {
						String data = Name + " " + ID + " " + Chat + " " + serverMember;
						// �Ű������� ���� �����͵��� client���� stringtokenizer����� ���� ���̻��̿� ������ $$�� ����
						System.out.println("Server send DATA:" + data); // Ȯ�ο����
						//txLog.append("Server send DATA:" + data + "\n");
						byte[] DATA = data.getBytes("UTF-8");// ���� ���ڿ��κ��� UTF-8�� ���ڵ��� ����Ʈ �迭�� ��´�.
						OutputStream osData = socket.getOutputStream(); // socket���� ��½�Ʈ���� ����
						osData.write(DATA);// ����Ʈ �迭�� �Ű������� write ȣ��
						osData.flush(); // ��� ��Ʈ���� ���� ���۸� ���� ���� ȣ��
					} catch(Exception e) {
						try {
							String message = "[Client communication error: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							System.out.print(message + "\n");
							connections.remove(Client.this);//connections �÷��ǿ��� ���ܰ� �߻��� Ŭ���̾�Ʈ ����
							socket.close(); // socket����
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