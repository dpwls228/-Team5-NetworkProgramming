import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class game_server {
	
	Vector<game_thread> alluser;		//연결된 모든 클라이언트
	Vector<game_thread> waituser;	//대기실에 있는 클라이언트
	Vector<game_room> room;			//생성된 Room
	
	public static void main(String[] args) {
		game_server server = new game_server();
		ServerSocket listener = null;
		
		server.alluser = new Vector<>();
		server.waituser = new Vector<>();
		server.room = new Vector<>();
		Scanner inputstream = null;

		int nPort = 0;
		try {
			inputstream = new Scanner(new File("server_info.dat"));
			String newInput = inputstream.nextLine();
			newInput = inputstream.nextLine();
			nPort = Integer.valueOf(newInput.substring(newInput.indexOf(" ") + 1));

		} catch (FileNotFoundException e) {
			nPort = 1024;
		}
		try {
			listener = new ServerSocket(nPort);

			while (true) {
				try {

					System.out.println("Waitting Clients...\n");
					Socket client = listener.accept();
					System.out.println("One client connected! \n");
					game_thread user = new game_thread(client, server);
					user.start();
				} catch (Exception e) {

				} finally {

				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
			} finally {
			}
		}
	}
}
