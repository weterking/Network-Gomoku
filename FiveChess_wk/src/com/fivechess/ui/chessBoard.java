package com.fivechess.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fivechess.model.Chess;
import com.fivechess.model.Gamer;
import com.fivechess.model.Message;
import com.fivechess.model.arrChessboard;
import com.fivechess.net.Read;

public class chessBoard extends JPanel implements MouseListener,MouseMotionListener{

	/**
	 * Create the panel.
	 */
	public static final int raw = 15;//行x
	public static final int col = 15;//列y
	private static final int boardw = 25; 
	private static final int chessw = 32;
	private static final int chessr = 12;
	private static final int posir = 12;
	public ObjectInputStream  in = null;
	public ObjectOutputStream out =null;
	Message oldmessage = null;
	arrChessboard arrchess;
	Read read;
	Thread readmessage;
	boolean clickable = false;
	int mousex;
	int mousey;
	int result = -1;//结果-1未开始1胜利0失败2接近输3己方下棋中4已准备5对方下棋中
	Stack<Message> back;
	Image blackchess;
	Image whitechess;
	Image chessboard;
	Image position;
	Gamer gamer;
	public chessBoard(Gamer g, ObjectInputStream in, ObjectOutputStream out) {
		oldmessage = new Message(0, -1, -1);
		read = new Read();
		readmessage = new Thread(read);
		read.setIn(in);

		back = new Stack<Message>();
		setVisible(true);
		this.in= in;
		this.out = out;
		setBounds(0, 0, 500, 500);
		position = Toolkit.getDefaultToolkit().getImage("images/position.gif");
		blackchess = Toolkit.getDefaultToolkit().getImage("images/black.png");
		whitechess = Toolkit.getDefaultToolkit().getImage("images/white.png");
		chessboard = Toolkit.getDefaultToolkit().getImage("images/chessBoard.jpg");
		/*if(chessboard == null)
			System.out.println("图片加载有问题");
		else System.out.println("加载无问题");*/
		arrchess = new arrChessboard(raw, col);
		gamer = g;
		
		arrchess.initchess();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawImage(chessboard, 0, 0, null);
		if(result == 4)
		{
			try {
				out.writeObject(new Message(1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!readmessage.isAlive())
		{
			readmessage.start();
		}
		else if ((read.getMessage() != null &&( result != 0 || result != 1) && !read.getMessage().equals(oldmessage)) || (read.getMessage() != null && read.getMessage().getType() == 1) )
		{
			//消息响应模块，对各种消息进行响应 0棋子坐标1准备2悔棋3同意悔棋4拒绝5悔棋成功6准备成功7认输8同意认输9拒绝认输
			Message t =read.getMessage();
			oldmessage = t;
			if(read.getMessage().getType() == 0 && arrchess.chess[t.getX()][t.getY()] == Chess.BLANK)
			{
				arrchess.chess[t.getX()][t.getY()] = Chess.changecolor(gamer.getColor());
				back.push(read.getMessage());
				if(arrchess.judgenumber(t.getX(), t.getY(), 5, 0, Chess.changecolor(gamer.getColor())))
				{
					result = arrchess.getresult();
					if(result == 0)
					{
						JOptionPane.showMessageDialog(this, "您输了","加油", JOptionPane.PLAIN_MESSAGE);
					}
				}
				else 
				{
					clickable = true;
					result = 3;
				}
				
			}
			else if(read.getMessage().getType() == 1)
			{
				if(result == 4)//已经准备
				{
					if(gamer.getColor() == Chess.BLACK)
					{
						clickable = true;
						result = 3;
					}
					else 
					{
						setClickable(false);
						result = 5;
					}		
				}
			}
			else if(read.getMessage().getType() == 2)
			{
				int t1 = JOptionPane.showConfirmDialog(this, "对方请求悔棋","请求", JOptionPane.YES_NO_OPTION);
				
				if(t1 == 0)
				{
					try {
						out.writeObject(new Message(3));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message tMessage  = back.pop();
					arrchess.chess[tMessage.getX()][tMessage.getY()] =Chess.BLANK;
					result = 5;
					clickable = false;
				}
				else{
					try {
						out.writeObject(new Message(4));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(t.getType() == 3)
			{
				Message tMessage  = back.pop();
				arrchess.chess[tMessage.getX()][tMessage.getY()] =Chess.BLANK;
				result = 3;
				clickable = true;
			}
			else if(t.getType() == 4)
			{
				JOptionPane.showMessageDialog(this, "对方拒绝了您的悔棋请求");
			}
			else if (t.getType() == 5){
				//JOptionPane.showMessageDialog(null, "对面传个5");
			}
			else if (t.getType() == 7) {
				int t1 = JOptionPane.showConfirmDialog(this, "对方请求认输","请求", JOptionPane.YES_NO_OPTION);	
				if(t1 == 0)
				{
					try {
						out.writeObject(new Message(8));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(this, "您赢了","恭喜", JOptionPane.PLAIN_MESSAGE);
					result = 1;
					clickable = false;
				}
				else {
					try {
						out.writeObject(new Message(9));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(t.getType() == 8)
			{
				JOptionPane.showMessageDialog(this, "您输了","加油", JOptionPane.PLAIN_MESSAGE);
				result = 0;
				clickable = false;
			}
			else if(t.getType() == 9)
			{
				JOptionPane.showMessageDialog(this, "对方拒绝了您的认输情求");
			}
		}
		for(int i = 0; i < raw; i++)
		{
			for(int j = 0; j < col; j++)
			{
				if(arrchess.chess[i][j] == Chess.BLACK)
				{
					if(i >= 12)
						g.drawImage(blackchess, boardw + i * chessw  -chessr + 4, boardw + j * chessw -chessr, null);
					else 						
						g.drawImage(blackchess, boardw + i * chessw  -chessr, boardw + j * chessw -chessr, null);
				}
				else if(arrchess.chess[i][j] == Chess.WHITE)
				{
					if(i >= 12)
						g.drawImage(whitechess, boardw + i * chessw -chessr + 4, boardw + j * chessw - chessr , null);
					else
						g.drawImage(whitechess, boardw + i * chessw -chessr, boardw + j * chessw - chessr , null);
				}
			}
		}
		if (mousex < 18 * chessw + 2 * boardw && mousex >= boardw && mousey < 18 * chessw + 2 * boardw && mousey >= boardw)
		 {
			 int x = (int) Math.round(new Double(mousex-boardw)/chessw);
			 int y = (int) Math.round(new Double(mousey-boardw)/chessw);//四舍五入会超过15
			if(x < raw && y < col && arrchess.chess[x][y] == Chess.BLANK)
			{
				if(x>=12)
					g.drawImage(position, x * chessw + boardw -posir+4 , y * chessw + boardw-posir,  chessw/4*3, chessw/4*3, null);
				else
					g.drawImage(position, x * chessw + boardw -posir , y * chessw + boardw-posir,  chessw/4*3, chessw/4*3, null);
			}
		 }
		repaint();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mousex = e.getX();
		mousey = e.getY();
		repaint();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub\
		System.out.println("result" + "=" +result);
		int chessx = e.getX();
		int chessy = e.getY();
		if(clickable)
		{
			if (mousex < 18 * chessw + 2 * boardw && mousex >= boardw && mousey < 18 * chessw + 2 * boardw && mousey >= boardw)
			{
				int x = (int) Math.round(new Double(chessx - boardw)/chessw);
				int y = (int) Math.round(new Double(chessy - boardw)/chessw);
				if(arrchess.chess[x][y] == Chess.BLANK)
				{
					arrchess.chess[x][y] = gamer.getColor();
					back.push(new Message(0, x, y));//入栈
					System.out.println("rrchess.chess[" + x +"]["+ y+ "]" + "=" + gamer.getColor());
					result = 5;
					try {
						out.writeObject(new Message(0,x,y));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				clickable = false;
				if(arrchess.judgenumber(x, y, 5, 1, gamer.getColor()))
				{
					if(result == 5 || result == 3)
						result = arrchess.getresult();
						System.out.println(result);
					if(result == 1)
					{
						JOptionPane.showMessageDialog(this, "您赢了","恭喜", JOptionPane.PLAIN_MESSAGE);
					}	
				}
			}		
		}
		else if(result == -1)
		{
			JOptionPane.showMessageDialog(this, "请先准备开始游戏","提示",JOptionPane.PLAIN_MESSAGE);
		}
		else if(result == 0 || result == 1)
		{
			JOptionPane.showMessageDialog(this, "对局已结束","提示",JOptionPane.PLAIN_MESSAGE);
		} 
		else if(result == 4)
		{
			JOptionPane.showMessageDialog(this, "正等待对方的准备","提示",JOptionPane.PLAIN_MESSAGE);
		}
		System.out.println("my color is " + gamer.getColor());
		repaint();
			 //System.out.println(chessx + "  " + chessy +"    "+x + "  "+ y);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		repaint();
		
	}
/*	boolean judge(int x, int y)
	{
		return judgenumber(x, y, 5, 1);
	}*/
	public boolean isClickable() {
		return clickable;
	}
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

	public Gamer getGamer() {
		return gamer;
	}

}
