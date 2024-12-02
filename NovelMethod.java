import java.util.*;
public class NovelMethod {
    public static Integer[] possible = {1,2,3, 4, 5, 6,7,8,9};
    Set<Integer> BOARD_SIZE = new HashSet<>(Arrays.asList(possible));
    public static int[][] board = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
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
    public static boolean isSolved(int[][] board) {
        // Check if all cells are filled and the board is valid
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    return false; // Unsolved
                }
                // Validate the number at this position
                if (!isValid(board, row, col, board[row][col])) {
                    return false; // Invalid board
                }
            }
        }
        return true;
    }

    public static List<int[][]> generateNextStates(int[][] board) {
        List<int[][]> nextStates = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) { // Empty cell found
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            int[][] newState = deepCopy(board);
                            newState[row][col] = num;
                            nextStates.add(newState);
                        }
                    }
                    return nextStates; // Only generate for the first empty cell
                }
            }
        }
        return nextStates;
    }

    public static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num && i != col) return false;
            if (board[i][col] == num && i != row) return false;
        }
        // Check 3x3 subgrid
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int r = startRow + i, c = startCol + j;
                if (board[r][c] == num && !(r == row && c == col)) return false;
            }
        }
        return true;
    }

    public static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

    public static int[][] solveBFS(int[][] board) {
        Queue<int[][]> queue = new LinkedList<>();
        queue.add(board);

        while (!queue.isEmpty()) {
            int[][] currentState = queue.poll();
            if (isSolved(currentState)) {
                return currentState; // Solution found
            }
            queue.addAll(generateNextStates(currentState)); // Generate and queue all valid next states
        }
        return null; // No solution found
    }
    public static void main(String[] args) {
        HashSet<Integer> cand;
        Graph CandidateGraph = new Graph();
        int[] boxID = new int[2];
        long startTime = System.nanoTime();
        for (int i=0; i<board.length; i++){

            int rowIndex = (i) / ((int)Math.sqrt(board.length));
            boxID[0] = rowIndex;
            //0,1,2
            for (int j = 0; j<board.length; j++){
                if (board[i][j]==0){
                    int colIndex = (j) / ((int)Math.sqrt(board.length));
                    boxID[1] = colIndex;
                        //System.out.println("Adding to graph at box" + boxID[0] + "," + boxID[1] + " at position " + i +"," + j);
                        cand = CreateCandidates(i,j,boxID,board); //create candidates for each box without a fixed solution
                        //System.out.println("\t candidates are: " + cand);
                        CandidateGraph.add(i,j,boxID, (new Vertex(i,j,boxID,cand)));//adds to graph
        
                }
  
            }

        }
        while (!CandidateGraph.isEmpty()){//solve for sodoku items until graph empty (no pending solutions)
            ArrayList<int[]> i = CandidateGraph.eliminateSingleCandidates(); //finds obvi solutions and returns solutions found
            if (i.size() == 0){//if no solution was found u should check for identical direct neighbored pairs
                CandidateGraph.eliminateNakedPairs();
            }else{//if u did find solutions, then put them on the board
                for (int d = 0; d<i.size(); d++){
                    board[i.get(d)[0]][i.get(d)[1]] = i.get(d)[2];//super long winded way of editing the board
                }
            }
            
        }
        int[][] f =solveBFS(board);
        long endTime = System.nanoTime(); // End the timer
        long elapsedTime = endTime - startTime; // Calculate elapsed time
        System.out.println("Execution Time: " + (elapsedTime / 1_000_000.0) + " ms");
        for (int[] row : f) {
            System.out.println(Arrays.toString(row));
            
        }
        /*
        for (int row = 0; row<board.length; row++){
            for (int col=0; col<board[row].length; col++){
                System.out.print(board[row][col]);
            }
            System.out.println();
        }
         */
        

    }



    
}
