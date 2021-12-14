import java.io.*;
import java.math.BigInteger;
import java.security.*;
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
  
  

	System.out.println("First loop"); 
  inputMessage = in.readLine();

	 AES256 encoder = new AES256();
  
  String outputMessage =null;
  outputMessage = String.valueOf(inputMessage); 
 
  System.out.println(inputMessage+ "Connected!");

	out.write(inputMessage+"\n");
	out.flush();
  if(inputMessage.equalsIgnoreCase("0")) {
  
  }
  else if( inputMessage.equals("1"))  
  {  
		   inputMessage = in.readLine();  
		   System.out.println("Client send1: "+inputMessage);
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
	    
 
    String pwLine = pwReader.readLine();
 
	   String ptoken[] = inputMessage.split(" "); 
	   System.out.println("Client send2: "+inputMessage);
	   System.out.println(inputMessage+ " TK1: " +ptoken[0]+ "TK2:"+ ptoken[1]);	 
	   String pwstring = encoder.encrypt(ptoken[1]);
//System.out.println("Test as: +"+pwLine+"to" +encoder.decrypt(pwLine)+ "|" + " to "+encoder.decrypt(pwstring)+ " was " + pwstring);
//	   System.out.println(pwLine+" "+pwLine.length()+"|"+pwstring+" "+pwstring.length()+ " " +pwstring.equals(ptoken[1])); 
//	   System.out.println(encoder.decrypt(pwLine)+"line"+encoder.decrypt(pwLine).length()+" "+encoder.decrypt(pwstring) +"as "+encoder.decrypt(pwstring).length()+ " "+encoder.decrypt(pwLine).equals(encoder.decrypt(pwstring)) +"is win?" + encoder.decrypt(pwLine).equals(encoder.decrypt(pwstring)));
//for(int i=0; i<encoder.decrypt(pwLine).length();i++)
//{
//	System.out.println(i+"th : "+encoder.decrypt(pwLine).charAt(i));
//}for(int i=0; i<encoder.decrypt(pwstring).length();i++)
//{
//	System.out.println(i+"st : "+encoder.decrypt(pwstring).charAt(i));
//} TODO: USER ERROR / LOGIN sequence implement
	   if(encoder.decrypt(pwLine).equals(encoder.decrypt(pwstring))==true)
	   {
		   System.out.println("Login complete as ID of "+ UserID);
		   out.write("LEND "+UserID+"\n");  
		   out.flush();    
		   out.write("LEND UI:"+UserID+" Email: "+pwReader.readLine() +"Username: "+pwReader.readLine() +"\n");  
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

  inputMessage = in.readLine();   
  PrintWriter UWriter = new PrintWriter(new FileWriter(UID));  
	  String pwEcd = encoder.encrypt(inputMessage);

	 		
  UWriter.println(pwEcd);  

  System.out.println(inputMessage+" Printed!");
  out.write("PWDN"+"\n");   // password done
  out.flush();
  
  inputMessage = in.readLine();   
  UWriter.println(inputMessage);  
  System.out.println(inputMessage+" Printed!");
  out.write("UNDN"+"\n");   // Username done
  out.flush();

  inputMessage = in.readLine();   
  UWriter.println(inputMessage);   
  out.write("EMDN"+"\n");   // Email done
  out.flush();
  
  
  UWriter.close();
  System.out.println(inputMessage+" Printed!");
  out.write("CIID"+"\n");  
  out.flush();
 
  
	   
	   }catch(IOException e) {
		     e.printStackTrace();
	   }finally {
		   
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
 
