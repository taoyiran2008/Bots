package com.tyr.ui;

import java.awt.Graphics;

import javax.swing.JPanel;

public class World extends JPanel
{
	private static final long serialVersionUID = 1L;
	private RobotsClient mClient;
	private boolean firstTime = true;

	public World(RobotsClient client)
	{
		mClient = client;
		new Thread(new PaintThread()).start();
	}

	public void paint(Graphics g)
	{
		//每次都必须重绘背景，不然会出现上一次画面不被擦出的洋葱皮效果
		for (int i = 0; i < mClient.rows; i++)
		{
			for (int j = 0; j < mClient.cols; j++)
			{
				Grid grid = mClient.mapGrids[i][j];
				grid.paint(g);
			}
		}
		for (int i = 0; i < mClient.robots.size(); i++)
		{
			mClient.robots.get(i).paint(g);
		}
	}

	class PaintThread implements Runnable
	{
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(RobotsClient.FRAMERATE);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				repaint();
			}
		}
	}
}
