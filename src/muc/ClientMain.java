package muc;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextArea;

/**
 * @author Pankaj Walke
 * Name: Pankaj Walke
 *UTA ID: 1001781008
 *Lab 1 - Distributed Systems CSE5306-001 Sring2020 
 */

public class ClientMain {
	
	
	private JTextArea clientOutput = null;
	Socket clientSocket = null;
	private Scanner input = null;
	private PrintWriter output = null;
	
	//Thread Class for handling input and ouput from Client	
	public class ClientActionThread implements Runnable{
		private Scanner input;
		private PrintWriter output;

		
		
		
		public ClientActionThread(Scanner input, PrintWriter output) {
			this.input = input;
			this.output = output;
		}
		
		//Run Method for ClientActionThread
		@Override
		public void run() {
			while(true)
			{
				if(input.hasNextLine()) 
				{	
				
					if(output == null) {
						System.out.println(input.nextLine());
					}
					else {
						output.println(input.nextLine());
					}
			}
			
		}
		
	}
}
	
	public ClientMain(JTextArea clientOutput) {
		// TODO Auto-generated constructor stub
		if(clientOutput != null) {
			this.clientOutput = clientOutput;
			PrintStream printStream = new PrintStream(new CustomOutput(clientOutput));
	        System.setOut(printStream);
	        System.setErr(printStream);
		}

	}
	//Main method
	public static void main(String[] args) {
		new ClientMain(null).startChat();
	}
	
	//Method to Start Chat
public void startChat() {
	try 
	{
			System.out.println("Connecting to server..");
			clientSocket = new Socket("localhost",9999); 
			
			input = new Scanner(clientSocket.getInputStream());
			output = new PrintWriter(clientSocket.getOutputStream(),true);
			Scanner sc = null;
			if(this.clientOutput == null) {
				sc = new Scanner(System.in);
				ClientActionThread dispatcher = new ClientMain(this.clientOutput).new ClientActionThread(sc, output);
				new Thread(dispatcher).start();
			}
				ClientActionThread listener = new ClientMain(this.clientOutput).new ClientActionThread(input, null);
				//ClientActionThread dispatcher = new ClientMain(this.clientOutput).new ClientActionThread(sc, output);
				new Thread(listener).start();
//				new Thread(dispatcher).start();		
	}catch (Exception e) {
		e.printStackTrace();
	}
}

public void send(String msg) {
	
	output.println(msg);
	
}
}
	

