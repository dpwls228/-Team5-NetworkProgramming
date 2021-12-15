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
 

class check{
public boolean check(String a)
{
	/*
	 * 0 1 2 
	 * 3 4 5
	 *  6 7 8
	 */

boolean result = false;
	for(int i=0;i<3;i++)
	{
		if(a.charAt(i)== a.charAt(i+3)&&a.charAt(i+3) == a.charAt(i+6))
		{
			return true;
		}	
		if(a.charAt(i)== a.charAt(3*i)&&a.charAt(3*i+1) == a.charAt(3*i+2))
		{
			return true;
		}
	}
	if(a.charAt(0)== a.charAt(4)&&a.charAt(4) == a.charAt(8))
	return true; 

	if(a.charAt(2)== a.charAt(4)&&a.charAt(4) == a.charAt(6))
	return true; 
	
	return false; 

}
}
class PanelTicTacToeW extends JPanel
{
	private PanelMain Main;
	private JLabel  OneOne,OneTwo,OneThr,
	            TwoOne,TwoTwo,TwoThr,
	            ThrOne,ThrTwo,ThrThr;
	private String Stat, NowTeam;
	 

	private void setMain() {
		Main.connect(); 
	}

	   public PanelTicTacToeW(PanelMain Main, String Status) { 
	      this.Main = Main; 
	      this.Stat = Status;
	      setLayout(null);
	      System.out.println(Status+ " "+Status.length());
	      OneOne = new JLabel(Status.charAt(0) == '0'? " ": Status.charAt(0)=='1'?"O":"X");
	      OneOne.setSize(50,50);
	      OneOne.setLocation(10,10); 
	      add(OneOne);
	      OneTwo = new JLabel(Status.charAt(1) == '0'? " ": Status.charAt(1)=='1'?"O":"X");
	      OneTwo.setSize(50,50);
	      OneTwo.setLocation(70,10); 
	      add(OneTwo); 
	      OneThr = new JLabel(Status.charAt(2) == '0'? " ": Status.charAt(2)=='1'?"O":"X");
	      OneThr.setSize(50,50);
	      OneThr.setLocation(130,10); 
	      add(OneThr);

	      TwoOne = new JLabel(Status.charAt(3) == '0'? " ": Status.charAt(3)=='1'?"O":"X");
	      TwoOne.setSize(50,50);
	      TwoOne.setLocation(10,70); 
	      add(TwoOne);
	      
	      TwoTwo = new JLabel(Status.charAt(4) == '0'? " ": Status.charAt(4)=='1'?"O":"X");
	      TwoTwo.setSize(50,50);
	      TwoTwo.setLocation(70,70); 
	      add(TwoTwo); 
	      TwoThr = new JLabel(Status.charAt(5) == '0'? " ": Status.charAt(5)=='1'?"O":"X");
	      TwoThr.setSize(50,50);
	      TwoThr.setLocation(130,70); 
	      add(TwoThr);  
	      ThrOne = new JLabel(Status.charAt(6) == '0'? " ": Status.charAt(6)=='1'?"O":"X");
	      ThrOne.setSize(50,50);
	      ThrOne.setLocation(10,130); 
	      add(ThrOne);
	      
	      ThrTwo = new JLabel(Status.charAt(7) == '0'? " ": Status.charAt(7)=='1'?"O":"X");
	      ThrTwo.setSize(50,50);
	      ThrTwo.setLocation(70,130); 
	      add(ThrTwo); 
	      
	      ThrThr = new JLabel(Status.charAt(8) == '0'? " ": Status.charAt(8)=='1'?"O":"X");
	      ThrThr.setSize(50,50);
	      ThrThr.setLocation(130,130);    
	      add(ThrThr); 
	          
	      
	      }
 
	
}
class PanelTicTacToe extends JPanel {
private PanelMain Main;
private JTextArea LoginText,PWText;
private JButton OneOne,OneTwo,OneThr,
            TwoOne,TwoTwo,TwoThr,
            ThrOne,ThrTwo,ThrThr;
private String Stat, NowTeam;
 

private void setMain() {
	Main.connect(); 
}

   public PanelTicTacToe(PanelMain Main, String Status) { 
      this.Main = Main; 
      this.Stat = Status;
      setLayout(null);
      System.out.println(Stat+" "+Status+ " "+Status.length());
      OneOne = new JButton(Status.charAt(0) == '0'? " ": Status.charAt(0)=='1'?"O":"X");
      OneOne.setSize(50,50);
      OneOne.setLocation(10,10);
      OneOne.addActionListener(new OneOneListener());
      add(OneOne);
      OneTwo = new JButton(Status.charAt(1) == '0'? " ": Status.charAt(1)=='1'?"O":"X");
      OneTwo.setSize(50,50);
      OneTwo.setLocation(70,10);
      OneTwo.addActionListener(new OneTwoListener());
      add(OneTwo); 
      OneThr = new JButton(Status.charAt(2) == '0'? " ": Status.charAt(2)=='1'?"O":"X");
      OneThr.setSize(50,50);
      OneThr.setLocation(130,10);
      OneThr.addActionListener(new OneThrListener());
      add(OneThr);

      TwoOne = new JButton(Status.charAt(3) == '0'? " ": Status.charAt(3)=='1'?"O":"X");
      TwoOne.setSize(50,50);
      TwoOne.setLocation(10,70);
      TwoOne.addActionListener(new TwoOneListener());
      add(TwoOne);
      
      TwoTwo = new JButton(Status.charAt(4) == '0'? " ": Status.charAt(4)=='1'?"O":"X");
      TwoTwo.setSize(50,50);
      TwoTwo.setLocation(70,70);
      TwoTwo.addActionListener(new TwoTwoListener());
      add(TwoTwo); 
      TwoThr = new JButton(Status.charAt(5) == '0'? " ": Status.charAt(5)=='1'?"O":"X");
      TwoThr.setSize(50,50);
      TwoThr.setLocation(130,70);
      TwoThr.addActionListener(new TwoThrListener());
      add(TwoThr);  
      ThrOne = new JButton(Status.charAt(6) == '0'? " ": Status.charAt(6)=='1'?"O":"X");
      ThrOne.setSize(50,50);
      ThrOne.setLocation(10,130);
      ThrOne.addActionListener(new ThrOneListener());
      add(ThrOne);
      
      ThrTwo = new JButton(Status.charAt(7) == '0'? " ": Status.charAt(7)=='1'?"O":"X");
      ThrTwo.setSize(50,50);
      ThrTwo.setLocation(70,130);
      ThrTwo.addActionListener(new ThrTwoListener());
      add(ThrTwo); 
      
      ThrThr = new JButton(Status.charAt(8) == '0'? " ": Status.charAt(8)=='1'?"O":"X");
      ThrThr.setSize(50,50);
      ThrThr.setLocation(130,130);
      ThrThr.addActionListener(new ThrThrListener());      
      add(ThrThr); 
          
      }

   class OneOneListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {       
    	  if(Stat.charAt(0)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(0, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ " received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
   }
   class OneTwoListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {       
    	  /*
    	   * Give one player start with a trash message so that he could skip receivesend ,continuing the programs.
    	   * 
    	   */
    	  if(Stat.charAt(1)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(1, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
            System.out.println(builder.toString()+ " APPR" + Stat);
      
         
            try {  
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToeWatch(Stat);   
                Stat =Main.receiveandsend();
                Main.ChangeTicTacToe(Stat);
            } 
    	  } 
   }
  
   }
   class OneThrListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {          
    	  if(Stat.charAt(2)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(2, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
      } 
   class TwoOneListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {      
    	  if(Stat.charAt(3)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(3, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
   }
   class TwoTwoListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {        

    	  if(Stat.charAt(4)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(4, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
   }
   class TwoThrListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {      

    	  if(Stat.charAt(5)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(5, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
   }
   class ThrOneListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {       

    	  if(Stat.charAt(6)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(6, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
   }
   class ThrTwoListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {      

    	  if(Stat.charAt(7)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(7, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
   }
   class ThrThrListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e)
      {    

    	  if(Stat.charAt(8)=='0')
    	  { 
    	    StringBuilder builder = new StringBuilder(Stat);
            builder.setCharAt(8, '3'); // the player's set is Undefined in client level, so leave it as 3
            setMain(); 
      
            try { 
            	Stat = Main.SendAndReceive(builder.toString());
            	System.out.println(Stat+ "received!");
            }catch(Exception er)
            {
            	er.printStackTrace();
            }finally{

                Main.ChangeTicTacToe(Stat);  
            } 
    	  } 
      }
   }
}
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

				out.write("GAME\n");
				out.flush();
				inputMessage = in.readLine();
				if(inputMessage.equals("START")) // 받은 문자가 
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

					out.write("GAME\n");
					out.flush();
					inputMessage = in.readLine();
					if(inputMessage.equals("START")) 
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
								 System.out.println("Get pw + "+inputMessage);
									 if(inputMessage.equalsIgnoreCase("ENID")) 
									 { 
		 
									 } 
								 else if(inputMessage.equals("CRID"))
								 { 
									 
									 outputMessage = PWText.getText(); 
									 out.write(inputMessage+" "+outputMessage+"\n");   
									 out.flush();
									 inputMessage = in.readLine();   
									 System.out.println("GOT?" + inputMessage);
								 } 
									  String token[] = inputMessage.split(" ");
							 if(token[0].equals("LEND"))
							 {
							 	 System.out.println(token[1]+" received: Login complete.");
							 	 inputMessage = in.readLine(); // Get another information from server!
								 System.out.println(inputMessage +" received!");   // 
									  String Results[] = inputMessage.split(" ");
									  for(int i=0;i<Results.length;i++)
									  {
										  System.out.println(i+" "+Results[i].toString());
									  }
									  // Results[3] = username /  Results[5] = email // Socket = socket
									  System.out.println("username : "+Results[3]+ "\n email: "+ Results[5]);
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
	public PanelTicTacToe panelTTT= null;
	public PanelTicTacToeW panelTTTw = null;
	
	public PanelLogin login = null;
	public PanelRegister register = null;  
	 
	private BufferedReader in = null;  
	private BufferedWriter out = null;
	private Socket socket =null; 
	private Scanner inputstream = null;
	private String serverIP=null;
	private int nPort =0;

	public String receiveandsend() {
		String result="000000000";
		try {
			result = in.readLine();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		return result;
	}
	public BufferedReader getReader() {
		return this.in;
	}
	public BufferedWriter getWriter() {
	return this.out;
	}
	public Socket getSocket() {
		return this.socket;
	}
	public Scanner getScanner() {
		return this.inputstream;
	}
	 public String serverIP() {
		 return this.serverIP;
	 }
	 public int getPort() {
		 return this.nPort;
	 }
	 
	public void connect() {
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
		
		try {
			 socket = new Socket(serverIP,nPort);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			System.out.println("Server errors!");
		}
	}
	public String SendAndReceive(String input) {
	try
	{ 
		   System.out.println("connect complete"+input);
		out.write(input+"\n");
		out.flush();
		   System.out.println("send complete" + socket.getPort() + " " + socket.getInetAddress());
		String result =in.readLine();

		   System.out.println("receive complete"+result);
		   
		   result = in.readLine(); // wait for next player
		return result;
	}catch(IOException e)
	{
		e.getStackTrace();
	}finally{

	}
	System.out.println("ERROR?");
		 return input;
	}
	   public void ChangeTicTacToe(String target)
	   {  
		   System.out.println(target+ "Selected!");
	      getContentPane().removeAll();
	      this.panelTTT= new PanelTicTacToe(this, target);
	      getContentPane().add(panelTTT);
	      revalidate();
	      repaint();
	   }
	   public void ChangeTicTacToeWatch(String target)
	   { 
		   System.out.println(target+ "Selected!");
	      getContentPane().removeAll();
	      this.panelTTTw= new PanelTicTacToeW(this, target);
	      getContentPane().add(panelTTTw);
	      revalidate();
	      repaint();
		   
	   }
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