package muc;

import java.awt.EventQueue;
import muc.ServerMain;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Pankaj Walke
 * Name: Pankaj Walke
 *UTA ID: 1001781008
 *Lab 1 - Distributed Systems CSE5306-001 Sring2020 
 */

public class ServerGUI {

	private JFrame frame;
	
	ServerMain serverMain = null;
	boolean serverStarted = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 577, 555);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JTextArea txt_serverOutput = new JTextArea();
		txt_serverOutput.setBounds(176, 10, 359, 485);
		frame.getContentPane().add(txt_serverOutput);
		
		JButton btn_startServer = new JButton("Start Server");
		btn_startServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				//ServerMain call
				serverMain = new ServerMain(txt_serverOutput);
				serverMain.startServer();
				serverStarted = true;
			}
		});
		btn_startServer.setBounds(31, 121, 109, 61);
		frame.getContentPane().add(btn_startServer);
		
		JButton btn_endServer = new JButton("Stop Server");
		btn_endServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(serverStarted) {
					serverMain.endServer();
					serverStarted = false;
				}
				
			}
		});
		btn_endServer.setBounds(31, 221, 109, 61);
		frame.getContentPane().add(btn_endServer);
	}
}
