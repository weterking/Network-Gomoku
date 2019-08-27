package com.fivechess.net;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.fivechess.model.Chess;
import com.fivechess.model.Gamer;
import com.fivechess.model.Message;
import com.fivechess.ui.chessMainboard;

public class Read implements Runnable{
	ObjectInputStream in = null;
	Message message =null;
	//Message oldmessage = new Message(0, -1, -1);
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	
	public Message getMessage() {
		return message;
	}

	public void run()
	{
		while(true)
			try {
				if(in != null)
					message = (Message) in.readObject();	
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
	}
}
