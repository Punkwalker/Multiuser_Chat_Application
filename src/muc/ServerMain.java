package muc;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JTextArea;

/**
 * @author Pankaj Walke
 * Name: Pankaj Walke
 *UTA ID: 1001781008
 *Lab 1 - Distributed Systems CSE5306-001 Sring2020 
 *
 */
public class ServerMain implements Runnable{

	private static ServerSocket serverSocket;
	private static String userName;
	static Scanner inputFromClient = null; //Input from Clients
	static PrintWriter outputForClient = null; //Output to Client Display
	static Vector<ConnectedClient> activeClient = new Vector<>(); //Collection of Active Sockets 
	static ArrayList<String> clientList = new ArrayList<String>();// List of Usernames
	private Thread serverThread;
	private volatile boolean stopThread = false;
	
	public ServerMain(JTextArea serverOutput) {
		// TODO Auto-generated constructor stub
		if(serverOutput!=null) {
			//code block logic from https://github.com/iSpammer/MultiUser-Chat-Application
			PrintStream printStream = new PrintStream(new CustomOutput(serverOutput));
	        System.setOut(printStream);
	        System.setErr(printStream);
		}
		
	}
	//Main method
	public static void main(String[] args) {
		new ServerMain(null).startServer(); //Start Server instance for GUI button
//		new ServerMain().endServer();			//End Server instance for GUI button
	}
	
	/*
	 * Start Server Method
	 */
	public void startServer() {
		try {
			serverSocket = new ServerSocket(9999, 20); //Start Server Socket
			
			serverThread = new Thread(this);
			serverThread.start();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return;
	}
	
	//Method to End Server
	public void endServer() {
		System.out.println("Closing all connections...");
		for (ConnectedClient connectedClient : activeClient) {
			connectedClient.closeConnection();
		}
		
		try {
			stopThread = true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!stopThread) // Infinite loop to make server listen for Connecting Clients
		{
			System.out.println("Waiting for Client to connect...."); //All System.Out are for Server Display
			try {
				Socket acceptSocket = serverSocket.accept(); // Server accepts connected client
				outputForClient = new PrintWriter(acceptSocket.getOutputStream(),true);
				inputFromClient = new Scanner(acceptSocket.getInputStream()); //First line from Client to be used as Username
				outputForClient.println("Please enter username -");
				while (inputFromClient.hasNextLine()) {
					userName = inputFromClient.nextLine();
					int flag = 0;
					//Loop to check Existing userName and Insert userName in list
					for (String string : clientList) { 
						if (string.equals(userName)) {
							flag = 1;
							outputForClient.println("Username already in use.." + "Please enter different Username!! -");
							break;
						}
					}
					//Function to Insert userName in list and assign new Thread to user
					if (flag == 0) {
						clientList.add(userName);
						ConnectedClient client = new ConnectedClient(acceptSocket, userName, inputFromClient,outputForClient);
						Thread thread = new Thread(client);
						activeClient.add(client);
						thread.start();
						break;
					}
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//When server is called to stop, stop all the services and serversocket
		inputFromClient.close();
		outputForClient.close();
		try {
			serverSocket.close();
			serverSocket = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

//Thread extension for new user
class ConnectedClient implements Runnable {

	private Socket socket = null;
	public String name;
	Scanner input = null;
	PrintWriter output = null;
	boolean isOnline;

	public ConnectedClient(Socket socket, String name, Scanner inputFromClient, PrintWriter outputForClient) {
		System.out.println("Initializing the client...");
		this.socket = socket;
		this.name = name;
		this.input = inputFromClient;
		this.output = outputForClient;
		this.isOnline = true;
	}
	
	//method to close socket
	public void closeConnection() {
		output.println("Closing server.. Terminating all connections...");
		System.out.println("Closing socket connection to "+this.name);
		
		try {
			output.close();
			input.close();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Run method for new user thread
	@Override
	public void run() {
		System.out.println(name + " is Connected");
		StringBuilder sb = new StringBuilder();

		output.println("*** You are now connected ***");
		output.println("*** Your username is:"+name+" ***");
		//Loop to show online users and to inform that new user is online
		for (ConnectedClient client : ServerMain.activeClient) {
			if(!name.equals(client.name))
				client.output.println("*** "+name + " is online! ***");
			sb.append(client.name +"\n");
		}
		//Instructions about sending message
		sb.append("# To send message to individual, type '<username>:<message>'\n");
		sb.append("# To Broadcast, type 'all:<message>'\n");
		sb.append("# To Multicast, type '<username>,<username>:<message>'\n");
		output.println("*** Below are online members:\n"+sb.toString());
		
		//Infinite loop for continuous messaging
		while (true) {
			String message = input.nextLine();
			System.out.println(message);
			String[] token = message.split(":"); //code to separate message from recipient
			String[] list = token[0].split(",");
			
			//Function to Multicast
			try {
				if (list.length>1)
				{
					for (ConnectedClient client : ServerMain.activeClient) {
						for (String members : list ) {
							if(members.equals(client.name)) {
								if(!client.name.equals(name))
								{
								client.output.println(this.name +":"+token[1]);
								output.println("Multicast to "+ token[0]+" by "+this.name+":"+token[1]);
								}
								}
						}						
					}
					break;
				}
				//Function to end the chat from user end
				if (message.equalsIgnoreCase("Logoff")) {
					this.isOnline = false;
					output.println("*** You are now disconnected from Server ***");
					this.socket.close();
					System.out.println(this.name + " has left the chat");
					
					//Notification to all Users of Logged Out User
					for (ConnectedClient client : ServerMain.activeClient) {
						
						if(!client.name.equals(name))
						{
							client.output.println("*** "+this.name + " has left the chat ***");
						}
					}
					ServerMain.activeClient.remove(this);
					ServerMain.clientList.remove(name);
					break;
				}
				//Function to Broadcast
				if (token[0].equalsIgnoreCase("all")) {
					for (ConnectedClient client : ServerMain.activeClient) {
						
						if(!client.name.equals(name))
						{
							client.output.println(this.name + ":" + token[1]);
					}
					}
					output.println("Broadcast to all by " +this.name+":" + token[1]);
				} else {
					//Function for Direct Messaging
					for (ConnectedClient user : ServerMain.activeClient) {
						if (user.name.equals(token[0]) && !this.name.equals(token[0])) {
							user.output.println(this.name + ":" + token[1]);
							output.println(this.name + ":" + token[1]);							
							break;
						}

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}


