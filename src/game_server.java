import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class game_server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        Socket socket = null;
        while (true) {
            //listening for client 1
            System.out.println("waiting client 1...........");
            socket = serverSocket.accept();
            game_client c1 = new game_client(socket);
            System.out.println("client 1 joined..........");

            //listening for client2
            System.out.println("waiting client 2...........");
            socket = serverSocket.accept();
            game_client c2 = new game_client(socket);
            System.out.println("client 2 joined..........");

            //starting game with the clients
            game_function run = new game_function();
            run.game();
        }
    }
}