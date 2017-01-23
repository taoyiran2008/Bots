package com.tyr.ui;

import java.awt.Graphics;
import java.awt.Image;

import com.tyr.astar.Map;
import com.tyr.util.CommonUtil;

public class Grid
{
	private int x;
	private int y;
	public static final int TYPE_GRID=0;
	public static final int TYPE_END = 1;
	private int type;
	private boolean reachable = true;
	
	
	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public boolean isReachable()
	{
		return reachable;
	}

	public void setReachable(boolean reachable)
	{
		this.reachable = reachable;
	}

	public Grid(int x, int y, int type, boolean reachable)
	{
		super();
		this.x = x;
		this.y = y;
		this.type = type;
		this.reachable = reachable;
	}

	public void paint(Graphics g){
		Image image = null;
		switch (type)
		{
		case TYPE_GRID:
			if (reachable)
			{
				image = CommonUtil.loadImage("grid.png");
			}else {
				image = CommonUtil.loadImage("wall.png");
			}
			break;
		case TYPE_END:
			image = CommonUtil.loadImage("end.png");
			break;

		default:
			break;
		}
		if (image!=null)
		{
			g.drawImage(image,x*Map.WIDTH,y*Map.WIDTH,null);
		}
	}
	
}
