

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
	public void setRoot(WAVLNode root){
		this.root = root;
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
	  return 0==this.getSize();
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
  
  
  	public String search(int k){
  		return rec_search(this.root, k);
  		}
  	
  	public WAVLNode searchForNode(int k){
  		if (this.getRoot() == null)
  			return null;
  		WAVLNode node = this.getRoot();
  		while (null!=node){
  			int nodeKey = node.getKey();
  			if (k==nodeKey){
  				return node;
  			} else if(k < nodeKey){
  				if(node.getLeft()==null)
  					return node;
  				node = node.getLeft();
  			} else {
  				if (node.getRight()==null)
  					return node;
  				node = node.getRight();
  			}
  		}
  		return node;
  	}
  
  	public WAVLNode findPlace(int k,String i, WAVLNode node){
  		
  		if (this.getRoot() == null)
  			return null;
  		
  		while (!node.isExternalLeaf()){
  			int nodeKey = node.getKey();
  			if (nodeKey == k){
  				return node;
  			} else if(nodeKey > k){
  				if(node.getLeft().isExternalLeaf())
  					return node;
  				node = node.getLeft();
  			} else if(nodeKey < k){
  				if (node.getRight().isExternalLeaf())
  					return node;
  				node = node.getRight();
  			}
  		}
  		return node;
  		
	   }
   
  public int insertBalance(WAVLNode newNode){
	  if(newNode.getParent()==null)
		  return 0;
	  //detect the case!!!
	  WAVLNode nodeParent = newNode.getParent();
	  
	  if(nodeParent.getRank() != newNode.getRank()) {
          return 0;
      }
	  
	  int BalanceFactor = Math.abs(nodeParent.left.getRankDiff()-nodeParent.right.getRankDiff());
	//Case #1
	  if (BalanceFactor == 1) {
		  nodeParent.rankPromote();
		  return 1+insertBalance(nodeParent); //recursion- go up
	  }
	//Case #2
	  else if (BalanceFactor == 2){
		  int newNodeBalance = newNode.left.getRankDiff()-newNode.right.getRankDiff();
		  
		  if ((newNode == nodeParent.getRight() && newNodeBalance==1) || (newNode == nodeParent.getLeft() && newNodeBalance==-1)){
			  if(newNode == nodeParent.getLeft()){
				  rotateRight(newNode);
			  }
			  else{
				  rotateLeft(newNode);
			  }
			  nodeParent.rankDemote();			  
			  return 1;
		  }
		  //Case #3
		  else{
			  doubleRotate(newNode);
              newNode.rankDemote();
              nodeParent.rankDemote();
              return 2;
          }	  
	  }
	return 0;
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
	   WAVLNode nodeLocation = findPlace(k, i,this.getRoot());
	          if (nodeLocation.getKey() == k) {
    	   return -1;
       }

	   WAVLNode newNode = new WAVLNode(k,i,nodeLocation);
	   
	   
	   //connect the parent to the new node

	   if (k>newNode.parent.key){
		   newNode.parent.right=newNode;
		   
	   }
	   else if (k<newNode.parent.key){
		   newNode.parent.left=newNode;
		   
	   }
	   //update the size
	   size++;
	   
	   if(max == null || k>max.getKey()){
		   this.setMax(newNode);
	   }
	   else if(min == null || k<min.getKey()){
		   this.setMin(newNode);
	   }
	   
	   return insertBalance(newNode);
   }
   
   private void byPassNode(WAVLNode node, WAVLNode newNode){
		  WAVLNode nodeParent = node.getParent();
		  
		  WAVLNode currentRight = newNode.getRight();
		  WAVLNode currentLeft = newNode.getLeft();
		  WAVLNode currentParent = newNode.getParent();
		  
		  // replacing node children to newNode left right pointers
		  if (!node.getRight().isExternalLeaf() && !node.getLeft().isExternalLeaf()){
			  newNode.setRank(node.getRank());
			  if (node.getLeft() != newNode){
				  newNode.setLeft(node.getLeft());
				  node.getLeft().setParent(newNode);
			  }
			  if (node.getRight() != newNode){
				  newNode.setRight(node.getRight());
				  newNode.getRight().setParent(newNode);
			  }
		  
		  //fixing the pointers on the old position of newNode
		  if (node.getRight()!= newNode && node.getLeft()!=newNode){
			  //if newNode is a leaf
			  if (currentLeft.isInternalLeaf() && currentRight.isExternalLeaf()){
				  if (newNode.isLeft()){
					  currentParent.setLeft(currentLeft);
					  currentLeft.setParent(currentParent);					  
				  } else {
					  currentParent.setRight(currentRight);
					  currentRight.setParent(currentParent);
				  }
			  } else if (!currentLeft.isExternalLeaf() && currentRight.isExternalLeaf()){
				  if (newNode.isLeft()){
					  currentParent.setLeft(currentLeft);
					  currentLeft.setParent(currentParent);
				  } else {
					  currentParent.setRight(currentLeft);
					  currentLeft.setParent(currentParent);
				  }
			  } else if (currentLeft.isExternalLeaf() && !currentRight.isExternalLeaf()){
				  if (newNode.isLeft()){
					  currentParent.setLeft(currentRight);
					  currentRight.setParent(currentParent);
				  } else {
					  currentParent.setRight(currentRight);
					  currentRight.setParent(currentParent);
				  	}
			  	}
		  	}
		  
		  }
		  if (node.getParent() != null){
			  newNode.setParent(nodeParent);
			  if (node.isRight()){
				  nodeParent.setRight(newNode);
			  } else {
				  nodeParent.setLeft(newNode);
			  }
		  } else { //if node is the root of the tree
			  this.setRoot(newNode);
			  newNode.setParent(null);
		  }	  

   }
   
   private int deleteBalance(WAVLNode node){
	   if (node == null){
		   return 0;
	   }
	   //check if the node is a leaf 
	   //if have a rank diff = 0 
	   if (node.isInternalLeaf()){
		   if (node.checkDiffs(2, 2)){
			   node.rankDemote();
			   //up the problem
			   return 1+deleteBalance(node.getParent());
		   }
		   else if (node.checkDiffs(1, 1))
			   return 0;
	   }
	   // the node is not a leaf. check the diffs
	   if (node.getRight().getRankDiff()<=2 && node.getLeft().getRankDiff()<=2){
		   //no further actions are needed
		   return 0;
	   }
	   // if we get to here - the node is not a valid WAVL
	   if (node.getLeft().getRankDiff() == 3){
		   
		   if (node.getRight().getRankDiff() == 2){ //demote 
			   node.rankDemote();
			   return 1 + deleteBalance(node.getParent());
		   }
		   if (node.getRight().getRankDiff() == 1){
			   WAVLNode nodeRight = node.getRight();
			   
			   // (2,2) node
			   if (nodeRight.getLeft().getRankDiff() == 2 && nodeRight.getRight().getRankDiff() == 1){
				   nodeRight.rankDemote();
				   node.rankDemote();
				   return 2+deleteBalance(node.getParent());
			   }
			   //rotation left case
			   if (nodeRight.getRight().getRankDiff() == 1){
				   rotateLeft(nodeRight);
				   nodeRight.rankPromote();
				   node.rankDemote();
				   if (node.isInternalLeaf()){
					   node.rankDemote();
					   return 2;
				   }
				   else {
					   return 1;
				   }
			   }
			   
			   //case double rotate - right & left rotation
			   if (nodeRight.getRight().getRankDiff() ==2){
				   WAVLNode nodeRightLeft = nodeRight.getLeft();
				   
				   //roations
				   rotateRight(nodeRightLeft);
				   rotateLeft(nodeRightLeft);
				   // rank update
				   nodeRightLeft.rankPromote();
				   nodeRightLeft.rankPromote();
				   nodeRight.rankDemote();
				   node.rankPromote();
				   node.rankDemote();
				   
				   return 2;
			   }
			   
		   }
		   
	   } else {
		   //the rank difference of the right side is 3
		   
		   //case demote
		   if (node.getLeft().getRankDiff() == 2){
			   node.rankDemote();
			   return 1+deleteBalance(node.getParent());
		   }
		   
		   if (node.getLeft().getRankDiff() == 1){
			   WAVLNode nodeLeft = node.getLeft();
			   
			   //double demote
			   if (nodeLeft.getLeft().getRankDiff() == 2 && nodeLeft.getRight().getRankDiff() == 2){
				   nodeLeft.rankDemote();
				   node.rankDemote();
				   return 2+deleteBalance(node.getParent());
			   }
			   //right rotation
			   if (nodeLeft.getLeft().getRankDiff() == 1 ){
				   rotateRight(nodeLeft);
				   nodeLeft.rankPromote();
				   node.rankDemote();
				   if (node.isInternalLeaf()){
					   node.rankDemote();
					   return 2;
				   }
				   else{
					   return 1;
				   }
			   }
			   // double rotate -right & left rotations
			   if (nodeLeft.getLeft().getRankDiff() == 2){
				  WAVLNode nodeLeftRight = nodeLeft.getRight();
				  
				  rotateLeft(nodeLeftRight);
				  rotateRight(nodeLeftRight);
				  
				  //update ranks
				  nodeLeftRight.rankPromote();
				  nodeLeftRight.rankPromote();
				  nodeLeft.rankDemote();
				  node.rankDemote();
				  node.rankDemote();
				  
				  return 2;
			   }
				   
		   }
	   }
	//problem if we get to here
	return 0;
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
	   WAVLNode nodeToDelete = searchForNode(k);
	   
	   WAVLNode nodeToBalance = null;
	   
	   int key = nodeToDelete.getKey();
	   
	   
	   //if the key does not exist
	   if (k != key) //hceck this
		   return -1;
	   //maintain the min max
	   if (nodeToDelete == this.getMin()){
		   this.setMin(this.findSuccessor(nodeToDelete));
	   }
	   if (nodeToDelete == this.getMax()){
		   this.setMax(this.findPredecessor(nodeToDelete));
	   }
	   
	   WAVLNode nodeParent = nodeToDelete.getParent();
	   WAVLNode nodeRight = nodeToDelete.getRight();
	   WAVLNode nodeLeft = nodeToDelete.getLeft();
	   //if nodeToDelete is a root or the only key in the tree
	   if (nodeToDelete.getRight().isExternalLeaf() && nodeToDelete.left.isExternalLeaf()){
		   if (nodeParent == null){
			   this.setRoot(null);
			   this.setMax(null);
			   this.setMin(null);
			   //this.size = this.size - 1;
		   } else if (nodeToDelete.isRight()){
			   nodeParent.setRight(new WAVLNode(nodeParent));   
		   } else if (nodeToDelete.isLeft()){
			   nodeParent.setLeft(new WAVLNode(nodeParent));
		   }
		   nodeToBalance = nodeParent;
	   } else { 
		   //check if have only left child
		   // if have only child - bypass him
		   if (nodeRight.isExternalLeaf()){
			   nodeToBalance = nodeToDelete.getParent();
			   //replace the node with his left child
			   byPassNode(nodeToDelete, nodeToDelete.getLeft());
		   }
		   // if the node have two children
		   // or only right child
		   else {
			   if (nodeLeft.isExternalLeaf()){
				   nodeToBalance = nodeToDelete.getParent();
				   //replace the node with his right child
				   byPassNode(nodeToDelete, nodeToDelete.getRight());
			   } else {
				   WAVLNode nodeSuccessor = this.findSuccessor(nodeToDelete);
				   nodeToBalance = nodeSuccessor.getParent();
				   if (nodeToBalance == nodeToDelete){
					   nodeToBalance = nodeSuccessor;
				   }
				   byPassNode(nodeToDelete, nodeSuccessor);
			   }
		   }
	   }
	   
	   
	   this.size = this.size()-1;
	   //balancing the tree: 
	   return deleteBalance(nodeToBalance);
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
	   return this.min.getInfo();
	   
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
	   return this.max.getInfo();
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
    * public void rotateRight
    * 
    * Rotate right from the given node (this node will go up)
    * @param node
    */
   public void rotateRight(WAVLNode node){
		
		  WAVLNode prevParent = node.getParent();
		  if (prevParent == null){
			  return;
		  }
		  
		  WAVLNode prevRight = node.getRight();
		  WAVLNode prevGrandparent = prevParent.getParent();
		  //if the current father is the root
		  if (prevParent==this.getRoot()){
			  this.setRoot(node);
		  }
		  if (prevGrandparent!=null){
			  if(prevParent == prevGrandparent.getRight()){
				  prevGrandparent.setRight(node);
			  }else {
				  prevGrandparent.setLeft(node);
			  }
		  }
		  node.parent = prevGrandparent;
		  node.right = prevParent;
		  prevParent.left = prevRight;
		  prevParent.parent = node;
		  if(prevRight != null){
			  prevRight.parent=prevParent;
		  }
		  
	}
   
   public void rotateLeft(WAVLNode node){
		
	   	WAVLNode prevParent = node.getParent();
		  if (prevParent == null){
			  return;
		  }
		WAVLNode prevLeft = node.getLeft();
		WAVLNode prevGrandparent = prevParent.getParent();
		if (prevParent==this.getRoot()){
			this.setRoot(node);
		}
		if (prevGrandparent!=null){
			if(prevParent == prevGrandparent.getRight()){
				prevGrandparent.setRight(node);
			} else{
				prevGrandparent.setLeft(node);
			}
		}
		node.parent = prevGrandparent;
		node.left = prevParent;
		prevParent.right = prevLeft;
		prevParent.parent = node;
		
		if (prevLeft !=null){
			prevLeft.parent = prevParent;
		}
	}
   public void doubleRotate(WAVLNode node){
	   if(node.getParent().getLeft()==node){
		   //Rotate left and rotate right
		   WAVLNode rightNode = node.getRight();
		   rotateLeft(rightNode);
		   rotateRight(rightNode);
		   rightNode.rankPromote();
		   
	   }
	   else{
		   WAVLNode leftNode = node.getLeft();
		   //Rotate right and then rotate left
		   rotateRight(leftNode);
		   rotateLeft(leftNode);
		   leftNode.rankPromote();
	   }
   }
   /*
    * returns the node which is the predecessor of the input "node"
    * The predecessor - the node with the maximum key that is less than node's key
    * Main use: to find the node with the maximun key in the tree, in case the current maximum have
    * been deleted
    */
   private WAVLNode findPredecessor(WAVLNode node){
	   if(node == null){
		   return null;
	   }
	   WAVLNode temp = node;
	   if (node.getLeft()!=null){
		   temp = temp.getLeft();
		   while(temp.getRight()!=null){
			   temp = temp.getRight();
		   }
		   return temp;
	   }
	   //if the left is null
	   if (node.getParent()==null)
		   return null;
	   //go up until the next left right
	   if (node == node.getParent().getLeft()){
		   return node.getParent();
	   }
	   while (temp == temp.getParent().getLeft()){
		   temp = temp.getParent();
	   }
	   if (temp.getParent() != null)
		   return temp.getParent();
	   return null;
	   
   }
   /*
    * returns the node which is the successor of the input "node"
    * 
    * The successor - the node with the minimum key which is
    * higher than the node's key.
    * Main use: to find the node with the minimum key in the tree after the current minimum have been
    * deleted.
    */
   private WAVLNode findSuccessor(WAVLNode node){
	   if(node == null){
		   return null;
	   }
	   WAVLNode temp = node;
	   if(node.getRight()!= null){
		   temp = node.getRight();
		   while (temp.getLeft()!=null){
			   temp = temp.getLeft();
		   }
		   return temp;
	   }
	   //what if the right is null?
	   //the successor will be the lowest ancestor
	   //of the node, which the node is in its left subtree
	   if (node.getParent()==null) //no parents->no successor
		   return null;
	   //go up until the next turn right
	   if (node == node.getParent().getLeft()){
		   return node.getParent();
	   }
	   while (temp == temp.getParent().getRight()){
		   temp = temp.getParent();
	   }
	   if (temp.getParent() != null)
		   return temp.getParent();
	   
	   return null;
	   
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
		private WAVLNode left; // The left child
		private WAVLNode right; // The right child
		private WAVLNode parent; // The father/parent
		private int key; // The key of the node 
		private String info; // the info of the node
		private int rank; // The rank of the node
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
		/**
		 * return the rank different between the
		 * current node and hs parent
		 * @return
		 */
		public int getRankDiff(){
			//case root
			return (this.parent.rank-this.rank);
		}
		public boolean isExternalLeaf(){
			if (this.rank == -1)
				return true;
			return false;
		}
		/**
		 * check if the node have at least one
		 * external leaf as his child
		 * @return
		 */
		public boolean isInternalNode(){
			return(!this.right.isExternalLeaf() && !this.left.isExternalLeaf());
		}
		/**
		 * 
		 * @return
		 */
		public boolean isInternalLeaf(){
			return(this.getRight().isExternalLeaf() && this.getLeft().isExternalLeaf());
		}
		public boolean isRight(){
			if(this.getParent()==null)
				return false;
			return (this == this.getParent().getRight());
		}
		/**
		 * public boolean isLeft()
		 *
		 * @return true of the node is the left child of its parent
		 */
		public boolean isLeft(){
			if(this.getParent()==null)
				return false;
			return (this == this.getParent().getLeft());
		}
		/**
		 * 
		 */
		public void rankPromote(){
			this.rank = this.rank + 1;
		}
		/**
		 * 
		 */
		public void rankDemote(){
			this.rank = this.rank - 1;
		}
		/*
		 * the function returns TRUE if there is a diff with value num 
		 * between the node and one of his childs, FALSE otherwise
		 */
		public boolean childDiffs(int num){
			if (this.getLeft().getRankDiff()==num||this.getRight().getRankDiff()==num)
				return true;
			return false;
		}
		public boolean checkDiffs(int num1, int num2){
			if(this.getLeft().getRankDiff()==num1 && this.getRight().getRankDiff()==num2)
				return true;
			return false;
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
	  System.out.println(tree.delete(5));
	  System.out.println(tree.size);
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
  

