package com.fivechess.model;

import java.io.Serializable;

public class Message implements Serializable{
	int type;//0棋子坐标1准备2悔棋3同意悔棋4拒绝5悔棋成功6准备成功7认输8同意认输9拒绝认输
	int x;
	int y;
	public Message(int type) {
		super();
		this.type=type;
		// TODO Auto-generated constructor stub
	}
	public Message(int type, int x, int y) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Message message = (Message) obj;
		if(message.x == this.x && message.y == this.y && message.type == this.type)
			return true;
		else return false;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
