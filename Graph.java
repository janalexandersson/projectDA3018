
//adjacency matrix takes more space but is more efficient with removing edges etc
//adjacency list takes less space but can be inefficient at finding of there is and edge between two nodes



//JAN will look at this during the weekend
public class Graph{


	int n;
	//We want to store the vertices as integers to save space
	//Need some way to translate between integers and vertex names
	LinkedList<Integer> adjList[];
	
	public Graph(int n){
		this.n = n;
				
		adjList = new LinkedList[n]; 
              
		// Create a new list for each vertex 
		// adjacent vertices will be stored in the list
		for(int i = 0; i < n ; i++){ 
			adjList[i] = new LinkedList<>(); 
		} 
	}

	
	public void addEdge(int from, int to) { 
        // Add an edge from 'from' to 'to'.  
        this.adjList[from].add(from); 
          
		// Add that edge to the other vertex too because our graph is undirected
        this.adjList[to].add(to); 
    } 
	
	
	public void removeEdge(int from, int to) { 
		
		//JAN will look at this during the weekend
		
    } 
	
	
	public void removeNode(int node) { 
		//JAN will look at this during the weekend
    } 
	
	
	
	
	//Some algorithm for counting partitions will be implemented below etc..
	
	
	
	
	
	
}