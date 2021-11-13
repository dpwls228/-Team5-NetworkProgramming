import java.io.*;
import java.util.*;
import java.net.*;

import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
	
public class game_thread extends Thread {
	// making socket
	BufferedReader in = null;
	BufferedWriter out = null;

	Socket socket = null;

	Scanner inputstream = null;
//making thread server
	public game_thread(Socket sock) {
		socket = sock;
	}

//server playing
	@Override
	public void run() {
		try 
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//client send Game
			String inputMessage;
			inputMessage = in.readLine();
//Start write
			if (inputMessage.equals("Game"))
			{
				System.out.println("Client added " + inputMessage);
				out.write("Start\n");
				out.flush();
			}
			else 
			{
				System.out.println("WRONG Client added");
				socket.close();
			}
//file connect
			String PathPointer = "./Server";
			File ServerPointer = null;
			Path path = Paths.get(PathPointer);
//file isn't exist  -> make directory
			if (!Files.exists(path) && !Files.isDirectory(path)) 
			{
				ServerPointer = new File(PathPointer);
				ServerPointer.mkdir();
			}
			PathPointer = "./Server/Users";
			path = Paths.get(PathPointer);

			if (!Files.exists(path) && !Files.isDirectory(path)) 
			{
				ServerPointer = new File(PathPointer);
				ServerPointer.mkdir();
			}

			while (true) 
			{

				System.out.println("First loop");
				inputMessage = in.readLine();
//menu input
				String outputMessage = null;
				outputMessage = String.valueOf(inputMessage);
				String token[] = inputMessage.split(" ");
				System.out.println(inputMessage + " Connected!");

				out.write(inputMessage + "\n");
				out.flush();
//play menu
//0: game leave-> leaved client is lose
				if (inputMessage.equalsIgnoreCase("0")) 
				{
					break;
				} 
//1: game start
				else if (inputMessage.equals("1")) 
				{
					inputMessage = in.readLine();//winner client name
					path = Paths.get(PathPointer + "/" + inputMessage); //path set
					System.out.println(path + " " + Files.exists(path) + inputMessage);
				
					if (Files.exists(path)) 
					{
						out.write("CRID" + "\n"); //success
						out.flush();
						System.out.println("ID is correct as " + inputMessage);
//file directory
						File RID1 = new File(PathPointer + "/" + inputMessage);
						RID1.mkdir();
						try 
						{
							RID1 = new File(PathPointer + "/" + inputMessage + "Result/data.txt"); //make text file named ID
							RID1.createNewFile();

							PrintWriter RWriter = new PrintWriter(new FileWriter(RID1));
//current time print
							LocalDateTime now = LocalDateTime.now();
							String time = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
							inputMessage = in.readLine(); //result information
							RWriter.println(inputMessage +"----" +time); //write in file
							System.out.println(inputMessage + " Printed!");
							
							out.write("Game End"+ "\n");
							out.flush(); 
							
							RWriter.close();
						} catch (FileNotFoundException e) {

						} catch (IOException e) {

						} finally {

						}
						
						inputMessage = in.readLine();//lose client name
						path = Paths.get(PathPointer + "/" + inputMessage); //path set
						System.out.println(path + " " + Files.exists(path) + inputMessage);
					
						if (Files.exists(path)) 
						{
							out.write("CRID" + "\n"); //success
							out.flush();
							System.out.println("ID is correct as " + inputMessage);
	//file directory
							File RID2 = new File(PathPointer + "/" + inputMessage);
							RID2.mkdir();
							try 
							{
								RID2 = new File(PathPointer + "/" + inputMessage + "Result/data.txt");
								RID2.createNewFile();

								PrintWriter RWriter = new PrintWriter(new FileWriter(RID2));
	//current time print
								LocalDateTime now = LocalDateTime.now();
								String time = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
								inputMessage = in.readLine(); //result information
								RWriter.println(inputMessage +"----" +time); //write in file
								System.out.println(inputMessage + " Printed!");
								
								out.write("Game End"+ "\n");
								out.flush(); 
								
								RWriter.close();
							} catch (FileNotFoundException e) {

							} catch (IOException e) {

							} finally {

							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Client quited");
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
