package com.fivechess.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.fivechess.model.Chess;
import com.fivechess.model.Gamer;
import com.fivechess.model.Message;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.NetworkInterface;
import java.nio.channels.NetworkChannel;
import java.awt.event.ActionEvent;

public class chessMainboard extends JFrame implements ActionListener{
	public chessBoard chessboard;
	private JButton btback;
	private JPanel contentPane;
	public JButton btstart;
	public JButton btlose;
	private JButton regchess;
	private JLabel label;
	private int result = 0;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	/**
	 * Create the frame.
	 */
	public chessMainboard(Gamer g, ObjectInputStream in, ObjectOutputStream out) {
		this.in =in;	
		this.out = out;
		chessboard = new chessBoard(g, in, out);
		setBounds(100, 100, chessboard.getWidth() + 200, chessboard.getHeight()+50);
		chessboard.setBounds(0, 0, chessboard.getWidth(), chessboard.getHeight());
		setTitle("五子棋_wk");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		btback = new JButton("返回主菜单");
		btback.addActionListener(this);
		btback.setBounds(0, 0, btback.getWidth(), btback.getHeight());
		label = new JLabel("状态信息");
		
		btstart = new JButton("准备开始");
		btstart.addActionListener(this);
		
		regchess = new JButton("悔棋");
		regchess.addActionListener(this);
		
		btlose  = new JButton("认输");
		btlose.addActionListener(this);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(chessboard, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(label)
							.addGap(55))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(regchess, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(btstart, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(btback, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(btlose, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
							.addGap(21))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(chessboard, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(49)
							.addComponent(label)
							.addGap(25)
							.addComponent(btstart, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addGap(47)
							.addComponent(btback, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addGap(47)
							.addComponent(regchess, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addGap(46)
							.addComponent(btlose, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if(chessboard != null)
		{
			result=chessboard.getResult();
		}
		if(result == 0 || result == 1)
		{
			btstart.setText("再来一局");
			
			if(result == 0)
				label.setText("您输了");
			else if(result == 1)
				label.setText("您赢了");
		}
		else if(result == 4)
		{
			label.setText("您已经准备");
		}
		else if(result == 3)
		{
			label.setText("您正在下棋");
		}
		else if(result == 5)
		{
			label.setText("对方正在下棋");
		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btstart)//开始游戏
		{
			chessboard.setResult(4);
			if(btstart.getText().equals("再来一局"))
			{
				chessboard.gamer.setColor(Chess.changecolor(chessboard.gamer.getColor()));
				chessboard.arrchess.initchess();
				chessboard.setResult(4);
				btstart.setText("开始游戏");
			}
			else if (btstart.getText().equals("开始游戏")) 
			{
				chessboard.arrchess.initchess();
			}
		}
		else if (e.getSource() == btback)//返回主菜单
		{
			dispose();
			new chessstart();
		}
		else if (e.getSource() == regchess)
		{
			if(chessboard.getResult() == 5)//5对方下棋中
			{
				try {
					chessboard.out.writeObject(new Message(2));
				} catch (IOException e1) {
				// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		else if(e.getSource() == btlose)//认输
		{
			try {
				chessboard.out.writeObject(new Message(7));//发送认输消息
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("认输");
		}
	}
}
