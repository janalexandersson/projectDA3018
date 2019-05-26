
//adjacency matrix takes more space but is more efficient with removing edges etc
//adjacency list takes less space but can be inefficient at finding of there is and edge between two nodes

import java.io.*;
import java.util.*;

// ajdList represents the graph
//nodeNameToIndex translates between nodename to index
//indexToNodeName translates between index to nodename
public class Graph{


	//We want to store the vertices as integers to save space
	//Need some way to translate between integers and vertex names
	ArrayList<Collection<Integer>> adjList;
	HashMap<String, Integer> nodeNameToIndex = new HashMap<String, Integer>();
	HashMap<Integer, String> indexToNodeName = new HashMap<Integer, String>();
	
	public Graph(int n){
				
		adjList = new ArrayList(n); 
              
		// Create a new list for each vertex 
		// adjacent vertices will be stored in the list
		for(int i = 0; i < n ; i++){ 
			adjList.add(new ArrayList<Integer>()); 
		} 
	}
	
	//It might be better to use TreeSet instead of ArrayList for storing the neightbors
	//Because then it will be faster to remove specified edges from our adjacency list
	public void addEdge(int from, int to) {  
		//Add more vertices with no edges
        while( adjList.size() <= Math.max(from, to)){
			adjList.add(new ArrayList<Integer>());
        }
        
		// Add an edge from 'from' to 'to'. 
        this.adjList.get(from).add(to); 
          
		// Add that edge to the other vertex too because our graph is undirected
        this.adjList.get(to).add(from); 
    } 
	
	public void addEdge(String from, String to) {  
		if(!nodeNameToIndex.containsKey(from)){
			int i =  nodeNameToIndex.size();
			nodeNameToIndex.put(from, i);
			indexToNodeName.put(i, from);
		}
		
		if(!nodeNameToIndex.containsKey(to)){
			int i =  nodeNameToIndex.size();
			nodeNameToIndex.put(to, i);
			indexToNodeName.put(i, to);
		}
		
		//Map from string to int, stringname to index
		int fromIndex = nodeNameToIndex.get(from);
		int toIndex = nodeNameToIndex.get(to);
		
		addEdge(fromIndex, toIndex);
    } 
	
	
	public void printAllEdges() { 
		
		for(int v = 0; v < adjList.size(); v++){
			Collection<Integer> vNeighbors = adjList.get(v);
			
			
			for(int n: vNeighbors){
				System.out.println(v + " " + n);
			}
			
			
// 			for(int n = 0; n< vNeighbors.size(); n++){
// 				System.out.println(v + " " + vNeighbors.get(n));
// 			}
		}
    } 
	
	
	public String indexToNodeName(int index){
		return indexToNodeName.get(index);
	}
	
	
	//Fill in the connected component containing node "startNode" with the number "componentNumber"
	//This uses Flood Fill
	private void floodFillHelper(int[] indicator, int startNode, int componentNumber){
		
		Stack<Integer> toVisit = new Stack<Integer>();
		
		indicator[startNode] = componentNumber;
		toVisit.push(startNode);
		
		//recursion did not work due to stack overflow
		while(!toVisit.isEmpty()){
			int node = toVisit.pop();
			indicator[node] = componentNumber;
			
			
			//loop over neighbors and push nodes to visit
			for(int n: adjList.get(node)){
				if(indicator[n] != componentNumber){
					toVisit.push(n);
				}
			}
		}
				
	}
	
	
	//If there are components of size 1, those should be discarded beacuase we technically removed the node (leaved it as isolated)
	public int[] connectedComponents(){
		int[] indicator = new int[adjList.size()];
		
		Arrays.fill(indicator, -1);
		
		int k = 0;
		for(int i = 0; i<indicator.length; i++){
			//if node i does not belong to a component yet, it has value 0
			if(indicator[i] == -1){
				//paint component with color k
				floodFillHelper(indicator, i, k);
				k++;
			}
		}
		System.out.println("Partitions = " + k);
		return indicator;
	}
	
	
	
	//This will leave nodes as isolated, we know that we did not have any isolated nodes in our base graph
	public void removeNode(int nodeToRemove) { 
		
		Collection<Integer> neightbors = adjList.get(nodeToRemove);
		
		for(int n: neightbors){
			adjList.get(n).remove(nodeToRemove);
		}
		
		adjList.get(nodeToRemove).clear();
    } 
    //To count number of removed edges we can just see how many edges we had before partitoning and then subrtact edges after partitoning
	
	
	public int[] degreeDist(){
	
		int[] degrees = new int[adjList.size()]; 
	
		for(int i = 0; i < adjList.size(); i++){
		
			degrees[i] = adjList.get(i).size();

		}
		
		return degrees;
	
	}
	
	
	public int arrayMax(int[] array){

		int max = array[0];
		
		for(int i = 1; i < array.length; i++){
		
			System.out.println(array[i]); //int[] initialized with 0
		
		}
		
		return(max);

	}
	
	public int[] partitionDist(){
				
		int[] set = connectedComponents();
		
		int max = arrayMax(set);
		
		//Time vs memory, either we save the max, which uses memory, or we calculate the max twice which uses extra time
		
		int[] sizes = new int[max+1];

		
		for(int k = 0; k < set.length; k++){
		
			sizes[set[k]]++; //int[] initialized with 0
			
		}
		
		return sizes;
	
	}

}
