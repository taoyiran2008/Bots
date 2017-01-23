package com.tyr.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;

import com.tyr.astar.Map;
import com.tyr.astar.Node;
import com.tyr.util.CommonUtil;

public class Robot
{
	private int x;
	private int y;
	private int id;
	private LinkedList<Node> path;
	private Node lastStep;
	private Node currentStep;
	private RobotsClient mClient;
	private int failedTimes ; 
	private long lastFindPathTime; //ms
	private static final int INTERVAL = 3; //s
	private static final int TIMES = 5; 
	private int direction=-1;
	
	//用于平滑移动
	public int xpos , ypos;
	public int xspeed , yspeed;
	public int speed = 4;
	

	public Robot(int x, int y ,int id ,RobotsClient client)
	{
		super();
		this.x = x;
		this.y = y;
		this.id = id;
		mClient = client;
	}

	public boolean havePath()
	{
		if (path==null)
		{
			return false;
		}
		return !(path.size()==0);
	}

	public void paint(Graphics g)
	{
		Image image = null;
		image = CommonUtil.loadImage("player.png");
		xpos += xspeed;
		ypos += yspeed;
		if (xspeed>0&&xpos>x*Map.WIDTH)
		{
			xpos=x*Map.WIDTH;
		}else if (xspeed<0&&xpos<x*Map.WIDTH)
		{
			xpos=x*Map.WIDTH;
		}
		if (yspeed>0&&ypos>y*Map.WIDTH)
		{
			ypos=y*Map.WIDTH;
		}else if (yspeed<0&&ypos<y*Map.WIDTH)
		{
			ypos=y*Map.WIDTH;
		}
		if (image != null)
		{
			g.drawImage(image, xpos, ypos, null);
//			g.drawString(""+getId(), xpos+5, ypos+15);
		}
	}

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

	public LinkedList<Node> getPath()
	{
		return path;
	}

	public void setPath(LinkedList<Node> path)
	{
		this.path = path;
		if (path!=null&&!path.isEmpty())
		{
			setCurrentStep(path.getFirst());
		}/*else {
			setCurrentStep(null);
		}*/
		
	}
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Node getLastStep()
	{
		return lastStep;
	}

	public void setLastStep(Node lastStep)
	{
		this.lastStep = lastStep;
	}

	public Node getCurrentStep()
	{
		return currentStep;
	}

	public void setCurrentStep(Node currentStep)
	{
		this.currentStep = currentStep;
	}

	public boolean isNeedRefind(){
		//重寻路与等待机制
		failedTimes++;
		if (failedTimes<TIMES)
		{
			return true;
		}else if(failedTimes==TIMES) {
			lastFindPathTime = System.currentTimeMillis();
		}else {
			long currentTime = System.currentTimeMillis();
			if ((currentTime-lastFindPathTime)/1000>INTERVAL)
			{
				failedTimes = 0;
				return true;
			}
		}
		
		return false;
	}
	public int getFailedTimes()
	{
		return failedTimes;
	}

	public void setFailedTimes(int failedTimes)
	{
		this.failedTimes = failedTimes;
	}

	public long getLastFindPathTime()
	{
		return lastFindPathTime;
	}

	public void setLastFindPathTime(long lastFindPathTime)
	{
		this.lastFindPathTime = lastFindPathTime;
	}
	
	public void move(){
		//按当前所持有的路径移动
		getCurrentStep().setReachable(true);
		setLastStep(getCurrentStep());
		setCurrentStep(getPath().pop());
		setX(getCurrentStep().getX());
		setY(getCurrentStep().getY());
		
		xpos = getLastStep().getX()*Map.WIDTH;
		ypos = getLastStep().getY()*Map.WIDTH;
		if (x>getLastStep().getX())
		{
			xspeed = speed;
		}else if (x<getLastStep().getX())
		{
			xspeed = -speed;
		}else {
			xspeed = 0;
		}
		if (y>getLastStep().getY())
		{
			yspeed = speed;
		}else if (y<getLastStep().getY())
		{
			yspeed = -speed;
		}else {
			yspeed = 0;
		}
	}
	public int getDirection()
	{
		return direction;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}


}
