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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class ClientGUI{

	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel loginPanel;
	private JPanel chatPanel;
	private JLabel userNameLabel;
	private JLabel userAvailableLabel;
	private Socket userSocket;
	private JTextField clientName;
	private JTextField userMessage;
	private JButton userLogoff;
	private JCheckBox userAvailableCheckbox;
	private JButton userLogin;
	private Client userClient;
	private static JTextField userNameRegistration;
	private static JButton userConnect;
	private static JButton userSend;
	private static JLabel userGreeting;
	public static JTextArea userAvailability;
	public static JScrollPane userAvailabilityScroll = new JScrollPane();
	public static JTextArea userContent;
	public static JScrollPane userContentScroll = new JScrollPane();
	public static String userName;
	@SuppressWarnings("rawtypes")
	public static JList usersAvailable = new JList();
	DateFormat timeStamp = new SimpleDateFormat("HH:mm:ss");

	/*
	 * Main function to setup the window for Client GUI
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.mainFrame.setVisible(true);
					window.mainFrame.setTitle("Instant Messenger Client");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Connecting with Server socket using local host and port = 80. The counter variable helps in keeping list for client's online presence.
	 * If client doesn't check the online box, this counter will be 0 and client name will not be added to the list.
	 */
	protected void serverConnection() {
		try{
			final int port = 80; 
			userSocket = new Socket("localhost",port); 
			userClient = new Client(userSocket);
			PrintWriter out = new PrintWriter(userSocket.getOutputStream());
			String counter = "0";
			if(userAvailableCheckbox.isSelected())
				counter = "1";
			out.println(counter+userNameRegistration.getText());
			out.flush();
			Thread thread = new Thread(userClient);
			thread.start();
		}
		catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Server down.");
			System.exit(0);
		}
	}

	public ClientGUI() {
		initialize();
		listeners();
	}
	
	/*
	 * Initialize module for setting up Client's login and chat home screen. 
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setBounds(100, 100, 700, 700);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 600, 600);
		mainFrame.getContentPane().add(mainPanel);
		mainPanel.setLayout(new CardLayout());
		
		loginPanel = new JPanel();
		mainPanel.add(loginPanel, "loginpanel");
		loginPanel.setLayout(null);
		
		userNameLabel = new JLabel("Username:");
		userNameLabel.setBounds(180, 300, 80, 20);
		loginPanel.add(userNameLabel);
		
		userNameRegistration = new JTextField();
		userNameRegistration.setBounds(280, 300, 150, 20);
		loginPanel.add(userNameRegistration);
		userNameRegistration.setColumns(10);
		
		userAvailableCheckbox = new JCheckBox("Online");
		userAvailableCheckbox.setBounds(450, 300, 150, 20);
		loginPanel.add(userAvailableCheckbox);
		
		userLogin = new JButton("Login");
		userLogin.setBounds(280, 350, 150, 20);
		loginPanel.add(userLogin);
		
		chatPanel = new JPanel();
		mainPanel.setBounds(0, 0, 700, 700);
		mainPanel.add(chatPanel, "chatpanel");
		chatPanel.setLayout(null);
		
		userContent = new JTextArea();
		userContent.setColumns(20);
		userContent.setRows(5);
		userContent.setEditable(false);
		userContent.setLineWrap(true);
		
		userGreeting = new JLabel();
		userGreeting.setBounds(20, 10, 223, 39);
		chatPanel.add(userGreeting);
		
		userContentScroll.setViewportView(userContent);
		userContentScroll.setBounds(20, 50, 460, 460);
		chatPanel.add(userContentScroll);
		
		userAvailableLabel = new JLabel("Available Users");
		userAvailableLabel.setBounds(500, 10, 99, 40);
		chatPanel.add(userAvailableLabel);
		
		userAvailabilityScroll.setViewportView(usersAvailable);
		userAvailabilityScroll.setBounds(500, 50, 150, 460);
		chatPanel.add(userAvailabilityScroll);
		
		userMessage = new JTextField("Type your message");
		userMessage.setBounds(20, 550, 480, 20);
		chatPanel.add(userMessage);
		userMessage.setColumns(10);
		
		userSend = new JButton("Send");
		userSend.setBounds(520, 550, 110, 20);
		chatPanel.add(userSend);
		
		userLogoff = new JButton("Log off/Disconnect");
		userLogoff.setBounds(20, 600, 160, 20);
		chatPanel.add(userLogoff);
		
		clientName = new JTextField("Type the client's name");
		clientName.setBounds(200, 600, 300, 20);
		chatPanel.add(clientName);
		clientName.setColumns(10);
		
		userConnect = new JButton("Connect");
		userConnect.setBounds(520, 600, 110, 20);
		chatPanel.add(userConnect);
		
	}

	/*
	 * Whenever the user clicks on any button, using the below listeners the action performed method will be called.
	 */
	private void listeners() {
		userLogin.addActionListener(loginListener);
		userConnect.addActionListener(userConnectListener);
		userSend.addActionListener(userSendListener);
		userLogoff.addActionListener(userLogOffListener);
	}
	
	/*
	 * First login listener when the client registers into the messenger.
	 */
	private ActionListener loginListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!userNameRegistration.getText().equals("")){
				userName = userNameRegistration.getText();
				serverConnection();
				CardLayout layout = (CardLayout) mainPanel.getLayout();
				layout.show(mainPanel,"chatpanel");
				userGreeting.setText("Welcome, "+userName.toUpperCase());
				mainFrame.setTitle("Instant Messenger");
			}
			else{
				JOptionPane.showMessageDialog(null, "Username cannot be empty."); 
			}
		}
		
	};
	
	/*
	 * When the client wants to connect with another user, the user name will be acquired and connection will be established.  
	 */
	private ActionListener userConnectListener = new ActionListener(){
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String toConnectWith = clientName.getText();
			if(!toConnectWith.equals("")){
				PrintWriter printWriter;
				try {
					printWriter = new PrintWriter(userSocket.getOutputStream());
					printWriter.println("@ > "+ClientGUI.userName+" > "+toConnectWith);
					printWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				JOptionPane.showMessageDialog(mainFrame,"Invalid Name.");
			}
		}
	};
	
	/*
	 * Client message will be sent using "sent" button, where the message is acquired from userMessage box.
	 */
	private ActionListener userSendListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String message = userMessage.getText();
			if(!message.isEmpty()){
				ClientGUI.userContent.append("You: "+message+"\n");
				try {
					sendChatMessage(message);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			else{
				JOptionPane.showMessageDialog(mainFrame,"Message cannot be empty.");
			}
		}
		
	};
	
	/*
	 * When client clicks on the log off/disconnect button
	 */
	private ActionListener userLogOffListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				userClient.userLogedOff();
				System.exit(0);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	};

	/*
	 * Acquiring the user's chat message and appending with http Header.
	 */
	protected void sendChatMessage(String message) throws UnsupportedEncodingException {
		Date date = new Date();
		String httpHeader = "GET./.TimeStamp: "+date.toString()+"./.Content-Length:"+message.getBytes("UTF-8").length +"./.From: "+userName;
		userClient.userMessageToGUI(httpHeader+" > "+message);
		userMessage.setText("");
	}
}
