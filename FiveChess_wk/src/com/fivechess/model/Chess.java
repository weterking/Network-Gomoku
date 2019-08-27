package com.fivechess.model;

public class Chess {
	public final static int BLANK=0;
	public final static int WHITE=1;
	public final static int BLACK=2;
	public final static int changecolor(int color)
	{
		if(color == BLACK)
			return WHITE;
		else if(color == WHITE)
			return BLACK;
		return BLANK;
	}
}