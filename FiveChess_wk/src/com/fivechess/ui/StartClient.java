package com.fivechess.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fivechess.model.Chess;
import com.fivechess.model.Gamer;
import com.fivechess.model.Message;
import com.fivechess.net.Read;



public class StartClient extends chessstart{
	private static Read read;
	chessMainboard gamer1board = null;
	Message  message = new Message(0);
	Gamer gamer1 = new Gamer();
	Socket mySoket = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	Thread readMessage = null;
	public static void main(String[] args) {
		new StartClient();
	}
	public StartClient() {
		setTitle("Client");
		// TODO Auto-generated constructor stub
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent enent) {
					dispose();
					gamer1.setColor(Chess.WHITE);
					gamer1.setName("game1");
					try {
						mySoket = new Socket(tf1.getText(),Integer.parseInt(tf2.getText()));
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//readMessage = new Thread(read);
					if(mySoket.isConnected())
					{
							try {
								out = new ObjectOutputStream(mySoket.getOutputStream());
								in = new ObjectInputStream(mySoket.getInputStream());
								if(in == null)
									System.out.println("有问题");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							gamer1board = new chessMainboard(gamer1, in, out);
							gamer1board.setTitle("五子棋gameclient_wk");
							gamer1board.chessboard.setClickable(false);
					}
				
			}
		});
	}
	
}
