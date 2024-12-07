/*
 * Graph Class
 * Specific to Sudoku use case!
 */

import java.util.ArrayList;
import java.util.HashSet;

public class Graph {
    private Vertex start;

    public Graph() {
        this.start = null;
    }
    public Graph(Vertex s) {
        this.start = s;
    }
    private Vertex getStart() {
        return this.start;
    }
    public boolean isEmpty(){
        return this.start == null;
    }
    public void add(int row, int col, int[] boxID, Vertex vertex) { //adds new vertex to graph and adds edges connecting to rows, cols, and vertices that impact its candidate list
        HashSet<Vertex> visited = new HashSet<>();
        ArrayList<Vertex> queue = new ArrayList<>();
    
        if (this.getStart() == null) {
            this.start = vertex;
            return;
        }

        queue.add(this.start);
        visited.add(this.start);
    
        while (!queue.isEmpty()) {
            Vertex temp = queue.remove(0); 
    
            if (temp.getBoxID()[0] == boxID[0] && temp.getBoxID()[1] == boxID[1]) {
                if (!vertex.hasEdgeWith(temp, 2)) { 
                    vertex.addEdge(temp, 2);
                    temp.addEdge(vertex, 2);
                }
            }
    
            if (temp.getRow() == row) {
                if (!temp.hasEdgeWith(vertex, 0)) {
                    vertex.addEdge(temp, 0);
                    temp.addEdge(vertex, 0);
                }
            }
            if (temp.getCol() == col) {
                if (!temp.hasEdgeWith(vertex, 1)) {
                    vertex.addEdge(temp, 1);
                    temp.addEdge(vertex, 1);
                }
            }
            for (Vertex neighbor : temp.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    if (neighbor.getRow() == row || neighbor.getCol() == col || 
                        (neighbor.getBoxID()[0] == boxID[0] && neighbor.getBoxID()[1] == boxID[1])) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
    }
    private int[] helper(Vertex vertex, HashSet<Vertex> verticesToRemove) {//helper functino for single candidate removal
        int[] toret = {-1,-1,-1};
        if (vertex.getCandidates().size() == 1) {
            int fixedValue = vertex.getCandidates().iterator().next(); 
            toret[0] = vertex.getRow();
            toret[1] = vertex.getCol();
            toret[2]= fixedValue;
            for (Vertex neighbor : vertex.getNeighbors()) {
                neighbor.getCandidates().remove(fixedValue);
            }
            verticesToRemove.add(vertex); 
        }
        return toret;
    }
    public boolean eliminateNakedGroups(int groupSize) { //finds neighboring vertices with identical candidate sets, eliminates options from shared neighbors
        boolean changesMade = false;
        ArrayList<Vertex> queue = new ArrayList<>();
        HashSet<Vertex> visited = new HashSet<>();
        HashSet<Vertex> verticesToProcess = new HashSet<>();
    
        if (this.start == null) return false;
        queue.add(this.start);
        visited.add(this.start);
    
        // Traverse the graph
        while (!queue.isEmpty()) {
            Vertex current = queue.remove(0);
            findGroups(current, verticesToProcess, groupSize);
    
            for (Vertex neighbor : current.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    
        // Process each group
        for (Vertex vertex : verticesToProcess) {
            ArrayList<Vertex> neighborsCopy = new ArrayList<>(vertex.getNeighbors());
    
            for (Vertex sharedNeighbor : neighborsCopy) {
                if (!sharedNeighbor.equals(vertex)) {
                    if (vertex.getCandidates().equals(sharedNeighbor.getCandidates())
                            && vertex.getCandidates().size() == groupSize) {
                        changesMade = true;
    
                        // Remove candidates from all shared neighbors
                        for (Vertex neighborOfShared : sharedNeighbor.getNeighbors()) {
                            if (!neighborOfShared.equals(vertex)
                                    && isInCommonRegion(vertex, sharedNeighbor, neighborOfShared)) {
                                neighborOfShared.getCandidates().removeAll(vertex.getCandidates());
                            }
                        }
                    }
                }
            }
        }
        return changesMade;
    }
    
    private void findGroups(Vertex vertex, HashSet<Vertex> verticesToProcess, int groupSize) { //helper for naked groups
        for (Vertex neighbor : vertex.getNeighbors()) {
            if (vertex.getCandidates().equals(neighbor.getCandidates())
                    && vertex.getCandidates().size() == groupSize) {
                verticesToProcess.add(vertex);
                verticesToProcess.add(neighbor);
            }
        }
    }
    private boolean isInCommonRegion(Vertex vertex1, Vertex vertex2, Vertex candidate) { //helper for naked groups
        return ( vertex1.getBoxID().equals(vertex2.getBoxID()) && vertex1.getBoxID().equals(candidate.getBoxID()) ||
        vertex1.getRow() == vertex2.getRow() && vertex1.getRow() == (candidate.getRow()) ||
        vertex1.getCol() == (vertex2.getCol()) && vertex1.getCol() == candidate.getCol() );
    }    
    public ArrayList<int[]> eliminateSingleCandidates() { //removes vertex with one single candidate (as it is a solution); removes option from vertex's direct neighbors
        ArrayList<Vertex> queue = new ArrayList<>();
        HashSet<Vertex> visited = new HashSet<>();
        HashSet<Vertex> verticesToRemove = new HashSet<>();
        ArrayList<int[]> items = new ArrayList<>();

        if (this.start == null) return items;
        queue.add(this.start);
        visited.add(this.start);

        while (!queue.isEmpty()) {
            Vertex current = queue.remove(0);
            int[] coords = helper(current, verticesToRemove);
            if (coords[0] != -1){
                items.add(coords); 
            }
            for (Vertex neighbor : current.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        for (Vertex vertex : verticesToRemove) {
            ArrayList<Vertex> neighborsCopy = new ArrayList<>(vertex.getNeighbors());

            for (Vertex neighbor : neighborsCopy) {
                vertex.removeEdge(neighbor);
                neighbor.removeEdge(vertex);
            }

            vertex.getNeighbors().clear();
            vertex.clearEdges(); 

            if (vertex == this.start) {
                this.start = vertex.getNeighbors().isEmpty() ? null : vertex.getNeighbors().iterator().next();
            }
        }
        return items;
    }
    @Override
    public String toString() {//chat helped with this
        if (this.start == null) {
            return "Graph is empty";
        }

        StringBuilder result = new StringBuilder();
        ArrayList<Vertex> visited = new ArrayList<>();
        ArrayList<Vertex> queue = new ArrayList<>();

        queue.add(this.start);
        visited.add(this.start);

        while (!queue.isEmpty()) {
            Vertex current = queue.remove(0);
            result.append(current.toString()).append("\n");

            // Add all unvisited neighbors to the queue
            for (Vertex neighbor : current.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return result.toString();
    }
}
