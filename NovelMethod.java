import java.util.*;
public class NovelMethod {
    public static Integer[] possible = {1,2,3, 4, 5, 6,7,8,9};
    Set<Integer> BOARD_SIZE = new HashSet<>(Arrays.asList(possible));
    public static int[][] board = {
        {0, 0, 0, 6, 0, 0, 4, 0, 0},
        {7, 0, 0, 0, 0, 3, 6, 0, 0},
        {0, 0, 0, 0, 9, 1, 0, 8, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 5, 0, 1, 8, 0, 0, 0, 3},
        {0, 0, 0, 3, 0, 6, 0, 4, 5},
        {0, 4, 0, 2, 0, 0, 0, 6, 0},
        {9, 0, 3, 0, 0, 0, 0, 0, 0},
        {0, 2, 0, 0, 0, 0, 1, 0, 0}
    };
    public static HashSet<Integer> CreateCandidates(int row, int col,int[] boxID, int[][] board){
        HashSet<Integer> candidates = new HashSet(Arrays.asList(possible));
        int[] colItems = new int[9];
        ArrayList<Integer> boxItems = new ArrayList<>();
        for (int i=0;i<board.length;i++){
            colItems[i] = board[i][col];
        }
        for (int i = boxID[0]*3; i <boxID[0]*3+3; i++){
            for (int j = boxID[1]*3;j<boxID[1]*3+3; j++){
                boxItems.add(board[i][j]);
            }
        }
        for (int value : board[row]) {
            candidates.remove(value);
        }
        for (int value : boxItems) {
            candidates.remove(value);
        }
        for (int value : colItems) {
            candidates.remove(value);
        }
        return candidates;
    }
    public static void main(String[] args) {
        HashSet<Integer> cand;
        Graph CandidateGraph = new Graph();
        int[] boxID = new int[2];
        long startTime = System.nanoTime();
        for (int i=0; i<board.length; i++){
            int rowIndex = (i) / ((int)Math.sqrt(board.length));
            boxID[0] = rowIndex;
            for (int j = 0; j<board.length; j++){
                if (board[i][j]==0){
                    int colIndex = (j) / ((int)Math.sqrt(board.length));
                    boxID[1] = colIndex;
                        cand = CreateCandidates(i,j,boxID,board); //create candidates for each box without a fixed solution
                        CandidateGraph.add(i,j,boxID, (new Vertex(i,j,boxID,cand)));//adds to graph
                }
            }
        }
        boolean x;
        while (!CandidateGraph.isEmpty()){//solve for sodoku items until graph empty (no pending solutions)
            ArrayList<int[]> i = CandidateGraph.eliminateSingleCandidates(); //finds obvi solutions and returns solutions found
            if (i.size() == 0){//if no solution was found u should check for identical direct neighbored pairs
                x = CandidateGraph.eliminateNakedPairs();
                if (x==false) break;
            }else{//if u did find solutions, then put them on the board
                for (int d = 0; d<i.size(); d++){
                    board[i.get(d)[0]][i.get(d)[1]] = i.get(d)[2];//super long winded way of editing the board
                }
            }
        }
        System.out.println(("soegn"));
        SudokuSolverDLS.solveDLS(board,0,0,0);
        long endTime = System.nanoTime(); // End the timer
        long elapsedTime = endTime - startTime; // Calculate elapsed time
        
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));     
        }
        System.out.println("Execution Time: " + (elapsedTime / 1_000_000.0) + " ms");
    }  
}
