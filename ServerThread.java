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

import java.net.Socket;
import java.io.PrintWriter;
import java.util.Scanner;

public class ServerThread implements Runnable {
	Socket clientSocket;
	private Scanner userInput;
	private PrintWriter userScreen;
	private String clientMessage;
	
	public ServerThread(Socket socket) {
		this.clientSocket = socket;
	}

	/*
	 * If any two clients are connected, this module helps in separating the user name and connected user's name
	 */
	private Socket connectedSocket(String username){
		Socket connectedRSocket = null;
		if(Server.pair.containsKey(username)){
			String clientname = Server.pair.get(username);
			connectedRSocket = Server.socketsConnected.get(clientname);
		}
		return connectedRSocket;
	}
	
	/*
	 * The below module does 3 checks. Either the requested client is already connected, or the requested client is registered, or the
	 * request client is logged off/disconnected.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try{
			try{
				userInput = new Scanner(clientSocket.getInputStream());
				userScreen = new PrintWriter(clientSocket.getOutputStream());
				while(true){
					if(!userInput.hasNext())
						return;
					clientMessage = userInput.nextLine();
					if(clientMessage.substring(0, 1).contains("@")) {
						String currentUserName = clientMessage.split(" > ")[1];
						String clientName = clientMessage.split(" > ")[2];
						/*
						 * If the requested client name is in registered user name list, then server checks whether the client is
						 * already connected to any other user.
						 */
						if(Server.userList.contains(clientName)){
							/*
							 * If the requested client name is already in pair list, then GUI says Client Busy.
							 * No second client will be allowed in a chat pair.
							 */
							if(Server.pair.containsKey(clientName)) {
								userScreen.println("Client busy.");
								userScreen.flush();
							}
							/*
							 * If the requested client name is not in pair, then two users will be connected and added to list 'pair'
							 */
							else{
								Server.pair.put(currentUserName, clientName);
								Server.pair.put(clientName, currentUserName);
								Server.connectedList.add(currentUserName+" < > "+clientName);
								userScreen.flush();
								userScreen.println("Connected with:"+clientName);
								userScreen.flush();
								PrintWriter clientUserScreen = new PrintWriter(connectedSocket(currentUserName).getOutputStream());
								clientUserScreen.flush();
								clientUserScreen.println("Connected with:"+currentUserName);
								clientUserScreen.flush();
							}
						}
						/*
						 * If the requested client name is not in registered user name list, then GUI displays client not available.
						 */
						else{
							userScreen.println("Client not available.");
							userScreen.flush();
						}
					}
					/*
					 * Displaying the client's messages in the server GUI by stripping off the header message
					 */
					else {
						String[] stripMessage = clientMessage.split(" > ");
						String senderName = stripMessage[0];
						Socket recepientSocket = connectedSocket(senderName);
						/*
						 * If there is any connection going on, only then the server will display the messages in GUI
						 */
						if(recepientSocket!=null){
							try{
								PrintWriter Out = new PrintWriter(recepientSocket.getOutputStream());
								Out.println(senderName+": "+stripMessage[2]);
								Out.flush();
								String stripHeader[] = stripMessage[1].split("./.");
								for(String header:stripHeader)
									Server.server.serverLog.append(header+"\n");
								Server.server.serverLog.append("Content: "+stripMessage[2]+"\n\n");
							}catch(Exception e){
								userScreen.println("Client Disconnected/LoggedOff");
								userScreen.flush();
							}
						}
						/*
						 * If there is not connection established between pairs, or the user logs off, then the message will not
						 * displayed in server GUI, and the user will be notified saying Client is disconnected.
						 */
						else{
							userScreen.println("Client Disconnected/LoggedOff");
							userScreen.flush();
						}
					}
				}
			}
			finally{
				clientSocket.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
