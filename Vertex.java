/*
 * Vertex class
 * Specific to Sudoku use case
 *ChatGPT provided immense assistance with functions: removeEdge, removeEdges, countEdgetypes, hasEdgeWith, toString.
 */
import java.util.*;
public class Vertex {
    private HashSet<Integer> candidateData;
    private int row;
    private int col;
    private int[] box = new int[2];
    private ArrayList<Edge> edges;
    private ArrayList<Vertex> neighbors;
    public Vertex(HashSet<Integer> candidateData){
        this.candidateData = candidateData;
    }
    public Vertex(int row,int col, int[] box, HashSet<Integer> candidateData){
        this.row = row;
        this.col = col;
        this.box[0] = box[0];
        this.box[1] = box[1];
        this.candidateData = candidateData;
        this.neighbors = new ArrayList<Vertex>();
        this.edges = new ArrayList<Edge>();
        
    }
    public Vertex(){
        this.candidateData = null;
        this.neighbors = new ArrayList<Vertex>();
        this.edges = new ArrayList<Edge>();
        this.row = -1;
        this.col = -1;
    }
    public Map<String, Integer> countEdgeTypes() {
        Map<String, Integer> edgeCount = new HashMap<>();
        
        // Initialize counts for each edge type
        edgeCount.put("box", 0);
        edgeCount.put("row", 0);
        edgeCount.put("col", 0);
        
        // Iterate through the edges and count each type
        for (Edge edge : this.edges) {
            int edgeType = edge.getEdgeType();
            
            if (edgeType == 0) {  // row edge type
                edgeCount.put("row", edgeCount.get("row") + 1);
            } else if (edgeType == 1) {  // col edge type
                edgeCount.put("col", edgeCount.get("col") + 1);
            } else if (edgeType == 2) {  // box edge type
                edgeCount.put("box", edgeCount.get("box") + 1);
            }
        }
        
        return edgeCount;
    }
    public void removeEdges(Vertex target) { //chat help
        // Loop through edges list and remove edges that involve the target vertex
        for (int i = 0; i < this.edges.size(); i++) {
            Edge edge = this.edges.get(i);
            // Assuming each edge connects this vertex with a neighbor
            if (edge.getv1().equals(target) || edge.getV2().equals(target)) {
                this.edges.remove(i); // Remove the edge at index i
                i--; // Adjust the index to account for the removed element
            }
        }
    }
    public void removeEdge(Vertex neighbor) {
        this.neighbors.remove(neighbor);
        neighbor.neighbors.remove(this);
        // Remove the corresponding edge
        this.edges.removeIf(edge -> edge.getV2().equals(neighbor));
        neighbor.edges.removeIf(edge -> edge.getV2().equals(this));
    }
    
    
    public void clearEdges() {
        this.edges.clear();
    }
    
    public ArrayList<Vertex> getNeighbors(){
        if (this.neighbors!= null)
        {
            return this.neighbors;
        }else{
            return new ArrayList<Vertex>();
        }
    }
    public HashSet<Integer> getCandidates(){
        if (this.candidateData!= null)
        {
            return this.candidateData;
        }else{
            return new HashSet<Integer>();
        }
    }
    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public int[] getBoxID(){
        return this.box;
        
    }
    public ArrayList<Edge> getEdges(){
        return this.edges;
    }
    public void addEdge(Vertex vertex, int edgeType){
        if (!hasEdgeWith(vertex, edgeType)){
            this.edges.add(new Edge(this, vertex, edgeType));
            this.neighbors.add(vertex);
        }
        
    }
    public boolean hasEdgeWith(Vertex v, int edgeType) {
        for (Edge e : this.getEdges()) {
            if (e.getV2().equals(v) && e.getEdgeType() == edgeType) {
                return true;
            }
        }
        return false;
    }
    
    public String toString(){
        StringBuilder neighborData = new StringBuilder();//chat help
        for (Vertex neighbor : neighbors) {
            neighborData.append("[" +neighbor.getRow() + "," +neighbor.getCol()+ "]" + neighbor.getCandidates()).append(", "); // Replace getData() with an appropriate property
        }
        if (neighborData.length() > 0) {
            neighborData.setLength(neighborData.length() - 2); // Remove the trailing ", "
        }

        return ("[" + this.row + ","+this.col + "]" +"here is my data: " + this.candidateData + "and ym neighbors: " + this.neighbors.size() + "\n " + countEdgeTypes());
       // return("here is my data: " + this.candidateData + " \n here is my neigh: " + this.getNeighbors() + "\nrow,col,box: ");
    }
}
