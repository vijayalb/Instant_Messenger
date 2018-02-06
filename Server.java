/*
 * Name: Vijayalakshmi Balakrishnan
 * ID: 1001434626
 * Reference List:
 * http://www.discoversdk.com/blog/java-socket-programming
 * http://www.di.ase.md/~aursu/ClientServerThreads.html
 * http://stackoverflow.com/questions/7295569/java-chat-server-see-online-accounts
 * http://stackoverflow.com/questions/5466091/split-from-headers
 * http://eezytolearn.blogspot.com/
 * https://www.youtube.com/watch?v=kqBmsLvWU14&t=428s
 */

package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {

	public static ServerGUI server;
	private static String userName;
	static ServerSocket Server;
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
	public static ArrayList<String> userList = new ArrayList<String>();
	public static ArrayList<String> availableUser = new ArrayList<String>();
	public static ArrayList<String> connectedList = new ArrayList<String>();
	public static Map<String,Socket> socketsConnected = new HashMap<String,Socket>();
	public static Map<String,String> pair = new HashMap<String,String>();
	
	/*
	 * Main function to start the server using the socket with port = 80.
	 * It also prints the client's connection in output console of IDE.
	 */
	@SuppressWarnings({ "static-access" })
	public static void main(String args[]) throws IOException{
		try{
			try {
				final int port = 80;
				Server = new ServerSocket(port);
				System.out.println("Server Started.");
				server = new ServerGUI();
				server.server();
				while(true) { 
					Socket socket = Server.accept();
					socketList.add(socket);
					System.out.println("New Connection :"+socket.getLocalAddress().getHostName());
					socketAdd(socket);
					ServerThread start = new ServerThread(socket);
					Thread thread = new Thread(start);
					thread.start();
				}
			}
			finally{
				Server.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Whenever any client is connected to the server, user availability is checked and added to three lists available-user,
	 * user-list and sockets-connected.  
	 */
	public static void socketAdd(Socket socket) throws IOException {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(socket.getInputStream());
		userName = input.nextLine();
		if(userName.charAt(0)=='1')
			availableUser.add(userName.substring(1));
		userList.add(userName.substring(1));
		socketsConnected.put(userName.substring(1), socket);
		for(int i=0; i<userList.size();i++){
			Socket userSocket = (Socket) socketList.get(i);
			PrintWriter writer = new PrintWriter(userSocket.getOutputStream());
			writer.println("$"+availableUser);
			writer.flush();
		}
	}
}
