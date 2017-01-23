package com.tyr.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.tyr.astar.Map;

public class RobotsClient extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	public static final int SCREENWIDTH=800;
	public static final int SCREENHEIGHT=600;
	public static final int FRAMERATE=40;
	public static final int FRAMERATE_LARGE=200;
	public World world;
	public JPanel buttonPanel;
	public JButton exit;
	public JButton start;
	public JButton resume;
	public JButton pause;
	public static boolean isPause =false;
	public Map map;
	public int rows = SCREENHEIGHT/Map.WIDTH;
	public int cols = SCREENWIDTH/Map.WIDTH;
	public Grid mapGrids[][];
	public ArrayList<Robot> robots;
	public RobotsClient(){
		map = new Map(rows, cols );
		mapGrids = new Grid[rows][cols];
		robots = new ArrayList<Robot>();
		map.initWorld(this);
		Random r = new Random();
		int row = r.nextInt(rows);
		int col = r.nextInt(cols);
		map.setEndNode(map.getNodeList()[row][col]);
//		map.setEndNode(map.getNodeList()[20][20]);
		createRobots(68);
		/*robot = new Robot(1, 0 ,1,this);//x为2,y为0即第0行，第2列
		robots.add(robot);
		robot = new Robot(2, 0 ,2,this);
		robots.add(robot);
		robot = new Robot(6, 0 ,3,this);
		robots.add(robot);
		robot = new Robot(4, 0 ,4,this);
		robots.add(robot);
		robot = new Robot(5, 0 ,5,this);
		robots.add(robot);
		map.setRobots(robots);*/
		map.findPath();
	}
	public void createRobots(int num){
		for (int i = 0; i < num; i++)
		{
			Random r = new Random();
			int y = r.nextInt(rows);
			int x = r.nextInt(cols);
			if (x==map.getEndNode().getX()&&y==map.getEndNode().getY())
			{
				i--;
				continue;
			}
			Robot robot = new Robot(x, y, i, this); 
//			Robot robot = new Robot(i, 0, i, this); 
			robots.add(robot);
		}
		map.setRobots(robots);
	}
	public void launchFrame()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setUndecorated(true);//去除窗口，只显示出绘图区域
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		Container contentPanel = this.getContentPane();//在contentPanel里面绘制，不然Frame的绘图区域包括标题栏
		contentPanel.setLayout(new BorderLayout());
		world = new World(this);
		contentPanel.add(world, BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		exit = new JButton("退出");
		start = new JButton("寻路");
		resume = new JButton("重新开始");
		pause = new JButton("暂停");
		buttonPanel.add(exit);
		buttonPanel.add(start);
		buttonPanel.add(pause);
		buttonPanel.add(resume);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		resume.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
				new RobotsClient().launchFrame();
			}
		});
		pause.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				isPause= !isPause;
				if (isPause)
				{
					pause.setText("行走");
				}else {
					pause.setText("暂停");
				}
			}
		});
		start.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				map.findPath();
				/*for (int i = 0; i < 5; i++)
				{
					new RobotsClient().launchFrame();
				}*/
			}
		});
		
		this.pack();

		this.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode()==KeyEvent.VK_DOWN)
				{
					map.getNodeList()[map.getEndNode().getY()+1][map.getEndNode().getX()].setReachable(true);
					map.getNodeList()[map.getEndNode().getY()+1][map.getEndNode().getX()].isWaiting = false;
					System.out.println("下方机器人被杀死");
				}
			}
	    	 
		});
		this.setFocusable(true); //设置窗口焦点，不然键盘事件不会响应
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(SCREENWIDTH, SCREENHEIGHT+30));
		windowToCenter();
		this.setTitle("Robot Battle");
//		this.setBackground(null);//透明
	    this.setVisible(true);
	    
	}
	public void windowToCenter(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = this.getSize();
		int x = (screenSize.width-windowSize.width)/2;
		int y = (screenSize.height-windowSize.height)/2;
		this.setLocation(x, y);
	}
	
	@Override
	public void paint(Graphics arg0)
	{
		super.paint(arg0);
	}
	
	public static void main(String[] args)
	{
		new RobotsClient().launchFrame();
		/*new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();
		new RobotsClient().launchFrame();*/
		
	}
}
