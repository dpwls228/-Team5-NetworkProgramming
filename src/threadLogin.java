import java.io.*;
import java.util.*;
import java.net.*;

import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class threadLogin extends Thread {
	BufferedReader in = null; 
	BufferedWriter out = null;
 
	Socket socket =null;

	 Scanner inputstream = null;
	public threadLogin(Socket sock)
	{ 
		socket = sock;
	} 
	
	@Override
	public	void run() { 
try
{ 
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


String inputMessage;
inputMessage = in.readLine(); 
if(inputMessage.equals("HELLO"))
{ 
	System.out.println("Client added" + inputMessage); 
	out.write("WELCOME\n");
	out.flush();
}
else
{ 
	System.out.println("WRONG Client added");
	 socket.close();
}
 
String PathPointer = "./Server";
File ServerPointer = null;
Path path = Paths.get(PathPointer); 
 
if(!Files.exists(path) && !Files.isDirectory(path))
{
ServerPointer = new File(PathPointer);
ServerPointer.mkdir();
}  
PathPointer = "./Server/Users";
path = Paths.get(PathPointer);

if(!Files.exists(path) && !Files.isDirectory(path))
{
ServerPointer = new File(PathPointer);
ServerPointer.mkdir();
}  
  
 
while(true) { 

	System.out.println("First loop"); 
  inputMessage = in.readLine();
   
  
  String outputMessage =null;
  outputMessage = String.valueOf(inputMessage); 
  String token[] = inputMessage.split(" "); 
  System.out.println(inputMessage+ "Connected!");

	out.write(inputMessage+"\n");
	out.flush();
  if(inputMessage.equalsIgnoreCase("0")) {
 
	  break;
  }
  else if( inputMessage.equals("1"))  
  {  
		   inputMessage = in.readLine();  
		   path = Paths.get(PathPointer+"/"+inputMessage);
		   System.out.println(path +" "+ Files.exists(path)+ inputMessage);
		   if(!Files.exists(path)) {  
			   out.write("ENID"+"\n");
			   out.flush();  
		   } 
		   else
		   {  
			   out.write("CRID"+"\n");
			   System.out.println("ID is correct as "+ inputMessage);
			   out.flush();   
			   String UserID = inputMessage; 
try {
 
	   File UID = new File(PathPointer+"/"+inputMessage+"/data.txt");
	   inputMessage = in.readLine(); 
	   FileReader pw = new FileReader(UID);
	   BufferedReader pwReader = new BufferedReader(pw);
	   String pwLine = pwReader.readLine().substring(3);

	   String ptoken[] = inputMessage.split(" ");

	   System.out.println(pwLine+" "+pwLine.length()+"|"+inputMessage+" "+ptoken.length + ptoken[1]+"+"+ptoken[1].length()+pwLine.equals(ptoken[1])); 
	   System.out.println("PW : "+ ptoken[1]+" \nUI: "+inputMessage); 
	   if(pwLine.equals(ptoken[1]))
	   {

		   System.out.println("Login complete as ID of "+ UserID);

		   out.write("LEND "+UserID+"\n");  
		   out.flush();    
	   }
	   else
	   {

		   out.write("ENPW"+"\n");
		   out.flush();   
	   }
	   
}catch(FileNotFoundException e)
{
	
}catch(IOException e)
{
	
}
finally{
	
}
			   
		   } 
  }
  else if( inputMessage.equals("2"))  
  {

	   inputMessage = in.readLine();  
	   path = Paths.get(PathPointer+"/"+inputMessage);
	   System.out.println(path +" "+ Files.exists(path)+ inputMessage);
	   if(Files.exists(path)) {  
		   out.write("EIDE"+"\n"); 
		   out.flush();  
	   } 
	   else
	   {  
		   out.write("CRID"+"\n");  
		   System.out.println("ID is correct as "+ inputMessage);
		   out.flush();   
		   String UserID = inputMessage;  
		   File UID = new File(PathPointer+"/"+inputMessage);
		   UID.mkdir(); 
try {

   UID = new File(PathPointer+"/"+inputMessage+"/data.txt");
  UID.createNewFile();
 
  PrintWriter UWriter = new PrintWriter(new FileWriter(UID)); 
  do { 
	  inputMessage = in.readLine();  
  UWriter.println(inputMessage);  
  System.out.println(inputMessage+" Printed!");
  out.write("CIID"+"\n");  
  out.flush();
	  
  		}while(!inputMessage.equals("CIIE"));  
  UWriter.close();
	   }catch(IOException e) {
		   e.printStackTrace();
	   }finally {
		   
	   }
	   }
  }
} 
  }catch(Exception e){ 
	  e.printStackTrace();
	}finally{ 
		System.out.println("Client quited");
	try { 
	socket.close(); 
	}catch(IOException e)
	{ 
		e.printStackTrace(); 
	}	
	}

}
	} 
 
