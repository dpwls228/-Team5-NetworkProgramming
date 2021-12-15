import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.net.*;

import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class AESencryption{

    public static String alg = "AES/CBC/PKCS5Padding";
    private final String key = "10241048102410481024104810241048"; 
    private final String iv = key.substring(0, 16); // 16byte
    
    public String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec); 
        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }	
}
public class game_thread extends Thread {
	BufferedReader in = null;
	BufferedWriter out = null;

	Socket socket = null;
	game_server server = null;

	Vector<game_thread> auser; // �뿰寃곕맂 紐⑤뱺 �겢�씪�씠�뼵�듃
	Vector<game_thread> wuser; // ��湲곗떎�뿉 �엳�뒗 �겢�씪�씠�뼵�듃
	Vector<game_room> room; // �깮�꽦�맂 Room

	Scanner inputstream = null;

	game_room myRoom;
	game_file db;
	String nickname = null;

	public game_thread(Socket sock, game_server server) {
		this.socket = sock;
		this.server = server;

		auser = server.alluser;
		wuser = server.waituser;
		room = server.room;
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String inputMessage;
			inputMessage = in.readLine();
			if (inputMessage.equals("GAME")) {
				System.out.println("Client added" + inputMessage);
				out.write("START\n");
				out.flush();
			} else {
				System.out.println("WRONG Client added");
				socket.close();
			}

			String PathPointer = "./Server";
			File ServerPointer = null;
			Path path = Paths.get(PathPointer);

			if (!Files.exists(path) && !Files.isDirectory(path)) {
				ServerPointer = new File(PathPointer);
				ServerPointer.mkdir();
			}
			PathPointer = "./Server/Users";
			path = Paths.get(PathPointer);

			if (!Files.exists(path) && !Files.isDirectory(path)) {
				ServerPointer = new File(PathPointer);
				ServerPointer.mkdir();
			}
 

			System.out.println("First loop"); 
		  inputMessage = in.readLine();

			 AESencryption encoder = new AESencryption();
		  
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
			   System.out.println(pwLine + " @ " + pwstring);
			   if(encoder.decrypt(pwLine).equals(encoder.decrypt(pwstring))==true)
			   {
				   System.out.println("Login complete as ID of "+ UserID);
				   out.write("LEND "+UserID+"\n");  
				   out.flush();    
				   out.write("LEND UI:"+UserID+" Email: "+pwReader.readLine() +" Username: "+pwReader.readLine() +"\n");  
				   out.flush();    
			   }
			   else
			   {
				   System.out.println("Login INcomplete as ID of "+ UserID);
				   out.write("ENPW"+"\n");
				   out.flush();   
			   }
			   
		}catch(Exception e)
		{
		e.getStackTrace();	 
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
		 
		  
			   
			   }catch(Exception e) {
				     e.printStackTrace();
			   }finally {
				   
			   }
			   }
		  }else	if(inputMessage.equalsIgnoreCase("3")) {
				myRoom = new game_room();	//�깉濡쒖슫 Room 媛앹껜 �깮�꽦 �썑 myRoom�뿉 珥덇린�솕
				myRoom.title = myRoom.count;	//諛� �젣紐⑹쓣 m[1]濡� �꽕�젙
				myRoom.count++;			//諛⑹쓽 �씤�썝�닔 �븯�굹 異붽�
				
				room.add(myRoom);		//room 諛곗뿴�뿉 myRoom�쓣 異붽�
				
				myRoom.ccu.add(this);	//myRoom�쓽 �젒�냽�씤�썝�뿉 �겢�씪�씠�뼵�듃 異붽�
				wuser.remove(this);		//��湲곗떎 �젒�냽 �씤�썝�뿉�꽌 �겢�씪�씠�뼵�듃 �궘�젣
				
				out.write("3" + "//OKAY");
				System.out.println("[Server] "+"諛� '" + myRoom.count+ "' �깮�꽦");
				
				sendWait(roomInfo());	//��湲곗떎 �젒�냽 �씤�썝�뿉 諛� 紐⑸줉�쓣 �쟾�넚
				sendRoom(roomUser());	//諛⑹뿉 �엯�옣�븳 �씤�썝�뿉 諛� �씤�썝 紐⑸줉�쓣 �쟾�넚
			}  //諛� �깮�꽦 if臾�

			/* 諛� �엯�옣 */
			else if(inputMessage.equalsIgnoreCase("4")) {
				for(int i=0; i<room.size(); i++) {	//�깮�꽦�맂 諛⑹쓽 媛쒖닔留뚰겮 諛섎났
					game_room r = room.get(i);
					if(r.title == r.count) {	//諛� �젣紐⑹씠 媛숆퀬
						
						if(r.count < 2) {			//諛� �씤�썝�닔媛� 2紐낅낫�떎 �쟻�쓣 �븣 �엯�옣 �꽦怨�
							myRoom = room.get(i);	//myRoom�뿉 �몢 議곌굔�씠 留욌뒗 i踰덉㎏ room�쓣 珥덇린�솕
							myRoom.count++;			//諛⑹쓽 �씤�썝�닔 �븯�굹 異붽�
							
							wuser.remove(this);		//��湲곗떎 �젒�냽 �씤�썝�뿉�꽌 �겢�씪�씠�뼵�듃 �궘�젣
							myRoom.ccu.add(this);	//myRoom�쓽 �젒�냽 �씤�썝�뿉 �겢�씪�씠�뼵�듃 異붽�
							
							sendWait(roomInfo());	//��湲곗떎 �젒�냽 �씤�썝�뿉 諛� 紐⑸줉�쓣 �쟾�넚
							sendRoom(roomUser());	//諛⑹뿉 �엯�옣�븳 �씤�썝�뿉 諛� �씤�썝 紐⑸줉�쓣 �쟾�넚
							
							out.write("4" + "//OKAY");
							System.out.println("[Server] "+"諛� '" + myRoom.count+ "' �엯�옣");
						}
						
						else {	//諛� �씤�썝�닔媛� 2紐� �씠�긽�씠誘�濡� �엯�옣 �떎�뙣
							out.write("4" + "//FAIL");
							System.out.println("[Server] "+"諛� '" + myRoom.count+ "' �씤�썝珥덇낵");
						}
					}
					
					else {	//媛숈� 諛� �젣紐⑹씠 �뾾�쑝�땲 �엯�옣 �떎�뙣
						out.write("4" + "//FAIL");
						System.out.println("[Server] "+"諛� '" + myRoom.count+ "' �엯�옣�삤瑜�");
					}
				}
			} //諛� �엯�옣 if臾�
			
			/* �봽濡쒓렇�옩 醫낅즺 */
			else if(inputMessage.equalsIgnoreCase("5")) {
				auser.remove(this);		//�쟾泥� �젒�냽 �씤�썝�뿉�꽌 �겢�씪�씠�뼵�듃 �궘�젣
				wuser.remove(this);		//��湲곗떎 �젒�냽 �씤�썝�뿉�꽌 �겢�씪�씠�뼵�듃 �궘�젣
				
				sendWait(connectedUser());	//��湲곗떎 �젒�냽 �씤�썝�뿉 �쟾泥� �젒�냽 �씤�썝�쓣 �쟾�넚
			} //�봽濡쒓렇�옩 醫낅즺 if臾�
			
			/* 諛� �눜�옣 */
			else if(inputMessage.equalsIgnoreCase("6")) {
				myRoom.ccu.remove(this);	//myRoom�쓽 �젒�냽 �씤�썝�뿉�꽌 �겢�씪�씠�뼵�듃 �궘�젣
				myRoom.count--;				//myRoom�쓽 �씤�썝�닔 �븯�굹 �궘�젣
				wuser.add(this);			//��湲곗떎 �젒�냽 �씤�썝�뿉 �겢�씪�씠�뼵�듃 異붽�
				
				System.out.println("[Server] " +"諛� '" + myRoom.title + "' �눜�옣");
				
				if(myRoom.count==0) {	//myRoom�쓽 �씤�썝�닔媛� 0�씠硫� myRoom�쓣 room 諛곗뿴�뿉�꽌 �궘�젣
					room.remove(myRoom);
				}
				
				if(room.size() != 0) {	//�깮�꽦�맂 room�쓽 媛쒖닔媛� 0�씠 �븘�땲硫� 諛⑹뿉 �엯�옣�븳 �씤�썝�뿉 諛� �씤�썝 紐⑸줉�쓣 �쟾�넚
					sendRoom(roomUser());
					
				}
				
				sendWait(roomInfo());		//��湲곗떎 �젒�냽 �씤�썝�뿉 諛� 紐⑸줉�쓣 �쟾�넚
				sendWait(connectedUser());	//��湲곗떎 �젒�냽 �씤�썝�뿉 �쟾泥� �젒�냽 �씤�썝�쓣 �쟾�넚
			} //諛� �눜�옣 if臾�
			
			/* tictaetoc */
			else if(inputMessage.equalsIgnoreCase("7")) {
				for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom�쓽 �씤�썝�닔留뚰겮 諛섎났
					
					if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//諛� �젒�냽 �씤�썝 以� �겢�씪�씠�뼵�듃�� �떎瑜� �땳�꽕�엫�쓽 �겢�씪�씠�뼵�듃�뿉寃뚮쭔 �쟾�넚
						//myRoom.ccu.get(i).out.write("7" + "//" + m[1] + "//" + m[2] + "//" + m[3]);
					}
				}
			}  //�삤紐� if臾�
			
			/* �듅由� 諛� �쟾�쟻 �뾽�뜲�씠�듃 */
			else if(inputMessage.equalsIgnoreCase("8")) {
				System.out.println("[Server] " + nickname + " �듅由�");
				
				if(db.winRecord(nickname)) {	//�쟾�쟻 �뾽�뜲�씠�듃媛� �꽦怨듯븯硫� �뾽�뜲�씠�듃 �꽦怨듭쓣 �쟾�넚
					out.write("10" + "//OKAAAAY");
				} else {						//�쟾�쟻 �뾽�뜲�씠�듃媛� �떎�뙣�븯硫� �뾽�뜲�씠�듃 �떎�뙣瑜� �쟾�넚
					out.write("10" + "//FAIL");
				}
				
				for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom�쓽 �씤�썝�닔留뚰겮 諛섎났
					/* 諛� �젒�냽 �씤�썝 以� �겢�씪�씠�뼵�듃�� �떎瑜� �땳�꽕�엫�쓽 �겢�씪�씠�뼵�듃�씪�븣留� */
					if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
						myRoom.ccu.get(i).out.write("9" + "//");
						
						if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//�쟾�쟻 �뾽�뜲�씠�듃媛� �꽦怨듯븯硫� �뾽�뜲�씠�듃 �꽦怨듭쓣 �쟾�넚
							myRoom.ccu.get(i).out.write("10" + "//OKAY");
						} else {										//�쟾�쟻 �뾽�뜲�씠�듃媛� �떎�뙣�븯硫� �뾽�뜲�씠�듃 �떎�뙣瑜� �쟾�넚
							myRoom.ccu.get(i).out.write("10" + "//FAIL");
						}
					}
				}
			}  //�듅由� 諛� �쟾�쟻 �뾽�뜲�씠�듃 if臾�
			
			/* �뙣諛�, 湲곌텒 諛� �쟾�쟻 �뾽�뜲�씠�듃 */
			else if(inputMessage.equalsIgnoreCase("9")) {
				if(myRoom.count==1) {	//湲곌텒�쓣 �뻽�뒗�뜲 諛� �젒�냽 �씤�썝�씠 1紐낆씪 �븣 �쟾�쟻 誘몃컲�쁺�쓣 �쟾�넚
					out.write("10" + "//NO");
				}
				
				else if(myRoom.count==2) {	//湲곌텒 諛� �뙣諛곕�� �뻽�쓣 �븣 諛� �젒�냽 �씤�썝�씠 2紐낆씪 �븣
					out.write("10" + "//");
					
					if(db.loseRecord(nickname)) {	//�쟾�쟻 �뾽�뜲�씠�듃媛� �꽦怨듯븯硫� �뾽�뜲�씠�듃 �꽦怨듭쓣 �쟾�넚
						out.write("10" + "//OKAY");
					} else {										//�쟾�쟻 �뾽�뜲�씠�듃媛� �떎�뙣�븯硫� �뾽�뜲�씠�듃 �떎�뙣瑜� �쟾�넚
						out.write("10" + "//FAIL");
					}
					
					for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom�쓽 �씤�썝�닔留뚰겮 諛섎났
						
						/* 諛� �젒�냽 �씤�썝 以� �겢�씪�씠�뼵�듃�� �떎瑜� �땳�꽕�엫�쓽 �겢�씪�씠�뼵�듃�씪�븣留� */
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
							myRoom.ccu.get(i).out.write("8" + "//");
							
							if(db.winRecord(myRoom.ccu.get(i).nickname)) {	//�쟾�쟻 �뾽�뜲�씠�듃媛� �꽦怨듯븯硫� �뾽�뜲�씠�듃 �꽦怨듭쓣 �쟾�넚
								myRoom.ccu.get(i).out.write("10" + "//OKAY");
							} else {										//�쟾�쟻 �뾽�뜲�씠�듃媛� �떎�뙣�븯硫� �뾽�뜲�씠�듃 �떎�뙣瑜� �쟾�넚
								myRoom.ccu.get(i).out.write("10" + "//FAIL");
							}
						}
					}
			}  //�뙣諛�, 湲곌텒 諛� �쟾�쟻 �뾽�뜲�씠�듃 if臾�
		}  //while臾�
	}catch(

	IOException e)
	{
		System.out.println("[Server] �엯異쒕젰 �삤瑜� > " + e.toString());
	}
} // run()
	/* �쁽�옱 議댁옱�븯�뒗 諛⑹쓽 紐⑸줉�쓣 議고쉶�븯�뒗 硫붿냼�뱶 */

	String roomInfo() {
		String msg = "11" + "//";

		for (int i = 0; i < room.size(); i++) {
			msg = msg + room.get(i).title + " : " + room.get(i).count + "@";
		}
		return msg;
	}

	/* �겢�씪�씠�뼵�듃媛� �엯�옣�븳 諛⑹쓽 �씤�썝�쓣 議고쉶�븯�뒗 硫붿냼�뱶 */
	String roomUser() {
		String msg = "12" + "//";

		for (int i = 0; i < myRoom.ccu.size(); i++) {
			msg = msg + myRoom.ccu.get(i).nickname + "@";
		}
		return msg;
	}

	/* �젒�냽�븳 紐⑤뱺 �쉶�썝 紐⑸줉�쓣 議고쉶�븯�뒗 硫붿냼�뱶 */
	String connectedUser() {
		String msg = "13" + "//";

		for (int i = 0; i < auser.size(); i++) {
			msg = msg + auser.get(i).nickname + "@";
		}
		return msg;
	}

	/* ��湲곗떎�뿉 �엳�뒗 紐⑤뱺 �쉶�썝�뿉寃� 硫붿떆吏� �쟾�넚�븯�뒗 硫붿냼�뱶 */
	void sendWait(String m) {
		for (int i = 0; i < wuser.size(); i++) {
			try {
				wuser.get(i).out.write(m);
			} catch (IOException e) {
				wuser.remove(i--);
			}
		}
	}

	/* 諛⑹뿉 �엯�옣�븳 紐⑤뱺 �쉶�썝�뿉寃� 硫붿떆吏� �쟾�넚�븯�뒗 硫붿냼�뱶 */
	void sendRoom(String m) {
		for (int i = 0; i < myRoom.ccu.size(); i++) {
			try {
				myRoom.ccu.get(i).out.write(m);
			} catch (IOException e) {
				myRoom.ccu.remove(i--);
			}
		}
	}
	}
	// CCUser �겢�옒�뒪
