

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
	this.max=null;
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
  
  
  
   private WAVLNode findPlace(int k,String i, WAVLNode node){
/*	   if (node.isExternalLeaf()){
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
		   */
	   
	   while (!node.isExternalLeaf()){
		   if (node.key==k)
			   return null;
		   else if(k<node.key){
			   node = node.left;
		   }
		   else if(k>node.key){
			   node = node.right;
		   }  
	   }
	   
	   return new WAVLNode(k,i,node.getParent()); 
	   
	   
   }
  public int treeBalance(WAVLNode newNode, int cnt){
	  cnt++;
	  //detect the case!!!
	  WAVLNode nodeParent = newNode.getParent();
	  if (newNode.getRankDiff()==0){
		  // CASE #1
		  if (nodeParent.childDiffs(1)){
			  nodeParent.rankPromote();
			  return cnt+treeBalance(nodeParent,cnt);
		  }
		  
		  if ((nodeParent.childDiffs(2))&&(newNode.getRight().getRankDiff()==2)&&(newNode.getLeft().getRankDiff()==1)){
			  
			  //CASE #2
			  if((newNode.getRight().isInternalNode())){
				  newNode.rotateRight();
				  nodeParent.rankDemote();
				  return 1; //completed
			  }
			  //CASE #3
			  else{
				  WAVLNode tempRight = newNode.getRight();
				  newNode.rotateRight();
				  newNode.rotateRight();
				  newNode.rankDemote();
				  nodeParent.rankDemote();
				  tempRight.rankPromote();
				  return 1; //completed  
			  }
		  }
	  }
	  return cnt; //check this
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
		   max = root;
		   min = root;
		   return 0;
	   }
	  //find the place to insert the new item
	   WAVLNode newNode = findPlace(k,i,this.root);
	   //node with the same key is already in the tree
	   if (newNode==null){
		   return -1;
	   }
	   //connect the parent to the new node

	   if (k>newNode.parent.key){
		   newNode.parent.right=newNode;
		   size++;
	   }
	   else if (k<newNode.parent.key){
		   newNode.parent.left=newNode;
		   size++;
	   }

	   int cntBalance = 0;
	   //case B - insertion while the parent is not a leaf
	   //no balance needed
	   if (newNode.parent.isInternalNode()){
		   return cntBalance;
	   }
	   //case A - insertion
	   
	   
	   
	   if(max == null || k>max.key){
		   max = newNode;
	   }
	   else if(min == null || k<min.key){
		   min = newNode;
	   }
	   
	   return treeBalance(newNode,0);
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
	   if (this.min==null){
		   return null;
	   }
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
	   if (this.max==null){
		   return null;
	   }
	   return this.max.info;
   }
  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   //it's working but i'm not sure it's a good idea to make a public list for the list
   List<Integer> keys_list = new ArrayList<>();
   
   public int[] keysToArray()
   {
	   
	     keys_list.clear();
         rec_keysToArray(this.root);
         
         int[] ret = new int[keys_list.size()];
         for (int i=0; i < ret.length; i++)
         {
             ret[i] = keys_list.get(i).intValue();
         }
         return ret;        
         
   }
   
   public void rec_keysToArray(WAVLNode node){
	   if (node == null || node.isExternalLeaf()) {
			return;
		}
	   rec_keysToArray(node.left);
	   keys_list.add(node.key);
	   rec_keysToArray(node.right);
	return;
   }
   

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
   
  List<String> info_list = new ArrayList<>();
  public String[] infoToArray()
  {
	   
	  	info_list.clear();
	  	rec_infoToArray(this.root);
        
        String[] ret = new String[info_list.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = info_list.get(i);
        }
        return ret;        
                         
  }
  
  
  
  public void rec_infoToArray(WAVLNode node){
	   if (node == null || node.isExternalLeaf()) {
			return;
		}
	   rec_infoToArray(node.left);
	   info_list.add(node.info);
	   rec_infoToArray(node.right);
	return;
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
		public boolean childDiffs(int num){
			if (this.getLeft().getRankDiff()==num||this.getRight().getRankDiff()==num)
				return true;
			return false;
		}
		
		public WAVLNode rotateRight(){
			
			WAVLNode leftNode = this.left;
			this.left = leftNode.right;
			leftNode.right = this;
			  
			return leftNode;
			  
		}
		public WAVLNode rotateLeft(){
			  
			WAVLNode rightNode = this.right;
			this.right = rightNode.left;
			rightNode.left = this;
			
			return rightNode;
		}
		
		public WAVLNode doubleRotateLeftRight () {
			this.left = this.left.rotateLeft();
			WAVLNode Node = this.rotateRight();
			return Node;
			}
		
		public WAVLNode doubleRotateRightLeft () {
			this.right = this.right.rotateRight();
			WAVLNode Node = this.rotateLeft();
			return Node;
			}
		
  }
  
  //for tests//
  public static void main(String[] args){
	  //System.out.println(Arrays.toString(new int[]{3}));
	  //System.out.println("3");
	  
	  //Function tests//

	  WAVLTree tree = new WAVLTree();
	  System.out.println(tree.insert(2, "ggsdf"));
	  System.out.println(tree.insert(5, "fkgjlgd"));
	  System.out.println(tree.insert(5, "czxxzc"));
	  //System.out.println(tree.size);
	  System.out.println(tree.size());
	  System.out.println(tree.empty());
	  System.out.println(tree.min());
	  System.out.println(tree.max());
	  System.out.println(tree.search(5));
	  System.out.println(tree.search(2));
	  System.out.println(Arrays.toString(tree.keysToArray()));
	  System.out.println(Arrays.toString(tree.infoToArray()));
	  
	  
/*	  System.out.println(bin_tree.size);
	  System.out.println(bin_tree.size());
	  System.out.println(bin_tree.empty());
	  System.out.println(bin_tree.min());
	  System.out.println(bin_tree.max());
	  System.out.println(bin_tree.search(5));
	  System.out.println(bin_tree.search(2));
	  System.out.println(Arrays.toString(bin_tree.keysToArray()));
	  System.out.println(Arrays.toString(bin_tree.infoToArray()));
	  
	  //Case #1 test:
	  bin_tree.root = new WAVLNode(3, "Haim",null);
	  bin_tree.root.left = new WAVLNode(2, "maya", bin_tree.root);
	  bin_tree.root.right = new WAVLNode(6, "ron", bin_tree.root);
	  
*/
  }
  

}
  

