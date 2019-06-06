
import java.io.*;
import java.util.*;


/*
0. Identifier for one contig in overlap.
1. Identifier for second contig in overlap.
2. A umber representing similarity, but not relevant for this project.
3. Fraction of identical positions in the overlap.
4. A zero. Unused.
5. Start of overlap in first contig.
6. End of overlap in first contig.
7. Length of first contig.
8. A zero (0) or one (1) to say whether the overlap is on the reverse sequence (1) or not (0). See below for how this is used.
9. Start of overlap in second contig.
10. End of overlap in second contig.
11. Length of second contig.
*/


public class DataToGraphConverter{
	
	
	private DataToGraphConverter(){
		
	}
	
	//I use a wrapper function which returns a BufferedReader, if one would like to change the way we specify which file we want to use
	//Can be useful sometimes
	public static Graph readM4(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		return readM4(br);
	}
	
	public static Graph readM4(BufferedReader br) throws IOException{
		
		String line;
		line = br.readLine();
		
		Graph g = new Graph(0);
		
		int lineNumber = 0;
		
		//Note that every node we read is connected with at least one edge, according to data
		while (( line = br.readLine()) != null ){
		
		
		//Investigating the memory usage after the GC error ( garbage collection)
// 			lineNumber++;
// 			if(lineNumber %100000 == 0){
// 				long freeMem = Runtime.getRuntime().freeMemory();
// 				long maxMem = Runtime.getRuntime().maxMemory();
// 				double usage = (100.0*freeMem)/maxMem;
// 				System.out.println(lineNumber + " \t" + usage + "\t  Max:" + maxMem/(1024.0*1024));
// 			}
			
			String[] lineEntries = line.split("\\s+");
			
			String nodeA = lineEntries[0];
			String nodeB = lineEntries[1];
			
			
			int startA = Integer.parseInt(lineEntries[5]);
			int endA = Integer.parseInt(lineEntries[6]);
			int lengthA = Integer.parseInt(lineEntries[7]);
			
			int startB = Integer.parseInt(lineEntries[5]);
			int endB = Integer.parseInt(lineEntries[6]);
			int lengthB = Integer.parseInt(lineEntries[7]);
			
			//Make sure the overlap is not substring
			//Sometimes, the overlap is actually containment: if contig A is a subsequence of contig B, then A can be discarded.
			if(endA - startA < lengthA && endB - startB < lengthB ){
				g.addEdge(nodeA, nodeB);
			}
			
		} 
		
		return g;
	}
	
	
	//Do run this you need to allocate 8G
	//Run this: java -Xmx8G DataToGraphConverter ../overlaps.m4
	public static void main(String[] args){
		String filename = args[0];
		
		int maxSize;
		
		int threshold;
		
		int cutDecrease;
		
		if(args.length > 3){
		
			maxSize = Integer.parseInt(args[1]);
			threshold = Integer.parseInt(args[2]);
			cutDecrease = Integer.parseInt(args[3]);
			
		} else {
			
			maxSize = 1000;
			
			threshold = 100;
			
			cutDecrease = 5;
		
		}
		
		Graph g = null;
		
		try{
			g = DataToGraphConverter.readM4(filename);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		int[] degreeDist = g.degreeDist();
		
		int[] set = g.connectedComponents(degreeDist);
		
		int[] sizes = g.partitionDist(set);
		
		for(int i = 0; i < sizes.length; i++){
		
			System.out.println(sizes[i]);
			
		}	
		
		g.writeArray(degreeDist, "degreesPrior.txt");
		
		g.writeArray(sizes, "sizesPrior.txt");
		
		//System.out.println(set.length);
		
		//g.partitionWithoutExtremes(maxSize, threshold);
		
		g.partition3(maxSize, threshold, cutDecrease);
		
		degreeDist =  g.degreeDist();
		
		set = g.connectedComponents(degreeDist);
		
		sizes = g.partitionDist(set);
		
		g.partitionDist(set);
		
		g.writePartitions(set);
		
		g.writeArray(degreeDist, "degreesAfter.txt");
		
		g.writeArray(sizes, "sizesAfter.txt");
	}
	
	
	
}
