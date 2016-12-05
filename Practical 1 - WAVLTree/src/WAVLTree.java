

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */


public class WAVLTree {
	
	private WAVLNode root;
	//private final WAVLNode EXTLeaf;
	private WAVLNode min;
	private WAVLNode max;
	private int size; //update every delete\insert op
	
public WAVLNode getRoot() {
		return root;
	}
public WAVLNode getMin() {
		return min;
	}
public WAVLNode getMax() {
		return max;
	}
public void setMax(WAVLNode max) {
	this.max=max;
	}
public void setMin(WAVLNode min) {
	this.min=min;
	}
public int getSize() {
		return size;
	}



/**
 * WAVL constructor
 * create a new empty tree
 * 
 */
public WAVLTree(){
	this.root=null;
	this.min=null;
	this.min=null;
	this.size=0;
	

}
	
	

  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() { //check with Omri if I can use this
	  return 0==size;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String rec_search(WAVLNode node, int k){
	  if (node == null){
			return null;
		}
		else if (node.key == k) {
			return node.info;
		}
		else if (k > node.key) {
			return rec_search(node.right, k);
		}
		else if (k < node.key) {
			return rec_search(node.left, k);
		}
			
		return null;
  }
  
  
  public String search(int k)
  {
	  return rec_search(this.root, k);
  }
  
  public void Rotate_Left(WAVLNode node)
  {
	  
	  WAVLNode b = node.left;
	  node.left = node.parent;
	  node.parent = node;
	  node.left.right = b;
	  b.parent = node.left;

	  return;
  }
  
   private WAVLNode findPlace(int k,String i, WAVLNode node){
	   if (node.isExternalLeaf()){
		   return new WAVLNode(k,i,node.getParent());
	   }
	   else if (node.key==k)
		   return node;
	   else if (k<node.key){
		   node.left = findPlace(k,i,node.left);
	   }
	   else if (k>node.key){
		   node.right = findPlace(k,i,node.right);
	   }
	   return node;
	   
   }
  private void treeBalance(WAVLNode newNode){
	  //detect the case!!!
	  WAVLNode nodeParent = newNode.getParent();
	  //CASE-1
	  /*if (nodeParent.getRank()==0){
		  if (nodeParent.getLeft().isExternalLeaf()||nodeParent.getRight().isExternalLeaf()){
			  nodeParent.rankPromote();
			  treeBalance(nodeParent);
		  }
	  }*/
	  if (newNode.getRankDiff()==0){
		  if (nodeParent.getLeft().getRankDiff()==2||nodeParent.getRight().getRankDiff()==2){
			  
		  }
		  else {
			nodeParent.rankPromote();
			treeBalance(nodeParent);  
		  }
		  
	  }
	  //CASE-2
	  
	  
  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	   if (this.size==0){
		   this.root = new WAVLNode(k,i,null);
		   size++;
		   return 0;
	   }
	  //find the place to insert the new item
	   WAVLNode newNode = findPlace(k,i,this.root);
	   if (k>newNode.parent.key){
		   newNode.parent.right=newNode;
	   }
	   else if (k<newNode.parent.key){
		   newNode.parent.left=newNode;
	   }
	   else{
		   return -1;
	   }
	   int cntBalance = 0;
	   //case B - insertion while the parent is not a leaf
	   //no balance needed
	   if (newNode.parent.isInternalNode()){
		   return cntBalance;
	   }
	   //case A - insertion
	   
	   
	   
	   return cntBalance;
   }
   
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   return 42;	// to be replaced by student code
   }

   /**
    * public String min()
    *
    * Returns the of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   

   public String min()
   {
	   return this.min.info;
	   
   }
   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   return this.max.info;
   }
  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   //it's working but i'm not sure it's a good idea to make a public list for the list
   List<Integer> list = new ArrayList<>();
   
   public int[] keysToArray()
   {
	   
	   	 list.clear();
         rec_keysToArray(this.root);
         
         int[] ret = new int[list.size()];
         for (int i=0; i < ret.length; i++)
         {
             ret[i] = list.get(i).intValue();
         }
         return ret;        
         
   }
   
   public void rec_keysToArray(WAVLNode node){
	   if (node == null || node.key==Integer.MAX_VALUE) {
			return;
		}
	   rec_keysToArray(node.left);
	   list.add(node.key);
	   rec_keysToArray(node.right);
	return;
   }
   
   private int in_Order_info(int pos, String[] arr, WAVLNode node){
	   if (!node.isExternalLeaf() && !node.isInternalNode()){
		   
		   int i = in_Order_info(pos,arr,node.getLeft());
		   arr[i]=node.getInfo();
		   return in_Order_info(i+1,arr,node.getRight());
	   }
	   if (node.isExternalLeaf()){
		   return pos;
	   }
	   arr[pos]=node.info;
	   return pos+1;
   }
   

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  String[] arr = new String[this.size];
      in_Order_info(0,arr,this.root);
      return arr;                   
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return this.getSize(); 
   }

  /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This is an example which can be deleted if no such classes are necessary.
   */
  public static class WAVLNode{
		private WAVLNode left;
		private WAVLNode right;
		private WAVLNode parent;
		private int key;
		private String info;
		private int rank;
		/**
		 * Create a new node as an INTERNAL Leaf
		 * @param key
		 * @param info
		 * @param parent
		 */
		
		public WAVLNode(int key,String info, WAVLNode parent) {
			this.left = new WAVLNode(this);
			this.right = new WAVLNode(this);
			this.parent = parent;
			this.key = key;
			this.info = info;
			this.rank=0;
		}
		/**
		 * Create a new EXTERNAL leaf
		 * with a -1 rank
		 * @param parent
		 */
		public WAVLNode(WAVLNode parent) {
			this.left = null;
			this.right = null;
			this.parent = parent;
			this.key = 0;
			this.info = null;
			this.rank = -1;
		}

		public WAVLNode getLeft() {
			return left;
		}

		public void setLeft(WAVLNode left) {
			this.left = left;
		}

		public WAVLNode getRight() {
			return right;
		}

		public void setRight(WAVLNode right) {
			this.right = right;
		}

		public WAVLNode getParent() {
			return parent;
		}

		public void setParent(WAVLNode parent) {
			this.parent = parent;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		public int getRank() {
			return this.rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}
		public int getRankDiff(){
			//case root
			return (this.rank-this.parent.rank);
		}
		public boolean isExternalLeaf(){
			if (this.rank == -1)
				return true;
			return false;
		}
		public boolean isInternalNode(){
			return(!this.right.isExternalLeaf() && !this.left.isExternalLeaf());
		}
		public void rankPromote(){
			this.rank = this.rank+1;
		}
		public void rankDemote(){
			this.rank = this.rank-1;
		}
		
		public void Rotate_Right(){
			  WAVLNode prevParent = this.getParent();
			  WAVLNode prevRight = this.getRight();
			  
			  this.parent = prevParent.getParent();
			  this.right = prevParent;
			  prevParent.left = prevRight;
			  prevParent.parent = this;
			  
			  
			  
		  }
		
  }
  
  //for tests//
  public static void main(String[] args){
	  //System.out.println(Arrays.toString(new int[]{3}));
	  System.out.println("3");
	  
	  //Function tests//
	  WAVLTree bin_tree = new WAVLTree();
	  
	  System.out.println(bin_tree.size);
	  System.out.println(bin_tree.size());
	  System.out.println(bin_tree.empty());
	  //System.out.println(bin_tree.min());
	  //System.out.println(bin_tree.max());
	  System.out.println(bin_tree.search(6));
	  System.out.println(Arrays.toString(bin_tree.keysToArray()));
	  System.out.println(Arrays.toString(bin_tree.infoToArray()));
	  
	  

  }
  

}
  

