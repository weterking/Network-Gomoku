package com.fivechess.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.fivechess.model.Chess;
import com.fivechess.model.Gamer;



public class StartServer extends chessstart{

	static Socket gamersocket = null;//ServerThread thread1;

	
	public static void main(String[] args) {
		new StartServer();
		Gamer gamer2 = new Gamer();
		gamer2.setColor(Chess.BLACK);
		gamer2.setName("game1");
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		//new chessMainboard(gamer1);
		chessMainboard game2board;
		ServerSocket server1 = null;
		//ServerThread thread1;

		Socket gamersocket = null;
		try {
			server1 = new ServerSocket(5000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			gamersocket = server1.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in = new ObjectInputStream(gamersocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out = new ObjectOutputStream(gamersocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game2board = new chessMainboard(gamer2,in,out);
		game2board.setTitle("五子棋gameserver_wk");
	}
	public StartServer() {
		setTitle("Server");
		// TODO Auto-generated constructor stub
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent enent) {
					dispose();					
			}
		});
	}
}
