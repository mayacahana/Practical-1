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
	
	private WAVLNode root;//the root of the tree
	private WAVLNode min;//the minimum of the tree, updated every operation in order to get O(1) complexity
	private WAVLNode max;//the maximum of the tree, updated every operation in order to get O(1) complexity
	private int size; //update every delete\insert operation
	
	//Getters & Setters for the elements of the tree
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
  public boolean empty() {
	  return 0==this.getSize();
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  //implemented recursively
  	public String rec_search(WAVLNode node, int k){
  		//recursion base case
	  if (node == null){
			return null;
		}
	  //if we found the node
		else if (node.key == k) {
			return node.info;
		}
	//continue with the recursion on the right subtree
		else if (k > node.key) {
			return rec_search(node.right, k);
		}
	//continue with the recursion on the left subtree
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
  				if(node.getLeft()==null || node.getLeft().isExternalLeaf())
  					return node;
  				node = node.getLeft();
  			} else {
  				if (node.getRight()==null || node.getRight().isExternalLeaf())
  					return node;
  				node = node.getRight();
  			}
  		}
  		return node;
  	}
  	
  	 /**
     * public WAVLNode findPlace(int k,String i, WAVLNode node)
     *
     * locate and return the node we need to update
     */
  	public WAVLNode findPlace(int k,String i, WAVLNode node){
  	//if we get null tree
  		if (this.getRoot() == null)
  			return null;
  		//otherwise we get into the while loop until we find the node 
  		while (!node.isExternalLeaf()){
  			int nodeKey = node.getKey();
  			//if there is the same key we found it and return the node
  			if (nodeKey == k){
  				return node;
  	  	//if the key we are looking is smaller than the current node key we continue on the left subtree
  			} else if(nodeKey > k){
  				if(node.getLeft().isExternalLeaf())
  					return node;
  				node = node.getLeft();
  	  	//if the key we are looking is bigger than the current node key we continue on the right subtree
  			} else if(nodeKey < k){
  				if (node.getRight().isExternalLeaf())
  					return node;
  				node = node.getRight();
  			}
  		}
  		return node;
  		
	   }
   
  public int insertBalance(WAVLNode newNode){
	  //if the parent of the node is null than we return 0
	  if(newNode.getParent()==null)
		  return 0;
	  //detect the case (cases from the lecture presentation)
	  WAVLNode nodeParent = newNode.getParent();
	  
	  if(nodeParent.getRank() != newNode.getRank()) {
          return 0;
      }
	  
	  //the difference between the left and the right child rank  
	  int BalanceFactor = Math.abs(nodeParent.left.getRankDiff()-nodeParent.right.getRankDiff());
	//Case #1
	  if (BalanceFactor == 1) {
		  nodeParent.rankPromote();
		  return 1+insertBalance(nodeParent); //recursion- go up
	  }
	//Case #2
	  else if (BalanceFactor == 2){
		  //check the difference between the left and the right child rank of the newNode 

		  int newNodeBalance = newNode.left.getRankDiff()-newNode.right.getRankDiff();
		  
		  //do one rotation
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
		//do double rotation
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
	   //if the tree is empty than create new wavlNode as the root of the tree with the key and info
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
	          
	   // Create new node with the key and info
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
	   
	   //update the max if needed
	   if(max == null || k>max.getKey()){
		   this.setMax(newNode);
	   }
	   //update the min if needed
	   else if(min == null || k<min.getKey()){
		   this.setMin(newNode);
	   }
	   
	   //return the function insertBalance which will eventually return the number of balance actions
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
			  if ((currentLeft==null || currentLeft.isInternalLeaf()) && (currentRight==null || currentRight.isExternalLeaf())){
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
	   // if we get to here - the node is not a proper
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
				   
				   //Rotations
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
	   if (k != key) //Check this
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
		   //check if there is only left child
		   // if there is only child - bypass him
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
   // every tree has min value which being updated every operation of insert 
   // and delete and that will make it O(1) complexity
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
   // every tree has max value which being updated every operation of insert 
   // and delete and that will make it O(1) complexity
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
    //implemented with recursion
    public int[] keysToArray()
    {
 	     int[] keys_array = new int[this.getSize()]; 
          rec_keysToArray(this.root,keys_array, 0);
          
          return keys_array;        
          
    }
    
    public int rec_keysToArray(WAVLNode node, int[] keys_array, int array_index){
  		//recursion base case
 	   if (node == null || node.isExternalLeaf()) {
 			return array_index;
 		}
 	   //call the same function with the left subtree to add the key to the array in the expected order
 	   array_index = rec_keysToArray(node.left, keys_array, array_index);
 	   //add the key to the array
 	   keys_array[array_index] = node.getKey();
 	   array_index = array_index+1;
 	   //call the same function with the right subtree to add the key to the array in the expected order
 	   array_index = rec_keysToArray(node.right, keys_array, array_index);
 	   
 	return array_index;
    }
    

   /**
    * public String[] infoToArray()
    *
    * Returns an array which contains all info in the tree,
    * sorted by their respective keys,
    * or an empty array if the tree is empty.
    */
    //implemented with recursion
   public String[] infoToArray()
   {
 	   
 	  String[] info_array = new String[this.getSize()]; 
       rec_infoToArray(this.root,info_array, 0);
          
       return info_array;      
                          
   }
   
   
   
   public int rec_infoToArray(WAVLNode node, String[] info_array, int array_index){
 		//recursion base case
 	   if (node == null || node.isExternalLeaf()) {
 			return array_index;
 		}
 	   //call the same function with the left subtree to add the info to the array in the expected order
 	   array_index = rec_infoToArray(node.left, info_array, array_index);
 	   //add the info to the array
 	   info_array[array_index] = node.getInfo();
 	   array_index = array_index+1;
 	   //call the same function with the right subtree to add the info to the array in the expected order
 	   array_index = rec_infoToArray(node.right, info_array, array_index);
 	   
 	return array_index;
   }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   // every tree has size value which being updated every operation of insert 
   // and delete and that will make it O(1) complexity
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
		  //return if the there is no parent
		  if (prevParent == null){
			  return;
		  }
		  
		  WAVLNode prevRight = node.getRight();
		  WAVLNode prevGrandparent = prevParent.getParent();
		  
		  //if the current parent is the root
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
		  //the operations as been explained in the lecture for rotation
		  node.parent = prevGrandparent;
		  node.right = prevParent;
		  prevParent.left = prevRight;
		  prevParent.parent = node;
		  if(prevRight != null){
			  prevRight.parent=prevParent;
		  }
		  
	}
   
   public void rotateLeft(WAVLNode node){
		  //return if the there is no parent
	   	WAVLNode prevParent = node.getParent();
		  if (prevParent == null){
			  return;
		  }
		WAVLNode prevLeft = node.getLeft();
		WAVLNode prevGrandparent = prevParent.getParent();
		  //if the current parent is the root
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
		  //the operations as been explained in the lecture for rotation
		node.parent = prevGrandparent;
		node.left = prevParent;
		prevParent.right = prevLeft;
		prevParent.parent = node;
		
		if (prevLeft !=null){
			prevLeft.parent = prevParent;
		}
	}
   //double rotations as been explained in the lecture using the right and left single rotations
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
    * The predecessor - the node with the maximum key that is less than the key of the node;
    * Main use: to find the node with the maximum key in the tree, in case the current maximum have
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
    * higher than the key of the node;.
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
		private WAVLNode parent; // The parent
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

		//Getters and Setters for WAVLNode
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
		 * current node and his parent
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
		 * check if the node has at least one
		 * external leaf as his child
		 * @return
		 */
		public boolean isInternalNode(){
			return(!this.right.isExternalLeaf() && !this.left.isExternalLeaf());
		}
		
		/**
		 * check if the node has two externalLeaf children
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
		 * @return true if the node is the left child of its parent
		 */
		public boolean isLeft(){
			if(this.getParent()==null)
				return false;
			return (this == this.getParent().getLeft());
		}
		
		/**
		 * Promote the rank of the node by 1
		 */
		public void rankPromote(){
			this.rank = this.rank + 1;
		}
		/**
		 * Demote the rank of the node by 1
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
		
		/*
		 * the function returns TRUE if there is a diff with value num1 
		 * for the left child and num2 for the right child FALSE otherwise
		 */
		public boolean checkDiffs(int num1, int num2){
			if(this.getLeft().getRankDiff()==num1 && this.getRight().getRankDiff()==num2)
				return true;
			return false;
		}
		
		
  }  

}