package muc;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

/**
 * @author Pankaj Walke
 * Name: Pankaj Walke
 *UTA ID: 1001781008
 *Lab 1 - Distributed Systems CSE5306-001 Sring2020 
 */
public class ClientGUI {

	private JFrame frame;
	private JTextField txt_chat;
	private ClientMain client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
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
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 562, 543);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btn_endChat = new JButton("End Chat");
		btn_endChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				client.send("logoff");
			}
		});

		btn_endChat.setBounds(27, 225, 109, 82);
		frame.getContentPane().add(btn_endChat);
		
		JButton btnNewButton_1 = new JButton("Start Chat");
		
		btnNewButton_1.setBounds(27, 133, 109, 82);
		frame.getContentPane().add(btnNewButton_1);
		
		txt_chat = new JTextField();
		txt_chat.setBounds(178, 381, 273, 115);
		frame.getContentPane().add(txt_chat);
		txt_chat.setColumns(10);
		
		
		
		JButton btn_send = new JButton("Send");
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.send(txt_chat.getText());
				txt_chat.setText("");
			}
		});
		btn_send.setBounds(453, 380, 85, 21);
		frame.getContentPane().add(btn_send);
		
		JTextArea outputClient = new JTextArea();
		outputClient.setBounds(178, 10, 360, 365);
		frame.getContentPane().add(outputClient);
		
		//Start client on button press
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				client = new ClientMain(outputClient);
				client.startChat();
			}
		});
	}
}
