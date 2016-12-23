import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class TesterWAVLTree {
	
	WAVLTree generatedTree;
	public ArrayList<Integer> keysInTree = new ArrayList<>();
	
	public void treeGenerator(int size, int IntsLimit){
		Random random = new Random();
		generatedTree = new WAVLTree();
		int randomNumber;
		System.out.println("Start inserting elements!");
		for(int i=0;i<size;i++){
			randomNumber=random.nextInt(IntsLimit);
			if(keysInTree.contains(randomNumber)){
				i--;
				continue;
			}
			keysInTree.add(randomNumber);
			//System.out.println(randomNumber);
			generatedTree.insert(randomNumber, Integer.toString(randomNumber));
			int k = generatedTree.insert(randomNumber, Integer.toString(randomNumber));
			if( k != -1){
				System.err.println("Error in insert - returned value should be -1");
			}
			testAllMethods(generatedTree, keysInTree, randomNumber);
			if((i==((int) size*3/4)) || (i==(int) size/2)){
				System.out.println(Integer.toString(i)+" elements were inserted to the tree\n##Testing ranks and ranks differences");
				RankTest(generatedTree.getRoot());	//Calls rank test method (Insert version)
			}
		}
			System.out.println("all elements were inserted to the tree\n##Testing ranks and ranks differences again");
			RankTest(generatedTree.getRoot());		//Calls rank test method (Insert version)

		System.out.println("done inserting nodes!..");
		
		/*Sort the key's list
		Collections.sort(keysInTree,new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
			
		});*/
		//Collections.reverse(keysInTree);
	
	}
	
	public void treeDelete(){
		//int arg;
		System.out.println("Start deleting nodes");
		Collections.shuffle(keysInTree);
		ArrayList<Integer> list = deepClone(keysInTree);
		//System.out.println(keysInTree.toString());
		int firstTestItem = keysInTree.get((int) (keysInTree.size()/2));				//Test ranks and rank-diff after deleting 1/2 of the tree
		int secondTestItem = keysInTree.get((int) (1*keysInTree.size()/4));	//Test ranks and rank-diff after deleting 3/4 of the tree
		for(int arg : keysInTree ){
			if(!Integer.toString(Collections.max(list)).equals(generatedTree.max())){
				System.err.println("problem in max method");
			}
			if(!Integer.toString(Collections.min(list)).equals(generatedTree.min())){
				System.err.println("problem in min method");
			}
			System.out.println(list.size());
			testAllMethods(generatedTree, list, arg);
			generatedTree.delete(arg);
			int k = generatedTree.delete(arg);
			if (k != -1){
				System.err.println("Error in delete - returned value should be -1");
			}
			if(arg == firstTestItem){
					System.out.println(((int) (keysInTree.size()/2))+" elements were deleted from the tree\n##Testing ranks and ranks differences");
					RankTest(generatedTree.getRoot());	//Calls rank test method (regular version)
			}
			else if(arg ==secondTestItem){
				System.out.println(((int) (3*keysInTree.size()/4))+" elements were deleted from the tree\n##Testing ranks and ranks differences");
				RankTest(generatedTree.getRoot());		//Calls rank test method (regular version)
			}
			list = removeArg(list, arg);
		}
		System.out.println("Done deleting nodes.. ;)");
	}
	
	private ArrayList<Integer> removeArg(ArrayList<Integer> list, int arg) {
		ArrayList<Integer> new_list = new ArrayList<>();
		for (int var : list){
			if (arg != var){
				new_list.add(var);
			}
		}
		return new_list;
	}

	private ArrayList<Integer> deepClone(ArrayList<Integer> keysInTree2) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int arg: keysInTree2){
			list.add(arg);
		}
		return list;
	}

	private void testAllMethods(WAVLTree root, ArrayList<Integer> keysInTree, int key) {
		if (root.empty() && keysInTree.size() != 0){
			System.err.println("Error in empty");
		}
		if(root.size() != keysInTree.size()){
			System.err.println("Error in size");
		}
		if(!Integer.toString(Collections.min(keysInTree)).equals(root.min())){
			System.err.println("Error in min");
		}
		if(!Integer.toString(Collections.max(keysInTree)).equals(root.max())){
			System.err.println("Error in max");
		}
		if(!generatedTree.search(key).equals(Integer.toString(key))){
			System.err.println("Error in search");
		}
		Collections.sort(keysInTree);
		Boolean checkKeysToAarray = checkEqulas(keysInTree,generatedTree.keysToArray() );
		if(!checkKeysToAarray){
			System.err.println("Error in keysToArray");
		}
		Boolean checkInfoToAarray = checkEqulas2(keysInTree,generatedTree.infoToArray() );
		if(!checkInfoToAarray){
			System.err.println("Error in infoToArray");
		}
	}

	private Boolean checkEqulas2(ArrayList<Integer> keysInTree2, String[] infoToArray) {
		for(int i = 0; i < keysInTree2.size(); i++){
			if(!Integer.toString(keysInTree2.get(i)).equals(infoToArray[i])){
				return false;
			}
		}
		return true;
	}

	

	private Boolean checkEqulas(ArrayList<Integer> keysInTree2, int[] keysToArray) {
		for(int i = 0; i < keysInTree2.size(); i++){
			if(!keysInTree2.get(i).equals(keysToArray[i])){
				return false;
			}
		}
		return true;
	}
	

	/*private List<Integer> convertList(int[] keysToArray) {
		List<Integer> list = new ArrayList<>(keysToArray.length);
		for (int val : keysToArray) {
		  list.add(val);
		}
		return list;
	}*/ 

	private static int height(WAVLTree.WAVLNode node) {
		if (node.getInfo().equals("EXT")) {
			return -1;
		}
		return Math.max(height(node.getLeft()), height(node.getRight())) + 1;
	}
	private static int getRank(WAVLTree.WAVLNode node) {
		if (node.getInfo().equals("EXT")) { // external leaf
			return -1;
		}
		return node.getRank();
	}

	private boolean RankTest(WAVLTree.WAVLNode node) {
		if (node.getInfo().equals("EXT")) {
			return true;
		} else if (height(node) != node.getRank()) {
			System.out.println("hieght");
			return false;
		}
		int x = getRank(node) - getRank(node.getRight());
		int y = getRank(node) - getRank(node.getLeft());
		if (x > 2 || x < 1) {
			return false;
		}
		if (y > 2 || y < 1) {
			return false;
		}
		if (x == 2 && y == 2) {
			return false;
		}

		boolean res = checkTreeAfterInsertions(node.getLeft());
		res = res && checkTreeAfterInsertions(node.getRight());
		return res;
		
	}
	
	private static boolean checkTreeAfterInsertions(WAVLTree.WAVLNode node) {
		if (node.getInfo().equals("EXT")) {
			return true;
		} else if (height(node) != node.getRank()) {
			System.out.println("hieght");
			return false;
		}
		int x = getRank(node) - getRank(node.getRight());
		int y = getRank(node) - getRank(node.getLeft());
		if (x > 2 || x < 1) {
			return false;
		}
		if (y > 2 || y < 1) {
			return false;
		}
		if (x == 2 && y == 2) {
			return false;
		}

		boolean res = checkTreeAfterInsertions(node.getLeft());
		res = res && checkTreeAfterInsertions(node.getRight());
		return res;
	}
	
	public static void main(String[] args){
		TesterWAVLTree t = new TesterWAVLTree();
		t.treeGenerator(1000, 4000);
		t.treeDelete();
	}

}
