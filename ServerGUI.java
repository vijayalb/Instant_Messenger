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

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.EventQueue;

public class ServerGUI {

	private JFrame mainFrame;
	private JScrollPane serverLogScroll;
	public static JTextArea serverLog = new JTextArea();
	
	public ServerGUI() {
		initialize();
	}
	
	/*
	 * Initializing the Server GUI with main frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("Instant Messenger Server");
		mainFrame.setBounds(100, 100, 500, 500);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		serverLogScroll = new JScrollPane();
		serverLogScroll.setBounds(10, 10, 460, 430);
		serverLogScroll.setViewportView(serverLog);
		mainFrame.getContentPane().add(serverLogScroll);
	}
	
	/*
	 * This module is called by server.java class to fire up the server GUI.
	 */
	public static void server(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI mainScreen = new ServerGUI();
					mainScreen.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
}
