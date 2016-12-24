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
		return this.min;
	}
	public WAVLNode getMax() {
		return this.max;
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
   
   /**
    * deleteBalance - check if the tree is balanced 
    * 
    * @param node
    * @return
    */
   
  
   private int deleteBalance(WAVLNode node){
	   
	   int balanceCnt = 0;
	   boolean isWAVL = false;
	   WAVLNode brother;
	   boolean leftCase =node.isLeft();
	   if (node.getParent() == null){
		   return balanceCnt;
	   }
	   if (leftCase == true){
		   brother=node.getParent().getRight();
	   } else {
		   brother=node.getParent().getLeft();
	   }
	   
	 //terminal cases
	   if (node.getRankDiff() == 2){
		   if (node.isExternalLeaf()){
			   if (brother.getRankDiff() == 1)
				   isWAVL = true;
		   } else {
			   if (brother.getRankDiff() ==1 || brother.getRankDiff() ==2)
				   isWAVL = true;
		   }
	   }
	   
	   //if we reach the root we can stop,
	   //or when the problem is over
	   while ((node!=this.getRoot()) && isWAVL == false){
		   leftCase = node.isLeft();
		   WAVLNode nodeParent = node.getParent();
		   WAVLNode brotherChild1 =null, brotherChild2 = null;
		   // check if the node is the left child of his parent or not
		   if (leftCase == true){
			   brother=node.getParent().getRight();
			   brotherChild1 = brother.getRight();
			   brotherChild2 = brother.getLeft();
		   } else {
			   brother=node.getParent().getLeft();
			   brotherChild1 = brother.getLeft();
			   brotherChild2 = brother.getRight();
		   }
		   // a flag to check whether the difference between the brother 
		   // and the parent is 2 (or 1) on order to recogize the case
		   int check2 = Math.abs(nodeParent.getRank() - brother.getRank());
		   boolean caseTwo;
		   if (check2 == 2){
			   caseTwo = true;
		   } else {
			   caseTwo = false;
		   }
		   if (caseTwo){
			 //demote
			   nodeParent.rankDemote();
			   balanceCnt++;
			   //up
			   node = node.getParent();
			   if (node == this.getRoot() || (Math.abs(node.getParent().getRank() - node.getRank()) == 2)){
				   isWAVL = true;
			   }

		   } else { //brother diff is 1
			   // brother is (2,2) node
			   // double demote
			   if (brother.checkDiffs(2, 2)){
				   node.getParent().rankDemote();
				   brother.rankDemote();
				   balanceCnt += 2;
				   
				   //up
				   node = node.parent;
				   if (node == this.getRoot() || (Math.abs(node.getParent().getRank() - node.getRank()) == 2)){
					   isWAVL = true;
				   }
			   } else { //brother rank diff is 1 and brother is not (2,2)
				   
				   if (brotherChild1.getRankDiff()==2){
					 //double rotation case
					   if (leftCase){
						   WAVLNode brotherLeft = brother.getLeft();
						   brotherLeft.rankPromote();
						   brotherLeft.rankPromote();
						   rotateRight(brotherLeft);
						   rotateLeft(brotherLeft);
						   
					   } else {
						   WAVLNode brotherRight = brother.getRight();
						   brotherRight.rankPromote();
						   brotherRight.rankPromote();
						   rotateLeft(brotherRight);
						   rotateRight(brotherRight);
					   }
					   nodeParent.rankDemote();
					   nodeParent.rankDemote();
					   brother.rankDemote();
					   balanceCnt += 2;
					   //end
					   isWAVL=true;
				   } else {
					   //rotate
					   //rotating according to the case
					   
					   if (leftCase){
						   brother.rankPromote();
						   nodeParent.rankDemote();
						   rotateLeft(brother);
						   
					   } else {
						   brother.rankPromote();
						   nodeParent.rankDemote();
						   rotateRight(brother);
						   
					   }
					   
					   if (nodeParent.checkDiffs(2, 2)){
						   nodeParent.rankDemote();
					   }
					   balanceCnt++;
					   //end
					   isWAVL = true;
				   }
				   
			   } 
		   }
	   }
	return balanceCnt;
   }
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   
   public int delete(int k){
	   if (this.empty()){
		   return -1;
	   }
	   WAVLNode node = this.getRoot();
	   
	   //search for the node to delete
	   while(node.getKey()!= k && !node.isExternalLeaf()){
		   node = node.getKey() > k ? node.getLeft() : node.getRight();
	   }
	   if (node.isExternalLeaf()){
		   return -1;
	   }
	   WAVLNode nodeToDelete = null;
	   WAVLNode successor = null;
	   //if the node we found has a NULL child, we will delete it
	   // if not, we will delete its successor
	   if (node.getLeft().isExternalLeaf()||node.getRight().isExternalLeaf()){
		   nodeToDelete = node;
	   } else {
		   successor = this.findSuccessor(node);
		   nodeToDelete = successor;
		 
	   }
	   //in case we have a pointer to the successor
	   boolean deletedMin = (this.min == nodeToDelete);
	   boolean deletedMax = (this.max == nodeToDelete);
	   if (successor == this.max)
		   deletedMax=true;
	   
	   WAVLNode nodeChild;
	   if (nodeToDelete.getLeft().isExternalLeaf()){
		   nodeChild = nodeToDelete.getRight();
	   } else {
		   nodeChild = nodeToDelete.getLeft();
	   }
	   
	   // in case nodeToDelete is the root
	   if (nodeToDelete == this.getRoot()){
		   if (nodeToDelete.getLeft().isExternalLeaf() && nodeToDelete.getRight().isExternalLeaf()){
			   this.size = this.size - 1;
			   this.setRoot(null);
			   this.setMax(null);
			   this.setMin(null);
			   return 0;
		   } else {
			   nodeChild.setParent(null);
			   this.setRoot(nodeChild);
			   
		   }
		   
	   } else {
		   nodeChild.setParent(nodeToDelete.getParent());
		   if (nodeToDelete.isLeft()){
			   nodeToDelete.getParent().setLeft(nodeChild);
		   } else {
			   nodeToDelete.getParent().setRight(nodeChild);
		   }
	   }
	   
	   //in case we deleted the successor
	   if(nodeToDelete != node){
		   node.setKey(nodeToDelete.getKey());
		   node.setInfo(nodeToDelete.getInfo());
		   //deletedMax = true;
	   }
	   //update the max & min if necessary
	   if (deletedMin){
		   if (this.getRoot()==null){
			   this.setMin(null);
		   } else{
			   this.setMin(this.findMin(this.getRoot()));
		   }
		   
	   }
	   if (deletedMax || successor==this.max){
		   if (this.getRoot() == null){
			   this.setMax(null);
		   } else {
			   this.setMax(this.findMax(this.getRoot()));
		   }
	   }
	   this.size = this.size -1;
	   return (deleteBalance(nodeChild));
	   
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
    public int[] keysToArray() {
    	int[] keysArray = new int[this.getSize()]; 
    	if (this.getSize()>0)
    		recKeysToArray(this.root,keysArray, 0);
    	    	
    	return keysArray;          
    }
    
    public int recKeysToArray(WAVLNode node, int[] keysArray, int arrayIndex){
  		//recursion base case
 	   if (node == null || node.isExternalLeaf()) {
 			return arrayIndex;
 		}
 	   //call the same function with the left subtree to add the key to the array in the expected order
 	   arrayIndex = recKeysToArray(node.left, keysArray, arrayIndex);
 	   //add the key to the array
 	   keysArray[arrayIndex] = node.getKey();
 	   arrayIndex = arrayIndex+1;
 	   //call the same function with the right subtree to add the key to the array in the expected order
 	   arrayIndex = recKeysToArray(node.right, keysArray, arrayIndex);
 	   
 	return arrayIndex;
    }
    

   /**
    * public String[] infoToArray()
    *
    * Returns an array which contains all info in the tree,
    * sorted by their respective keys,
    * or an empty array if the tree is empty.
    */
    //implemented with recursion
   public String[] infoToArray() {
	   String[] infoArray = new String[this.getSize()]; 
	   if (this.getSize() > 0)
		   recInfoToArray(this.root,infoArray, 0);
          
       return infoArray;      
                          
   }
   
   
   
   public int recInfoToArray(WAVLNode node, String[] infoArray, int arrayIndex){
 		//recursion base case
 	   if (node == null || node.isExternalLeaf()) {
 			return arrayIndex;
 		}
 	   //call the same function with the left subtree to add the info to the array in the expected order
 	   arrayIndex = recInfoToArray(node.left, infoArray, arrayIndex);
 	   //add the info to the array
 	   infoArray[arrayIndex] = node.getInfo();
 	   arrayIndex = arrayIndex+1;
 	   //call the same function with the right subtree to add the info to the array in the expected order
 	   arrayIndex = recInfoToArray(node.right, infoArray, arrayIndex);
 	   
 	return arrayIndex;
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
   private void leftRotate2 (WAVLNode rotationNode){
	   WAVLNode node = rotationNode.getParent();
	   
	   // changing the pointers
	   // the node & rotation node
	   if (node.isLeft()){
		   setLeftChild(node.getParent(), rotationNode);
	   } else {
		   setRightChild(node.getParent(), rotationNode);
	   }
	   setRightChild(node, rotationNode.getLeft());
	   setLeftChild(rotationNode,node);
	   
   }
   
   private void rightRotate2 (WAVLNode rotationNode){
	   WAVLNode node = rotationNode.getParent();
	   
	   if (node.isLeft()){
		   setLeftChild(node.getParent(), rotationNode);
	   } else {
		   setRightChild(node.getParent(), rotationNode);
	   }
	   setLeftChild(node, rotationNode.getRight());
	   setRightChild(rotationNode, node);
   }
   private void setLeftChild(WAVLNode parent, WAVLNode child){
	   parent.setLeft(child);
	   child.setParent(parent);
   }
   private void setRightChild(WAVLNode parent, WAVLNode child){
	   parent.setRight(child);
	   child.setParent(parent);
   }
   
   private void doubleRotateRight(WAVLNode node){
	   leftRotate2(node);
	   rightRotate2(node);
   }
   private void doubleRotateLeft(WAVLNode node){
	   rightRotate2(node);
	   leftRotate2(node);
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
   
   
   private WAVLNode findMax(WAVLNode node){
	   WAVLNode temp = node;
	   while (!temp.getRight().isExternalLeaf()){
		   temp = temp.getRight();
	   }
	   return temp;
   }
   /*
    * returns the node which is the predecessor of the input "node"
    * The predecessor - the node with the maximum key that is less than the key of the node;
    * Main use: to find the node with the maximum key in the tree, in case the current maximum have
    * been deleted
    */
   private WAVLNode findPredecessor(WAVLNode node){
	   if (node.getLeft() != null)
		   return findMax(node.getLeft());
	   
	   WAVLNode parent = node.getParent();
	   while (parent != null && node.isLeft()){
		   node = parent;
		   parent = parent.getParent();
	   }
	   return parent;
	   
   }
   
   private WAVLNode findMin(WAVLNode node){
	   WAVLNode temp = node;
	   while (!temp.getLeft().isExternalLeaf()){
		   temp = temp.getLeft();
	   }
	   return temp;
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
	  if (node.getRight() != null)
		  return findMin(node.getRight());
	  
	  WAVLNode parent = node.getParent();
	  while (parent != null && node.isRight()){
		  node = parent;
		  parent = parent.getParent();
	  }
	  return parent;
	   
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
			this.key = Integer.MIN_VALUE;
			this.info = "EXT";
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