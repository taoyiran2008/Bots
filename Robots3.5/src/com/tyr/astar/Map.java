package com.tyr.astar;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.tyr.ui.Grid;
import com.tyr.ui.Robot;
import com.tyr.ui.RobotsClient;

/**
 * @author taoyr 
 * @description  说明
 * 使用最小堆（二叉堆），大大提升了执行效率
 * */
public class Map {
	public static final int WIDTH  = 20;
	public static final float SMALLERCOST=1.0f;
	public static final float BIGGERCOST=1.4f;
	public long startTime , execTime;
	private Node[][] nodeList;
	private int cols , rows;
	private Node startNode , endNode;
	private ArrayList<Robot> robots;
	private ArrayList<Node> stopNodes;
	private int[][] step =  {{0,-1} , {-1,-1} , {1,-1} , {-1,0} , {1,0} , {0,1} , {-1,1} ,{1,1}  };
	//上，左上，右上，左，右，下，左下，右下
	private int[][] step2 = {{0,-1} , {-1,0} , {1,0} , {0,1}};
	
	private RobotsClient mClient;
	RobotMoveLogic logic;
	/*public int[][] mapArr = {
	 出问题最多的地图，使用单步的判断后才解决
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1},
			{1,0,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,1,0,0,1},
			{1,1,1,1,1,1,0,1,1,1,1,0,1,1,1,0,1,0,1,0,1,1,1,1,1,0,1,1,1,1,1,0,1,1,0,1,1,0,1,1},
			{0,0,1,1,1,1,1,1,0,1,1,1,1,1,0,0,1,1,1,0,0,0,1,1,1,1,1,1,0,0,1,1,1,1,1,1,0,1,1,1},
			{1,1,1,1,0,0,1,0,0,0,1,1,1,0,0,1,1,1,1,1,0,1,1,1,0,0,1,1,1,1,1,1,0,1,1,1,1,1,1,1},
			{0,0,1,0,1,0,0,1,0,1,1,1,1,0,1,1,1,1,0,1,1,1,0,1,1,0,1,1,0,0,1,1,0,1,1,1,0,1,0,1},
			{0,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1},
			{1,0,1,1,1,1,1,0,1,0,0,0,0,1,1,0,1,0,1,0,1,0,1,1,1,1,0,0,1,0,1,1,0,1,1,1,0,0,1,1},
			{0,1,1,0,0,1,0,0,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,0,0,0,1,0,1,1,1},
			{1,0,1,1,1,0,1,0,0,1,1,0,1,1,0,0,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,0},
			{1,0,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,0,1,1,1,0,1,0,1,0,1,1,1,0,1},
			{1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,1,1,0,1,0,1,1,1},
			{0,1,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,0,1,1,1,0,0,1,1,1,1,1,1,1,1,1,0,1,1,0},
			{1,1,0,1,1,1,0,1,0,1,0,1,1,1,1,0,0,1,1,0,1,1,1,0,1,1,1,1,1,0,0,1,1,0,1,1,1,0,1,0},
			{0,1,1,1,1,1,1,0,0,0,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,0,1},
			{1,1,1,1,0,1,1,0,0,1,1,0,1,0,1,1,0,1,1,1,1,1,0,0,1,0,0,1,1,0,1,1,1,1,0,0,0,1,0,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,0,1,1,1,0,0,0,1,1,1,0,1,0,0,0,0,1,1,1,1,0,1,1,1},
			{1,1,1,0,1,0,0,1,1,1,0,0,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,1},
			{1,1,1,1,1,0,1,1,1,0,1,1,1,1,1,1,1,0,1,0,1,0,1,1,0,0,0,1,1,1,1,1,1,0,1,1,1,1,0,1},
			{1,1,1,1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,1,0,1,1},
			{1,0,1,1,0,1,1,1,1,0,1,1,0,1,1,1,0,0,0,0,1,0,1,1,1,0,1,0,0,0,0,0,1,1,1,0,0,1,1,1},
			{1,1,1,1,0,1,1,1,1,1,1,0,1,0,1,0,1,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1},
			{1,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,0,1,1,1,0,1,0,0,1,0,1,0,1,1,0,1,1,1,1,1,0,1,0,1},
			{1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0,0,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,1},
			{1,1,1,0,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0},
			{1,0,1,1,0,1,1,1,1,1,0,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,1,0,1,1,0,1,1,0,1,0,1,1,1},
			{1,0,0,0,1,1,1,1,0,0,1,1,1,1,1,0,1,1,1,1,1,1,0,0,1,1,1,0,1,1,0,1,0,1,1,1,1,1,0,1},
			{1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,0,1,0,1,1,0,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,1},
			{1,0,1,0,0,1,0,1,1,0,1,1,1,1,0,1,0,1,0,1,1,0,0,0,1,1,1,1,1,0,1,1,1,1,1,1,0,0,1,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,0,1,1,0,1,0,1,1,1,1,0,1,1,1,1,1}
			};*/
	
	private MinHeapList openList;//效率在添加、删除上面比ArrayList高，后者在按指定index查找上效率高
//	private LinkedList<Node> closeList;
	private LinkedList<Node> path;
	public void initWorld(RobotsClient client){
		mClient = client;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j <cols; j++) {
				Node node = nodeList[i][j];
				if (node.equals(endNode))
				{
					mClient.mapGrids[i][j] = new Grid(node.getX(), node.getY(), Grid.TYPE_END, node.isReachable());
				}else {
					mClient.mapGrids[i][j] = new Grid(node.getX(), node.getY(), Grid.TYPE_GRID, node.isReachable());
				}
			}
		}
	}
	public Map(int rows, int cols) {
		super();
		this.cols = cols;
		this.rows = rows;
		nodeList = new Node[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j <cols; j++) {
				nodeList[i][j] = new Node(j, i, true); //row 行数，第一 第二行分别对应着y值
			}
		}
		/*//nodeList[1][1].setReachable(false);
		nodeList[0][1].setReachable(false);
		nodeList[1][0].setReachable(false);*/
		openList =new MinHeapList(cols*rows);
//		closeList = new LinkedList<Node>();
		path = new LinkedList<Node>();
//		setBarrier(mapArr);
		setBarrier();
	}
	public void findPath(){
		if (logic==null)
		{
			logic =  new RobotMoveLogic();
			new Thread(logic).start();
		}
		startTime = System.currentTimeMillis();
		//为所有机器人找到路径
		if (robots==null)
		{
			return;
		}
		for (int i = 0; i < robots.size(); i++)
		{
			Robot robot = robots.get(i);
			if (!isAroundEndNode(nodeList[robot.getY()][robot.getX()]))//已经就位(在终点附近)的机器人不做寻路，不然会把所在的不可达位置置零
			{
				findPath(robot);
			}
		}
		execTime = System.currentTimeMillis()-startTime;
		System.out.println("time spent : " +execTime+"ms");
//		printMapArray();
	}
	public void printMapArray(){
		String str = "{\n";
		for (int i = 0; i < rows; i++) {
			String line = "{";
			for (int j = 0; j <cols; j++) {
				if (j==cols-1)
				{
					if (nodeList[i][j].isReachable())
					{
						line+= "1";
					}else {
						line+= "0";
					}
				}else {
					if (nodeList[i][j].isReachable())
					{
						line+= "1,";
					}else {
						line+= "0,";
					}
				}
			}
			if (i==rows-1)
			{
				line+="}\n";
			}else {
				line+="},\n";
			}
			str+=line;
		}
		str+="}";
		System.out.println(str);
	}
	public boolean findPath(Robot robot){
		boolean pathFinded = false;
		setStartNode(robot);
		Node node = getStartNode();
		robot.setLastStep(node);
		robot.setCurrentStep(node);//从第一次全局寻路开始保证机器人的当前位置不为空
		setWaiting(robot, true);
		
		while (true) {
			node = scanSurroundings(node);
			if (node==null) {
//				System.out.println("路径不可达");
				startNode.setReachable(false);
				robot.setPath(null);
				break;
			}else if(node.equals(endNode)) {
				pathFinded = true;
				System.out.println("找到路径");
				buildPath();
				robot.setPath(path);
				break;
			}
		}
		initMap();
		return pathFinded;
	}
	public void initMap(){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j <cols; j++) {
				nodeList[i][j].setF(0);
				nodeList[i][j].setG(0);
				nodeList[i][j].setH(0);
				nodeList[i][j].setParent(null);
				nodeList[i][j].isInCloseList = false;
				nodeList[i][j].isInOpenList = false;
			}
		}
		openList =new MinHeapList(cols*rows);
		path = new LinkedList<Node>();
	}
	public void buildPath(){
		Node node = endNode;
		path.addFirst(node);
		while ((node=node.getParent())!=null) {//只有起始点的父节点为空
			path.addFirst(node);
		}
	}
	public Node scanSurroundings(Node node){
		int x = node.getX();
		int y = node.getY();
		float f = 0;
		float g = 0;
		float h = 0;
		if (node.equals(startNode)) {
		}
		boolean isEndNear = false;//八方向下一步是否能走到
		boolean isReachable = true;//按四方向走是否都被封闭
		for (int i = 0; i < step.length; i++) {
			int posx = x+step[i][0];
			int posy = y+step[i][1];
			if (posx<0||posy<0||posx>cols-1||posy>rows-1) {
				continue; //超出地图范围
			}
			Node newNode = nodeList[posy][posx]; 
			if (newNode.equals(endNode))
			{
				isEndNear = true;
				//判断四方向行走是否有通路，走不通那么就换成八方向
				if (nodeList[y][endNode.getX()].isReachable()==false&&nodeList[endNode.getY()][x].isReachable()==false)
				{
					isReachable = false;
				}
			}
		}
		
		
		int nextStep[][] = step;
		if (isEndNear&&isReachable)
		{//最后一步按上下左右四个方向判定，从而保证把目的地牢牢围住
			nextStep = step2;
		}
		for (int i = 0; i < nextStep.length; i++) {
			int posx = x+nextStep[i][0];
			int posy = y+nextStep[i][1];
			if (posx<0||posy<0||posx>cols-1||posy>rows-1) {
				continue; //超出地图范围
			}
			Node newNode = nodeList[posy][posx]; //posy和posx别弄反了
			if (!newNode.isReachable()||newNode.isInCloseList) {
				continue; 
			}
			if (!nodeList[newNode.getY()][node.getX()].isReachable()&&!nodeList[node.getY()][newNode.getX()].isReachable())
			{
				if (isReachable)
				{
					//判断是否能从斜对角穿过去
					continue; 
				}
			
			}
			Node parent = node.getParent();
			if (i==1||i==2||i==6||i==7) {//斜对角
				if (parent==null) { //起始点
					g = BIGGERCOST;
				}else {
					g = BIGGERCOST+parent.getG();
				}
			}else {
				if (parent==null) { //起始点
					g = SMALLERCOST;
				}else {
					g = SMALLERCOST+parent.getG();
				}
			}
			h = diagonal(newNode);
			f = h+g;
			if (newNode.isInOpenList) { //如果存在则判断新的f值（g值）与之前f值的大小，并决定是否更新openlist中的值
				if (g<newNode.getG()) {
					newNode.setG(g);
//					newNode.setF(g+newNode.getH());
					newNode.setF(f);
					newNode.setParent(node);
				}
				
			}else {
				newNode.setParent(node);
				newNode.setG(g);
				newNode.setH(h);
				newNode.setF(f);
				openList.insert(newNode);
				if (newNode.equals(endNode))////找到路径
				{
					return endNode;
				}
				newNode.isInOpenList = true;
			}
		}
		/*if (openList.contains(endNode)) {//找到路径
//			System.out.println(endNode.getParent());
//			showList(openList);
			return endNode;
		}*/
//		closeList.addLast(node);
		node.isInCloseList = true;
//		if (node.isInOpenList) {
//			openList.remove(node);
			node.isInOpenList = false;
//		}
//     openList.showList(); //使用基于数组的最小堆，并没有从openlist中真正的remove掉pop出的最小元素
//而是将其放置在了数组的最底部。这种方式不用每次都对数组排序保证一个绝对的升序，只保证pop出来的最小
//		showList(openList);   //调试用，会很大影响性能
//		System.out.println(openList.pop().getF());
		if (openList.isEmpty()) {
			return null;
		}
		return openList.pop(); //弹出队列前面最小的值
		
		
	}
	public Node getStartNode() {
		return startNode;
	}
	/*public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}*/
	public void setStartNode(Robot robot) {
		this.startNode = nodeList[robot.getY()][robot.getX()];
		startNode.setReachable(true);
		if (isAroundEndNode(startNode))
		{
			startNode.setReachable(false);
		}
		mClient.mapGrids[robot.getY()][robot.getX()].setReachable(true);
	}
	public void setRobots(ArrayList<Robot> robots){
		/*if (robots==null)
		{
			robots = new ArrayList<Robot>();
		}*/
		this.robots = robots;
	}
	public Node getEndNode() {
		return endNode;
	}
	public void setEndNode(Node endNode) {
		this.endNode = endNode;
		endNode.setReachable(true);
		mClient.mapGrids[endNode.getY()][endNode.getX()].setReachable(true);
		mClient.mapGrids[endNode.getY()][endNode.getX()].setType(Grid.TYPE_END);
		
		stopNodes = new ArrayList<>();
		int x = endNode.getX();
		int y = endNode.getY();
		for (int i = 0; i < step.length; i++) {
			int posx = x+step[i][0];
			int posy = y+step[i][1];
			if (posx<0||posy<0||posx>cols-1||posy>rows-1) {
				continue; //超出地图范围
			}
			stopNodes.add(nodeList[posy][posx]);
		}
	}
	public void showList(LinkedList<Node> list){
		for (int i = 0; i < list.size(); i++) {
			System.out.println("node["+list.get(i).getX()+"]["+list.get(i).getY()+"]--"+list.get(i).getF());
		}
		System.out.println("————");
	}
	public Node[][] getNodeList() {
		return nodeList;
	}
	public void setNodeList(Node[][] nodeList) {
		this.nodeList = nodeList;
	}
	
	//对角线估价法
	private float diagonal(Node node)
	{
		float dx = Math.abs(node.getX()-endNode.getX());
		float dy = Math.abs(node.getY()-endNode.getY());
		float shortLine  = Math.min(dx, dy);
		float longLine =dx+dy-shortLine;

	    return  (longLine-shortLine)*SMALLERCOST+shortLine*shortLine;//(长边-短边）*直线代价 + 短边 * 对角线代价
	}
	public void printMap(){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j <cols; j++) {
				if (nodeList[i][j].equals(startNode))
				{
					System.out.print("s"+"    ");
				}else if (nodeList[i][j].equals(endNode)) {
					System.out.print("e"+"   ");
				}else if (nodeList[i][j].isReachable()) {
					System.out.print("□"+"   ");//☆■△
				}else {
					System.out.print("■"+"   ");
				}
			}
			System.out.println();
		}
	}
	public void printPath(){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j <cols; j++) {
				if (path.contains(nodeList[i][j]))
				{
					System.out.print("☆"+"   ");
				}else if (nodeList[i][j].isReachable()) {
					System.out.print("□"+"   ");
				}else {
					System.out.print("■"+"   ");
				}
			}
			System.out.println();
		}
	}
	public void setBarrier(){
		Random r = new Random();
		//player和目的地必须为可达
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j <cols; j++) {
				if (r.nextInt(8)<2)
				{
					nodeList[i][j].setReachable(false);
				}
				/*if (nodeList[i][j].equals(startNode)||nodeList[i][j].equals(endNode))
				{
					nodeList[i][j].setReachable(true);
				}*/
			}
		}
		/*nodeList[1][0].setReachable(false); //第一行，第0列
		nodeList[1][1].setReachable(false);
		nodeList[1][2].setReachable(false);
		nodeList[1][4].setReachable(false);
		nodeList[1][5].setReachable(false);
		nodeList[1][6].setReachable(false);
		nodeList[1][7].setReachable(false);
		nodeList[0][7].setReachable(false);*/
		
		
	}
	public void setBarrier(int args[][]){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j <cols; j++) {
				if (args[i][j]==1)
				{
					nodeList[i][j].setReachable(true);
				}else {
					nodeList[i][j].setReachable(false);
				}
			}
		}
	}
	
	public boolean isCross(Robot robot){
		for (int i = 0; i < robots.size(); i++)
		{
			//能互相穿过的情况
			Robot r = robots.get(i);
			if (r.getId()==robot.getId())
			{
				continue;
			}
			if (!robot.havePath()||!r.havePath())
			{
				continue;
			}
			if (r.getCurrentStep().equals(robot.getPath().getFirst())&&
					robot.getCurrentStep().equals(r.getPath().getFirst())
					&&robot.getId()<r.getId())//抓小放大原则，id小者将被留下
			{
				return true;
			}
		}
		return false;
	}
	public boolean isBlocked(int x , int y){
		/*if (nodeList[y][x].isReachable()==false)
		{
			return true;
		}
		return false;*/
		return !nodeList[y][x].isReachable();
	}
	public boolean isWaiting(Robot robot){
		//判断机器人的下一步是否可以通行
		Node node = robot.getPath().getFirst();
		if (node.equals(robot.getCurrentStep()))
		{
			return false; //下一步为当前位置的时候（刚刚搜寻到路径的时候）
		}
		return node.isWaiting;
		
	}
	public boolean isFindable(Robot robot){
		Node node = robot.getCurrentStep();
		//是否已经就位
		if (isAroundEndNode(node))
		{
			return false;
		}
		
		//是否影响其他机器人下一步的判断
		for (int i = 0; i < robots.size(); i++)
		{
			Robot r = robots.get(i);
			if (robot.getId()==r.getId())
			{
				continue;
			}
			
			if (r.havePath())
			{
				if (r.getPath().getFirst().equals(node))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	/*public Robot getRobot(Node step){
		for (int i = 0; i < robots.size(); i++)
		{
			Robot r = robots.get(i);
			if (r.getPath()==null)
			{
				return null;
			}
			if (step.equals(r.getPath().getFirst()))
			{
				return r;
			}
			
		}
		return null;
				
	}*/
	public Node getRandomNextStep(Robot robot){
		int i = -1;
		int x = robot.getX();
		int y = robot.getY();
		Random r = new Random();
		if (robot.getDirection()!=-1)
		{
			i = robot.getDirection();
			int posx = x+step[i][0];
			int posy = y+step[i][1];
			if (posx<0||posy<0||posx>cols-1||posy>rows-1
					||isBlocked(posx,  posy)
					||(!nodeList[robot.getY()][posx].isReachable()&&!nodeList[posy][robot.getX()].isReachable())) {
				i = r.nextInt(step.length);
			}
		}else {
			i = r.nextInt(step.length);
		}
		int posx = x+step[i][0];
		int posy = y+step[i][1];
		if (posx<0||posy<0||posx>cols-1||posy>rows-1) {
			return null;
		}
		if (isBlocked(posx,  posy)||(!nodeList[robot.getY()][posx].isReachable()&&!nodeList[posy][robot.getX()].isReachable()))//
		{
			return null;
		}
		Node newNode = nodeList[posy][posx]; 
		robot.setDirection(i);
		//判断是否可以走，前方不能有障碍，且不能斜对角穿过障碍
		
		return newNode;
	}
	public void backward(int index){
		 for (int i = 0; i < index; i++)
		{
			Robot robot = robots.get(i);
			if (robot.havePath())
			{
				Node lastStep = robot.getLastStep();
				robot.getPath().addFirst(robot.getCurrentStep());//还原上一步路径
				robot.setCurrentStep(lastStep);
				robot.setX(lastStep.getX());
				robot.setY(lastStep.getY());
			}
		}
	 }
	public void setWaiting(Robot robot,boolean flag){
		 if (robot.getCurrentStep()!=null)
		{
			 nodeList[robot.getCurrentStep().getY()][robot.getCurrentStep().getX()].isWaiting = flag;
		}
	}
	public boolean isAroundEndNode(Node node){
		return stopNodes.contains(node);
	}
	public boolean isNoPathCanBeFound(){
		for (int i = 0; i < robots.size(); i++)
		{
			Robot robot = robots.get(i);
			if (robot.havePath())
			{
				return false; 
			}
		}
		return true;
	}
	public boolean isAllRobotsDead(){
		//所有机器人的路径情况处于互相等待状态
		for (int i = 0; i < robots.size(); i++)
		{
			Robot robot = robots.get(i);
			if (robot.havePath())
			{
				if (!isWaiting(robots.get(i)))
				{
					return false; 
				}
			}
		}
		return true;
	}
	
	class RobotMoveLogic implements Runnable
	{//统一 一个线程控制，如果每个Robot都使用一个线程的话对
	//mClient.map.isConflict的使用可能会造成线程不同步的问题
		boolean rollback = false;
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(RobotsClient.FRAMERATE_LARGE);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				if (RobotsClient.isPause)
				{
					continue;
				}
				if (isAllRobotsDead())
				{
					if (!isNoPathCanBeFound())
					{
						findPath();
					}
				}
				//为暂时寻路不成功的机器人寻路
				//已经到位的机器人不寻路，如果存在其他机器人下一步为其当前所在位置时，不寻路（会导致可到达状态清空）
				for (int i = 0; i < robots.size(); i++)
				{
					Robot robot = robots.get(i);
					if (!robot.havePath() && isFindable(robot))
					{
						if (robot.isNeedRefind())
						{
//							System.out.println("重寻路");
							if (findPath(robot))
							{
								robot.setFailedTimes(0);
							}
						}else {
							//单步判断的散兵游勇机制
							Node step = getRandomNextStep(robot);
							if (step!=null)
							{
								//下面会改变机器人的CurrentStep
								Node node = robot.getCurrentStep();
								LinkedList<Node> path = new LinkedList<Node>();
								path.addFirst(step);
								robot.setPath(path);
								robot.setCurrentStep(node);//setPath会重置机器人的当前位置，所以这里要做特殊处理
								if (!isWaiting(robot))
								{
									setWaiting(robot, false);
									node.setReachable(true);
									robot.move();
									setWaiting(robot, true);
									robot.getCurrentStep().setReachable(false);
								}
							}
						}
					}

				}
				//优先判断会停止成为障碍物的机器人 
				for (int i = 0; i < robots.size(); i++){
					Robot robot = robots.get(i);
					if (robot.havePath())
					{
						Node node = robot.getPath().getFirst();
						int x = node.getX();
						int y = node.getY();
						if (x==endNode.getX()&&y==endNode.getY())
						{
							robot.getCurrentStep().setReachable(false);//对象引用，直接修改内存中的值
							robot.setPath(null);
						}
					}
				}
				
				
				//再判断在新的地图上机器人是否需要重新寻路
				for (int i = 0; i < robots.size(); i++){
					Robot robot = robots.get(i);
					if (robot.havePath())
					{
						Node node = robot.getPath().getFirst();
						int x = node.getX();
						int y = node.getY();
						if (isBlocked(x, y))
						{
							if (findPath(robot))
							{
								robot.getPath().pop();
//								rollback = true; 
								//做判断不必回滚
								node = robot.getPath().getFirst();
								x = node.getX();
								y = node.getY();
								if (x==endNode.getX()&&y==endNode.getY())
								{
									robot.getCurrentStep().setReachable(false);//对象引用，直接修改内存中的值
									robot.setPath(null);
									rollback = true;
								}
								
							}else {
								robot.getCurrentStep().setReachable(false);//前方有阻碍，第二次寻路中走不通，机器人停止并设置不可达状态，解决注释中地图2的情况
								rollback = true;
							}
						}
					}
				}
				if (rollback)
				{
					rollback = false;
					continue;
				}
				//判断是否因为会有发生相互穿越的情况
				//穿越情况会导致机器人进入互相等待的死循环
				for ( int i = 0; i < robots.size(); i++)
				{
					Robot robot = robots.get(i);
					if (robot.havePath())
					{
						if(isCross(robot)){
							robot.getCurrentStep().setReachable(false); //暂时不可通过
							rollback = true;
						}
					}
				}
				if (rollback)
				{
					rollback = false;
					continue;
				}
				
				//移动
				for ( int i = 0; i < robots.size(); i++)
				{
					Robot robot = robots.get(i);
					if (robot.havePath())
					{
						if(!isWaiting(robot)){//能够行走的条件
							setWaiting(robot, false);
							robot.move();
							setWaiting(robot, true);
						}
					}
				}
				
			}
		}
	}
}
