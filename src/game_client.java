
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class game_client {
	static int count = 1;
//client main
	public static void main(String[] args) {
//making socket
		BufferedReader in = null;
		BufferedReader stin = null;
		BufferedWriter out = null;

		Socket socket = null;
		Scanner inputstream = null;

//connect server ip, port
		String serverIP = null;
		int nPort = 0;

		try 
		{
			inputstream = new Scanner(new File("server_info.dat"));
			String newInput = inputstream.nextLine();
			serverIP = newInput.substring(newInput.indexOf(" ") + 1);
			newInput = inputstream.nextLine();
			nPort = Integer.valueOf(newInput.substring(newInput.indexOf(" ") + 1));

		} 
		catch (FileNotFoundException e) 
		{
			serverIP = "localhost";
			nPort = 1024;
		}
//success connect server
		try {
			socket = new Socket(serverIP, nPort);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String inputMessage = null;

			String outputMessage;
//game write
			out.write("Game\n");
			out.flush();
			inputMessage = in.readLine();
// server send start
			if (inputMessage.equals("Start")) 
			{
				System.out.println("Game Room! 0): Leave\n1)Start");

			} 
			else 
			{
				System.out.println("ERROR: The server is Wrong. ");
				socket.close();
			}
//repeat 
			while (true) 
			{
// menu input
				System.out.println("First loop");
				outputMessage = stin.readLine();
				System.out.println(outputMessage + " Sended");
				out.write(outputMessage + "\n");
				out.flush();
//server wait
				System.out.println(" waitting for response...");
				inputMessage = in.readLine();
				System.out.println(inputMessage + " responsed by server");
// play menu
//0: game leave
				if (inputMessage.equals("0")) 
				{
					break;
				} 
//1: game start
				else if (inputMessage.equals("1")) 
				{
//game class produce -----> 어떻게 랜덤하게 invited 할 것인가	---------------------------
//send winner X		
					game_user gameUser = new game_user("gompang");
					game_user gameUser2 = new game_user("apple");

			        // #2. 특정 유저가 방을 만드려고 함
			        game_room gameRoom = game_manager.createRoom(gameUser);

			        // #3. 그 방에 나머지 유저가 들어가려고 함
			        gameRoom.enterUser(gameUser2);
					try {
				        // -- 로직 진행~ 게임, 채팅 등 --
				        game_function clientX = new game_function();
						clientX.turn = "x"; //game order
						outputMessage = clientX.game(); //game start
	
				        // #4. 유저가 방에서 나감
				        gameRoom.exitUser(gameUser2);
	
				        // #5. 방장이 방에서 나감 -> gameUser2이 방장이 됨
				        gameRoom.exitUser(gameUser);
	
				        // #6. 아래 결과는 null 이겠지
				        game_room room = game_manager.getRoom(gameRoom);
	
				        if(room != null){
				            System.out.println("room open");
				        }
				        else{
				            System.out.println("room close");
				        }
	//승패 저장 
						if (outputMessage.equalsIgnoreCase("X"))
						{
							/*outputMessage = "clientname";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");
							
							outputMessage = "clientname1:win/clientname2:lose";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");
							
							outputMessage = "clientname2";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");
							
							outputMessage = "clientname2:lose/clientname1:win";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");*/
						}
	//send winner X
						else if (outputMessage.equalsIgnoreCase("O"))
						{
							/*outputMessage = "clientname2";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");
							
							outputMessage = "clientname2:win/clientname1:lose";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");
							
							outputMessage = "clientname1";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");
							
							outputMessage = "clientname1:lose/clientname2:win";
							out.write(outputMessage + "\n"); // CODE and Messages
							out.flush();
	//server 확인
							inputMessage = in.readLine(); // wait server to ready instruction
							System.out.println(inputMessage + " received to server.");*/
						}
	//게임 종료시 타이머 온
						Timer timer=new Timer();
						TimerTask task=new TimerTask(){
						    @Override
						    public void run() {
						    //TODO Auto-generated method stub
	//게임 결과 저장중 
								if(count < 10){ //count값이 5보다 작거나 같을때까지 수행
									System.out.println("Store score");
									count++; //실행횟수 증가 
								}
	//게임 종료
								else{
									timer.cancel(); //타이머 종료
									System.out.println("Game end");
								}
						    }	
						};
						timer.schedule(task, 1000, 1000); //실행 Task, 1초뒤 실행, 1초마다 반복
	//-----------------exit game!!!!!!!!!!!!!!! go to waiting room
					} catch (Exception e) {
						 // 서버에서 무언가의 이유로 gameRoom을 삭제함(모든 유저 퇴장처리)
				        game_manager.removeRoom(gameRoom);
				    	System.out.println(
								"Error: Currently server hasn't opened / or having connection problem. check your IP address. ");
					}
				} 
			}
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				System.out.println(
						"Error: Currently server hasn't opened / or having connection problem. check your IP address. ");
			}
		}
	}
}
