package gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UsersNStartGUI extends JPanel{
	String str = new String();
	public UsersNStartGUI() {
		
	}
	public UsersNStartGUI(String str) {
		String s = new String();
	    String url = str + ".txt";
	    File file = new File(".\\users", url);
	    JFrame jf = new JFrame(); // JFrame타이틀 지정
	    jf.setSize(900, 600); // 창 크기 설정
	    jf.setLayout(null);
	    
	    //Members 영역 지정 및 구성
	        JPanel membersPanel = new JPanel();
	        membersPanel.setLayout(null);
	        membersPanel.setBounds(20, 10, 310, 550);
	        jf.add(membersPanel);
	        JLabel titleMembers = new JLabel(str);
	        titleMembers.setBounds(0, 0, 310, 20);
	        membersPanel.add(titleMembers);
	        JPanel memDataP = new JPanel();
	        memDataP.setBounds(0, 20, 310, 350);
	        membersPanel.add(memDataP);
	        JTextArea userData = new JTextArea();
	        //userData.setBounds(0, 0, 310, 350);
	        userData.setLayout(null);
	        memDataP.add(userData);
	        
	        if(file.exists()) {
	        	BufferedReader inFile = null;
				try {
					inFile = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	String sLine = null;
	        	try {
					while( (sLine = inFile.readLine()) != null ) {
						s += sLine;
						s += "\n";
						//userData.setText(sLine);
						//memDataP.add(userData);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	System.out.println(s);
	        }
	        else {
	        	System.out.println("There is no such file.");
	        }
	        userData.setText(s);
			memDataP.add(userData);
			
			
	        JButton quit = new JButton("Quit");
	        quit.setBounds(0, 500, 310, 30);
	        membersPanel.add(quit);
	        JButton game = new JButton("Start Game");
	        game.setBounds(0, 450, 310, 30);
	        membersPanel.add(game);
	        
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
	        
	        JTextArea chattingLog = new JTextArea();
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
	        
	        JTextField chat = new JTextField("");
	        chat.setBounds(0, 10, 430, 20);
	        sendP.add(chat);

	        jf.setVisible(true);
    }
	
	static public void main(String[] args) {
		UsersNStartGUI gui = new UsersNStartGUI("A");
	}
}
