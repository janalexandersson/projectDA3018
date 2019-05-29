
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
	
	
	
	//Uses sizes of the adjecencylist to create the distribution of degrees
	// by taking the length of the list of neighbours for each node
	public int[] degreeDist(){
	
		int[] degrees = new int[adjList.size()]; 
	
		for(int i = 0; i < adjList.size(); i++){
		
			degrees[i] = adjList.get(i).size();

		}
		
		return degrees;
	
	}
	
	// Help function for partitionDist. Could not find a built in for max of an array so I wrote one myself.
	public int arrayMax(int[] array){ //O(n)

		int max = array[0];
		
		for(int i = 1; i < array.length; i++){
		
			if(array[i] > max){
			
				max = array[i];
			
			}
		
		}
		
		return(max);

	}
	
	
	//Takes the array returned by connectedComponents
	public int[] partitionDist(int[] set){
		
		int max = arrayMax(set);
		
		//Time vs memory, either we save the max, which uses memory, or we calculate the max twice which uses extra time
		
		int[] sizes = new int[max+1];

		
		for(int k = 0; k < set.length; k++){
		
			sizes[set[k]]++; //int[] initialized with 0
			
		}
		
		return sizes;
	
	}
	
	//Takes some maximum size for a partition whilst also taking a threshold where we remove all degrees of that degree or higher
	public void partitionWithoutExtremes(int maxSize, int threshold){
					
		//Generates arrays of (max length n) used for the algorithm 			
					
		int[] degrees = degreeDist();
		
		int[] set = connectedComponents();
			
		int[] sizes = partitionDist(set);
		
		System.out.println("Max Partition Size: " + arrayMax(sizes));
		
		int removed = 0; //Counting number of removed nodes
		
		while(arrayMax(sizes) > maxSize){ //While some partition is too large...
		
			for(int k = 0; k < sizes.length; k++){
			
				if(sizes[k] > maxSize){ //If a specific partition is too large...
					
					int toRemove = 0; //We can let this be 0 since there will alwasys be a node with tha largest degree.
					
					int degreeRemoved = 0;
					
					for(int i = 0; i < set.length; i++){
					
						if(set[i] == k){
							
							if(degrees[i] >= threshold){ //Remove all over certain threshold
							
								removeNode(i);
								
								removed++;
							
							}else if(degrees[i] > degreeRemoved){ //Update the node with the largest degree to remove it
							
								toRemove = i;
								
								degreeRemoved = degrees[i];
								
							}
						}
					}
					
					removeNode(toRemove); //Removing node with largest degree
					removed++;
					
				}
				
			}
			
			//Updates the arrays used in the algorithm
			
			degrees = degreeDist(); //O(n)
			
			set = connectedComponents(); // O(?)
			
			sizes = partitionDist(set);	//O(n)
			
			System.out.println("Max Partition Size: " + arrayMax(sizes));
			
		}
		
		System.out.println("Partition Complete: " + removed + " nodes were removed");
		
	}
	
	
	
    /**
     *does: reduces too big partitions step vise by removing nodes with smaller and smaller degree,
     * the steps given by cutDecrease.
     * returns: nothing but updates the arrays degrees, set and sizes
     *
     * pros: can treat partitions differently
     * cons: removes a little too much. We remove all nodes with degree>cutDegree, even if partition
     *       during the loop has become < maxsize. Checking more often however might take much time.
     *
     * @param maxsize max partition size
     * @param threshold nodes with this degree and more are removed. default = 100
     * @param cutDecrease how big steps we should take when iterating
     */
    public void partition3(int maxsize, int threshold, int cutDecrease){
        int[] degrees = degreeDist(); //degree of each node

        System.out.println("Max degree before removing degrees >= " + threshold + ": " + arrayMax(degrees));

        for(int i=0; i < degrees.length; i++){ // get rid of all obviously faulty nodes
            if(degrees[i] >= threshold){
                removeNode(i);
            }
        }

        degrees = degreeDist();  // update
        int[] set = connectedComponents(); //gives which partition a node belongs to
        int[] sizes = partitionDist(set); // sizes of the partitions. length=#partitions


        System.out.println("Max degree after removing >= " + threshold + ": " + arrayMax(degrees));
        System.out.println("Max Partition Size before first loop: " + arrayMax(sizes));
        System.out.println("Number of partitions before first loop: " + sizes.length);

        int nloops = 0; // to be updated and printed
        String continueLoop = "yes";
        if(arrayMax(sizes) < maxsize){
            continueLoop = "no";
        }
        int cutDegree = threshold-cutDecrease; // nwe remove nodes with this degree
        while(continueLoop.equals("yes")){
            reduce(maxsize, degrees, set, sizes, cutDegree); // see function below
            // updates
            degrees = degreeDist();
            set = connectedComponents();
            sizes = partitionDist(set);
            cutDegree-=cutDecrease;
            nloops++;
            if(arrayMax(sizes)<maxsize){
                continueLoop = "no";
            }

        }
        System.out.println("Number of loops: " + nloops);
        System.out.println("Max degree after last loop: " + arrayMax(degrees));
        System.out.println("Max Partition Size after last loop: " + arrayMax(sizes));
        System.out.println("Number of partitions after last loop: " + sizes.length);
    }

    /**
     * helps partition3
     * reduces partition size of too big partitions by removing nodes with degree given by cutDegree.
     * degrees, set and sizes must be updated between function calls
     *
     * @param maxsize max size of partition
     * @param degrees degree of each node
     * @param set which partition each node belongs to
     * @param sizes size of each partition
     * @param cutDegree nodes with this degree are removed
     */
    public void reduce(int maxsize, int[] degrees, int[] set, int[] sizes, int cutDegree){
        for(int partition=0; partition < sizes.length; partition++){  // go through all partitions
            if(sizes[partition] > maxsize){  // if partition is too big
                for(int nodeNumber = 0; nodeNumber < set.length; nodeNumber++ ){ //check all nodes
                    if(set[nodeNumber] == partition & degrees[nodeNumber] >= cutDegree){ //cut those with biggest degree
                        removeNode(nodeNumber);
                    }
                }
            }
        }
    }

	
}
