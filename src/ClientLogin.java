 

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientLogin {

	public static void main(String[] args) {
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
		 
		 
		 }catch(FileNotFoundException e)
		 { 
		 serverIP=  "localhost";
		 nPort = 1024;
			}
		  
		try
		{ socket = new Socket(serverIP,nPort);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String inputMessage = null;
	
			String outputMessage;

			out.write("HELLO\n");
			out.flush();
			inputMessage = in.readLine();
			if(inputMessage.equals("WELCOME")) // 받은 문자가 
			{
				System.out.println("Welcome! 0): Leave\n1)Login\n2)Register\n");

			}
			else
			{ 
				System.out.println("ERROR: The server is Wrong. ");
				socket.close();
			}
			 
	 			int Input; 
			while(true)
			{	  
				System.out.println("First loop");
				outputMessage = stin.readLine();
				 System.out.println(outputMessage +" Sended");   
				 out.write(outputMessage+"\n"); 
				 out.flush();

				 System.out.println(" waitting for response...");   
			 inputMessage = in.readLine();  
			 System.out.println(inputMessage +" responsed by server");    
				 if(inputMessage.equals("0")) 
				 { 
						 break; 
				 }
				 else if(inputMessage.equals("1")) 
				 {	 
					 
					 /*
					  * 1. Server gives Client 1 , the ready of login ( login could talk it to server )
					  * 2. Client gives server ID and password
					  * 3. Server finds ID, If there is no ID, SEND ENID : Error , no id 
					  * 4. Server finds PW, if there is no PW, send EPWW : Error, wrong password 
					  * 5. Client go first room 
					  */ 
						 outputMessage = stin.readLine();
						 System.out.println(outputMessage +" as ID Sended");  // ID Send
						 out.write(outputMessage+"\n");  // CODE and Messages
						 out.flush();
						 
						 inputMessage = in.readLine(); // wait server to ready instruction
						 System.out.println(inputMessage +" received to server.");  
						 
							 if(inputMessage.equalsIgnoreCase("ENID")) 
							 {
								 System.out.println(inputMessage +" ERROR : NO ID exists. ");   
 
							 } 
						 else if(inputMessage.equals("CRID"))
						 {
							 System.out.println(inputMessage +" : ID exists. Enter PW  "); 
							 
							 outputMessage = stin.readLine();
							 System.out.println(outputMessage +" as PW Sended");   
							 out.write(inputMessage+" "+outputMessage+"\n");   
							 out.flush();
							 inputMessage = in.readLine();  
							 System.out.println(inputMessage +" received to server.");  
						 } 
							  String token[] = inputMessage.split(" ");
					 if(token[0].equals("LEND"))
					 {
					 	 System.out.println(token[1]+" received: Login complete.");  
					 }
				 }
				 else if(inputMessage.equals("2")) 
				 {	 System.out.println(inputMessage +" received: Send ID ");  
						outputMessage = stin.readLine();
						 System.out.println(outputMessage +" Sended");  
						 out.write(outputMessage+"\n");
						 out.flush(); 
						 inputMessage = in.readLine(); // wait server to ready instruction
						 System.out.println(inputMessage +" received");  
						 if(inputMessage.equals("EIDE"))
						 {	 
							 // If Using GUI, We should change it to button...
							 System.out.println("Error : Same ID already exists.");   
							 }
						 else // ID created, so PW Need to be created. 
						 {
						 inputMessage = stin.readLine();
						 System.out.println(inputMessage +" Sended");  
						 out.write("PW:"+inputMessage+"\n");
						 out.flush(); 
						 inputMessage = in.readLine(); // wait server to ready instruction
						 System.out.println(inputMessage +" received");  
					/// ... like this, repeat until required data received. after this, send this. 
						 out.write("CIIE\n"); // completed Inserting Inputs Ended
						 out.flush();
						 }
				 }
			}
			} catch (IOException e) { 
			} finally {
				try {
					socket.close(); 
				} catch (Exception e) {
					System.out.println("Error: Currently server hasn't opened / or having connection problem. check your IP address. ");
				}
			}
		}
	}

