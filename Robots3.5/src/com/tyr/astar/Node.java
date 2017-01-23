package com.tyr.astar;


/**
 * 
 * @author taoyr
 * @param f
 * 寻路总代价
 * @param g 
 * 指定节点到相邻节点的代价
 * @param h 
 * 指定节点到目标节点的代价
 */
public class Node implements Comparable<Node> {
	private int x;
	private int y;
	private float f , g , h;
	private Node parent;
	private boolean reachable;
	public boolean isInCloseList = false;
	public boolean isInOpenList = false;
	public boolean isWaiting = false;
	public Node(int x, int y, boolean reachable) {
		super();
		this.x = x;
		this.y = y;
		this.reachable = reachable;
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
	public float getF() {
		return f;
	}
	public void setF(float f) {
		this.f = f;
	}
	public float getG() {
		return g;
	}
	public void setG(float g) {
		this.g = g;
	}
	public float getH() {
		return h;
	}
	public void setH(float h) {
		this.h = h;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public boolean isReachable() {
		return reachable;
	}
	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}
	@Override
	public int compareTo(Node node) {
		if (getF()>node.getF()) {
			return 1;
		}else if (getF()<node.getF()) {
			return -1;
		}
		return 0;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof Node)) return false;
		Node node=(Node)o;
		if (getX()==node.getX()&&getY()==node.getY()) {
			return true;
		}else {
			return false;
		}

	}
}
