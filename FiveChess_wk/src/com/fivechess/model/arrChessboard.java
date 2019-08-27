package com.fivechess.model;

import javax.naming.spi.DirStateFactory.Result;

public class arrChessboard {
	int raw, col;
	public int chess[][];
	int result = 3;
	public int getresult() {
		return result;
	}
	public arrChessboard(int r, int c) {
		// TODO Auto-generated constructor stub
		raw = r;
		col = c;
		chess = new int[raw][col];
	}
	public void initchess() {
		for(int i = 0; i < raw; i++)
		{
			for(int j = 0; j < col; j++)
			{
				chess[i][j] = Chess.BLANK;
			}
		}
	}
	public boolean judgenumber(int x, int y, int number, int r,int color)
	{
		int count = 1 ;
		
		//纵向
		for(int i=1, j=1; i<number && j<number;)
		{//如果情况允许则继续才看是否有连子
			if(!(y-j<0 || (y-j>=0 && chess[x][y-j] != color)) || !(y+i>=col || (y+i<col && chess[x][y+i] !=color)))
			{
				if(y-j>=0 && chess[x][y-j] == color)
				{
					j++;
					count++;
				}
				if(y+i<col && chess[x][y+i] == color)
				{
					i++;
					count++;
				}
			}
			else break;
			
		}
		//System.out.println("count:" + count);
		if(count < number)
		{
			count = 1;
		}
		else
		{
			result =r ;
			return true;
		}
		
		//System.out.println("x:" + x + "  " +"y:" + y);
		//横向
		for(int i=1, j=1; i<number && j<number;)
		{//如果情况允许则继续才看是否有连子
			if(!(x-j<0 || (x-j>=0 && chess[x-j][y] != color)) || !(x+i>=raw || (x+i<raw && chess[x+i][y] !=color)))
			{
				if(x-j>=0 && chess[x-j][y] == color)
				{
					j++;
					count++;
				}
				if(x+i<raw && chess[x+i][y] == color)
				{
					i++;
					count++;
				}
			}
			else break;
		}
		//System.out.println("count :" + count);
		if(count < number)
		{
			count = 1;
		}
		else
		{
			result = r;
			return true;
		}
		//西北
		for(int i=1, j=1; i<number && j<number;)
		{//如果情况允许则继续才看是否有连子
			if (!(x-j<0 || y-j<0 || (x-j>=0 && y-j>=0 && chess[x-j][y-j] != color)) || !(x+i>=raw || y+i>=col || (x+i<raw && y+i<col && chess[x+i][y+i] !=color))) {
				if(x-j>=0 && y-j>=0 && chess[x-j][y-j] == color)
				{
					j++;
					count++;
				}
				if(x+i<raw && y+i<col && chess[x+i][y+i] == color)
				{
					i++;
					count++;
				}
			}
			else break;
		}
		//System.out.println("count :" + count);
		if(count < number)
		{
			count = 1;
		}
		else
		{
			result = r;
			return true;
		}
		//西南
		for(int i=1, j=1; i<number && j<number;)
		{//如果情况允许则继续才看是否有连子
			if(!(x-j<0 || y+j >=col || (x-j>=0 && y+j<col && chess[x-j][y+j] != color) || !(x+i>=raw || y-i<0 || (x+i<raw && y-i>=0 && chess[x+i][y-i] !=color))))
			{
				if(x-j>=0 && y+j<col && chess[x-j][y+j] == color)
				{
					j++;
					count++;
				}
				if(x+i<raw && y-i>=0 && chess[x+i][y-i] == color)
				{
					i++;
					count++;
				}
			}
			else break;
			
		}
		//System.out.println("count :" + count);
		if(count < number)
		{
			count = 1;
		}
		else
		{
			result = r;
			return true;
		}
		return false;
	}
}
