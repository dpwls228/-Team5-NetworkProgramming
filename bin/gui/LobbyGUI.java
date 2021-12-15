package gui;

import java.awt.Container;
import java.awt.GridLayout;

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

public class LobbyGUI {
	
    final String[] btn_Title = {"A", "B"};
    // 버튼 Text 값 배열로 저장

    JFrame jp = new JFrame(); // JPanel생성
    JButton[] jbtn = new JButton[15]; // JButton을 담을수있는 그릇생성

    public LobbyGUI() {
        JFrame jf = new JFrame("Lobby"); // JFrame타이틀 지정
        jf.setSize(900, 600); // 창 크기 설정
        jf.setLayout(null);
        
        //메뉴바 설정
        JMenuBar menuBar = new JMenuBar();
        jf.setJMenuBar(menuBar);
        
        //메뉴바 요소 삽입
        JMenu mnNewMenu = new JMenu("Account");
        menuBar.add(mnNewMenu);
        JMenuItem mntmLogin = new JMenuItem("Login");
        mnNewMenu.add(mntmLogin);
        JMenuItem mntmLogout = new JMenuItem("Logout");
        mnNewMenu.add(mntmLogout);
        
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
        
        if(btn_Title.length != 0) {
        	for (int i = 0; i < btn_Title.length; i++) {
        		memButtonP.add(jbtn[i] = new JButton(btn_Title[i]));
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
    
    // threadLogin에서 아이디 받아와서 버튼 생성

    public static void main(String[] args) {
        LobbyGUI gui = new LobbyGUI();
    }
}