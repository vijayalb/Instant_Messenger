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

package client;

import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client implements Runnable{
	
	Socket clientSideSocket;
	Scanner getInput;
	PrintWriter getOutput;

	public Client(Socket socket){
		clientSideSocket = socket;
	}
	
	/*
	 * This module gets the input and output of the client socket. If the socket is terminated, finally statement will execute.
	 */
	@Override
	public void run() {
		try{
			try{
				getInput = new Scanner(clientSideSocket.getInputStream());
				getOutput = new PrintWriter(clientSideSocket.getOutputStream());
				getOutput.flush();
				displayAvailableUsers();
			}
			finally{
				clientSideSocket.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	/*
	 * Sending the message to Client GUI by using the object getOutput
	 */
	public void userMessageToGUI(String message){
		getOutput.println(ClientGUI.userName+" > "+message);
		getOutput.flush();
	}

	/*
	 * Socket connection will be closed when client clicks on log-off/disconnect button
	 */
	public void userLogedOff() throws IOException{
		getOutput.flush();
		clientSideSocket.close();
	}
	
	/*
	 * Displays the users available in the ClientGUI.
	 */
	@SuppressWarnings("unchecked")
	public void displayAvailableUsers(){
		while(true){
			if(getInput.hasNext()){
				String userMessage = getInput.nextLine();
				if(userMessage.contains("$")) {
					String[] available = userMessage.substring(1).replace("[", "").replace("]", "").split(", ");
					ClientGUI.usersAvailable.setListData(available);
				}
				else{
					ClientGUI.userContent.append(userMessage+"\n");
				}
			}
		}
	}		
}
