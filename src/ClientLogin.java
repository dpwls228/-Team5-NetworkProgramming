import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea; 
 

class PanelRegister extends JPanel{  
private PanelMain Main;
private JTextArea LoginText,PWText,UsernameText,UserEmailText ;
	public PanelRegister(PanelMain Main) {
		this.Main = Main;
		
		setLayout(null);
		 LoginText = new JTextArea();
		LoginText.setSize(100,20);
LoginText.setLocation(45,5);
LoginText.setText("ID");
add(LoginText);

JLabel IDHINT = new JLabel("ID:");
IDHINT.setBounds(10,5,30,20);
add(IDHINT);
 PWText = new JTextArea();
		PWText.setSize(100,20);
		PWText.setLocation(45,30);
		PWText.setText("PW");
		add(PWText); 
JLabel PWHINT = new JLabel("PW\t:");
PWHINT.setBounds(10,30,30,20);
add(PWHINT);

  UsernameText = new JTextArea();
UsernameText.setSize(100,20);
UsernameText.setLocation(80,55);
UsernameText.setText("Username");
add(UsernameText);
  JLabel UsernameHINT = new JLabel("Username:");
UsernameHINT.setBounds(10,55,1000,20);
add(UsernameHINT);
 

  UserEmailText = new JTextArea();
UserEmailText.setSize(100,20);
UserEmailText .setLocation(80,80);
UserEmailText .setText("Email");
add( UserEmailText );
JLabel UserEmailHINT = new JLabel("Email:");
UserEmailHINT.setBounds(10,80,1000,20);
add(UserEmailHINT);
 

		JButton LoginButton = new JButton("Back to Login"); 
		LoginButton.setSize(125,30);
		LoginButton.setLocation(25,105);
		LoginButton.addActionListener(new LoginListener());
		add(LoginButton);
		
		JButton RegisterButton = new JButton("Register"); 
		RegisterButton.setSize(90,30);
		RegisterButton.setLocation(45,140);
RegisterButton.addActionListener(new RegisterListener());
		add(RegisterButton);

 
		
	}

	
	class RegisterListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e)
		{      

			  
			BufferedReader in = null;  
			BufferedWriter out = null;
	 
	Socket socket =null; 
			 Scanner inputstream = null;

	String serverIP=null;
	int nPort =0;

			 try
			 { 
			inputstream = new Scanner(new File("server_info.dat"));  
			 String newInput = inputstream.nextLine();
			 serverIP=newInput.substring(newInput.indexOf(" ")+1);
			 newInput = inputstream.nextLine();
			 nPort = Integer.valueOf(  newInput.substring(newInput.indexOf(" ")+1)); 
			 
			 
			 }catch(FileNotFoundException e5)
			 { 
			 serverIP=  "localhost";
			 nPort = 1024;
				}
			  
			try
			{ socket = new Socket(serverIP,nPort);
				
				in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String inputMessage = null;
		
				String outputMessage;

				out.write("HELLO\n");
				out.flush();
				inputMessage = in.readLine();
				if(inputMessage.equals("WELCOME")) // 받은 문자가 
				{
					System.out.println("server is correct "); 
				}
				else
				{ 
					System.out.println("ERROR: The server is Wrong. ");
					Main.changeTo("Login");

					socket.close();
				} 
					outputMessage = String.valueOf(2); 
					 out.write(outputMessage+"\n"); 
					 out.flush();
 				 inputMessage = in.readLine();  // wait for response    
					 if(inputMessage.equals("2"))  // server sended it : means requirs ID input
					 {	 
							outputMessage = LoginText.getText(); //Send server ID 
							 out.write(outputMessage+"\n");
							 out.flush(); 
							 
							 inputMessage = in.readLine(); // wait server to ready instruction 
							 if(inputMessage.equals("EIDE")) // EIDE : Error: ID already EXists!
							 {	 
								 // Do Nothing due to Error , But not crash itself
								 System.out.println("Error : Same ID already exists.");   
								  
								 }
							 else // ID doesn't exist so created ID, so PW Need to be created. 
							 { 
								 outputMessage = PWText.getText(); 
								 out.write(outputMessage+"\n"); // Send PW to server
								 out.flush(); 
								 inputMessage = in.readLine(); // wait server to ready instruction   
								 outputMessage = UsernameText.getText(); // after receive server's response, give it Username 
								 out.write(outputMessage+"\n");
								 out.flush(); 
								 inputMessage = in.readLine(); // wait server to ready instruction  
								 outputMessage = UserEmailText.getText(); // give server User Email. 
								 out.write(outputMessage+"\n"); 
								 out.flush(); 
								 inputMessage = in.readLine(); // wait server to ready instruction 
								 out.write("CIIE\n"); // completed Inserting Inputs Ended
								 out.flush();
							 }
					 	}
					 } catch (IOException e3) { 
				} finally {
					try {
						socket.close(); 
					} catch (Exception e4) {
						System.out.println("Error: Currently server hasn't opened / or having connection problem. check your IP address. ");
					}
				}
			
		}
	}
	class LoginListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Main.setSize(260,120);
			Main.setLocationRelativeTo(null);
			Main.changeTo("login");
			
		}
	}
}
class PanelLogin extends JPanel {
private PanelMain Main;
private JTextArea LoginText,PWText;
	public PanelLogin(PanelMain Main) {
			this.Main = Main; 
			setLayout(null);
			LoginText = new JTextArea();
			LoginText.setSize(100,20);
			LoginText.setLocation(45,10);
			LoginText.setText("ID");
			add(LoginText);

			JLabel IDHINT = new JLabel("ID:");
			IDHINT.setBounds(10,10,50,20);
			add(IDHINT);

			PWText = new JTextArea();
			PWText.setSize(100,20);
			PWText.setLocation(45,40);
			PWText.setText("PW");
			add(PWText);

			JLabel PWHINT = new JLabel("PW\t:");
			PWHINT.setBounds(10,40,30,20);
			add(PWHINT);

			JButton LoginButton = new JButton("Login"); 
			LoginButton.setSize(90,30);
			LoginButton.setLocation(150,5);
			LoginButton.addActionListener(new LoginListener());
			add(LoginButton);
		
			JButton RegisterButton = new JButton("Register"); 
			RegisterButton.setSize(90,30);
			RegisterButton.setLocation(150,40);
			RegisterButton.addActionListener(new RegisterListener());
			add(RegisterButton);
		
	}

		
	class RegisterListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e)
			{    
				Main.setSize(200,220);
				Main.setLocationRelativeTo(null);
				Main.changeTo("register");
			}
		}
		class LoginListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				BufferedReader in = null; 
				BufferedReader stin = null;
				BufferedWriter out = null;
		 
				Socket socket =null; 
				Scanner inputstream = null;

				String serverIP=null;
				int nPort =0;

				 try
				 { 
					 inputstream = new Scanner(new File("server_info.dat"));  
					 String newInput = inputstream.nextLine();
					 serverIP=newInput.substring(newInput.indexOf(" ")+1);
					 newInput = inputstream.nextLine();
					 nPort = Integer.valueOf(  newInput.substring(newInput.indexOf(" ")+1)); 
				 
				 
				 }catch(FileNotFoundException e1)
				 { 
					 serverIP=  "localhost";
					 nPort = 1024;
					}
				  
				try
				{ 
					socket = new Socket(serverIP,nPort);
					
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					stin = new BufferedReader(new InputStreamReader(System.in));
					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String inputMessage = null;
			
					String outputMessage;

					out.write("HELLO\n");
					out.flush();
					inputMessage = in.readLine();
					if(inputMessage.equals("WELCOME")) 
					{ 

					}
					else
					{  
						System.out.println("ERROR: The server is Wrong. ");
						socket.close();
					}
					   
						outputMessage = String.valueOf(1);
						 System.out.println(outputMessage +" Sended");   
						 out.write(outputMessage+"\n"); 
						 out.flush();

						 System.out.println(" waitting for response...");   
						 inputMessage = in.readLine();  
						 System.out.println(inputMessage +" responsed by server");    
						 if(inputMessage.equals("0")) 
						 {  
						  
						 }
						 else if(inputMessage.equals("1")) 
						 {	  
								 outputMessage = LoginText.getText(); 
								 out.write(outputMessage+"\n");  // CODE and Messages
								 out.flush();
								 
								 inputMessage = in.readLine(); // wait server to ready instruction 
								 
									 if(inputMessage.equalsIgnoreCase("ENID")) 
									 { 
		 
									 } 
								 else if(inputMessage.equals("CRID"))
								 { 
									 
									 outputMessage = PWText.getText(); 
									 out.write(inputMessage+" "+outputMessage+"\n");   
									 out.flush();
									 inputMessage = in.readLine();   
								 } 
									  String token[] = inputMessage.split(" ");
							 if(token[0].equals("LEND"))
							 {
							 	 System.out.println(token[1]+" received: Login complete.");
							 	 inputMessage = in.readLine(); // Get another information from server!
								 System.out.println(inputMessage +" received");   // 
									  
							 }
						 }
				} catch (IOException e1) { 
					} finally {
						try {
							socket.close(); 
						} catch (Exception e2) { 
 
							Main.setLocationRelativeTo(null); 
							System.out.println("Error: Currently server hasn't opened / or having connection problem. check your IP address. ");
						}
						finally
						{
 
							Main.setLocationRelativeTo(null);
							Main.changeTo("login");
						}
 
					}
				}
			}
		}

class PanelMain extends JFrame{
	public PanelLogin login = null;
	public PanelRegister register = null;  
	public void changeTo(String target)
	{
		if(target == null)
		{

			getContentPane().removeAll();
			getContentPane().setSize(500,500);
			getContentPane().add(login); 
			revalidate();
			repaint();
		}
		else		if(target.equals("login"))
		{
			getContentPane().removeAll();
 
			
			getContentPane().add(login); 
			revalidate();
			repaint();
		} 
		else
		{
			getContentPane().removeAll();
			getContentPane().setSize(500,500);
			getContentPane().add(register); 
			revalidate();
			repaint();
		}
		
	}
}
public class ClientLogin extends JFrame{ 
	public static void main(String[] args) { 

  PanelMain Main = new PanelMain();
  Main.setTitle("Login");
  Main.login= new PanelLogin(Main);
  Main.register = new PanelRegister(Main); 
  Main.add(Main.login);
  Main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  Main.setSize(260,120); 
  Main.setLocationRelativeTo(null);
  Main.setResizable(false);
	Main.setVisible(true);  
		}
	} 