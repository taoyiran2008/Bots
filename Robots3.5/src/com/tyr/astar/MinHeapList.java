package com.tyr.astar;



public class MinHeapList {  
	private Node heapArray[];// 堆容器  
    private int currentSize; // 堆大小  
   
    public MinHeapList(int max) {  
        heapArray = new Node[max];
        currentSize = 0;  
    }  
    /** 
     * 自上而下调整 
     *  
     * @param start 
     * @param endOfHeap 
     */  
    public int parent(int i) {  
        return (i - 1) >> 1;  //(i-1)/2
    }  
    private int left(int i) {  
        return ((i + 1) << 1) - 1;  //2 * i + 1
    }  
    private int right(int i) {  //2*i+2
        return (i + 1) << 1;  
    }  
    public void filterDown(int start, int endOfHeap) {  
        int i = start;  
        int j = left(i); // j是i的左子女位置  
        Node temp = heapArray[i];  
  
        while (j <= endOfHeap) { // 检查是否到最后位置  
            if (j < endOfHeap // 让j指向两子女中的小者  
                    && heapArray[j].getF() > heapArray[j+1].getF()) {  
                j++;  
            }  
            if (temp.getF() <= heapArray[j].getF()) { // 小则不做调整  
                break;  
            } else { // 否则小者上移，i，j下降  
            	/*heapArray[i] = heapArray[j];
            	heapArray[j] = temp;
                i = j;  
                j = left(i);  
               
                temp = heapArray[i];  */
            	heapArray[i] = heapArray[j];
            	heapArray[j] = temp;
                i = j;  
                j = left(i);  
            }  
        }  
    }  
  
    /** 
     * 自下而上的调整:从结点start开始到0为止，自下向上比较，如果子女的值小于双亲结点的值则互相交换 
     *  
     * @param start 
     */  
    public void filterUp(int start) {  
        int j = start;  
        int i = parent(j);  
        Node temp = heapArray[j];  
  
        while (j > 0) { // 沿双亲结点路径向上直达根节点  
            if (heapArray[i].getF() <= temp.getF()) {// 双亲结点值小，不调整  
                break;  
            } else {// 双亲结点值大，调整  
            	heapArray[j] = heapArray[i];
            	heapArray[i] = temp;
            			
                j = i;  
                i = parent(j); 
                
//                temp = heapArray[j];  
            }  
           
        }  
    }  
  
    /** 
     * 堆中插入结点 
     *  
     * @param key 
     * @return 
     * @throws MinHeapException 
     */  
    public void insert(Node node){  
//    		heapArray.add(node);
    		heapArray[currentSize]= node;
            filterUp(currentSize);  
            currentSize++;  
    }  
  
    /** 
     * 删除堆中的最小值 
     *  
     * @return 
     * @throws MinHeapException 
     */  
    public Node pop() {  
        /*Node root = heapArray.get(0);  
        root = heapArray.get(heapArray.size()-1);  */
    	Node root = top();
    	heapArray[0] = heapArray[currentSize-1];
        currentSize--;  
        filterDown(0, currentSize - 1);  
        return root;  
    }  
    public Node top(){
    	return heapArray[0];
    }
    /** 
     * 按某种格式输出堆 
     */  
    public void displayHeap() {  
        System.out.print("heapArray: ");  
        for (int i = 0; i < currentSize; i++) {  
            if (heapArray[i] != null) {  
                System.out.print(heapArray[i].getF() + " ");  
            } else {  
                System.out.print("-- ");  
            }  
        }  
        System.out.println();  
  
        int nBlanks = 32; // heap format  
        int itemsPerRow = 1;  
        int column = 0;  
        int j = 0; // current item  
        String dots = "...............................";  
        System.out.println(dots + dots); // dotted top line  
  
        while (currentSize > 0) { // for each heap item  
            if (column == 0) { // first item in row  
                for (int k = 0; k < nBlanks; k++) { // preceding blanks  
                    System.out.print(" ");  
                }  
            }  
            System.out.print(heapArray[j].getF()); // display item  
  
            if (++j == currentSize) { // done?  
                break;  
            }  
  
            if (++column == itemsPerRow) { // end of row?  
                nBlanks /= 2; // half the blanks  
                itemsPerRow *= 2; // twice the items  
                column = 0; // start over on  
                System.out.println(); // next row  
            } else { // next item on row  
                for (int k = 0; k < nBlanks * 2 - 2; k++) {  
                    System.out.print(' '); // interim blanks  
                }  
            }  
        }  
        System.out.println("\n" + dots + dots);  
    }  
  
    public boolean isEmpty() {  
        return currentSize==0;  
    }  
    public void showList(){
		for (int i = 0; i < currentSize; i++) {
			System.out.println("node["+heapArray[i].getX()+"]["+heapArray[i].getY()+"]--"+heapArray[i].getF());
		}
		System.out.println("————");
	}
  
    public void makeEmpty() {  
        currentSize = 0;  
    }  
   /* public void initMinHeap(int[] args){
		for (int i = 0; i < args.length; i++)
		{
			insert(args[i]);
		}
		int n = args.length/2;
		while (n>0)
		{
			filterUp(n);
			n--;
		}
    }*/
    
}  
