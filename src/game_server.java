import java.io.*;
import java.net.*;
import java.util.Scanner;

public class game_server {

	public static void main(String[] args) {
		ServerSocket listener = null;

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
					game_thread user = new game_thread(client);
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