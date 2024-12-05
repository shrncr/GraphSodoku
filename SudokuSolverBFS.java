/*
 * BFS Implementation of Sodoku Solver
 * Implementation based on https://github.com/erickfunier/sudoku-solver
 * Was originally in Python
 * Asked ChatGPT, "can you transform *program* from python to java"
 * Original transformation did not work
 * Changed board to 0's, returned program to Chat, Chat returned fixed program
 */
import java.util.*;

public class SudokuSolverBFS {

    public static boolean isSolved(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    return false; //unsolved
                }
                if (!isValid(board, row, col, board[row][col])) {
                    return false; //not valid
                }
            }
        }
        return true;
    }


    public static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num && i != col) return false;
            if (board[i][col] == num && i != row) return false;
        }
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

    public static List<int[][]> generateNextStates(int[][] board) {
        List<int[][]> nextStates = new ArrayList<>();
        outerLoop:
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
                    break outerLoop; 
                }
            }
        }
        return nextStates;
    }
    
    public static ArrayList<int[][]> solveBFS(int[][] board) {
        Queue<int[][]> queue = new LinkedList<>();
        queue.add(board);
        ArrayList<int[][]> solutions = new ArrayList<>();
    
        while (!queue.isEmpty()) {
            int[][] currentState = queue.poll();
            if (isSolved(currentState)) {
                boolean isDuplicate = solutions.stream().anyMatch(solution -> Arrays.deepEquals(solution, currentState));
                if (!isDuplicate) {
                    solutions.add(currentState);
                }
                continue; 
            }
            queue.addAll(generateNextStates(currentState));
        }
        return solutions; 
    }
    

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        int[][] board = {
            {2, 9, 5, 7, 4, 3, 8, 6, 1},
            {4, 3, 1, 8, 6, 5, 9, 0, 0},
            {8, 7, 6, 1, 9, 2, 5, 4, 3},
            {3, 8, 7, 4, 5, 9, 2, 1, 6},
            {6, 1, 2, 3, 8, 7, 4, 9, 5},
            {5, 4, 9, 2, 1, 6, 7, 3, 8},
            {7, 6, 3, 5, 2, 4, 1, 8, 9},
            {9, 2, 8, 6, 7, 1, 3, 5, 4},
            {1, 5, 4, 9, 3, 8, 6, 0, 0}
        };

        ArrayList<int[][]> solutions = solveBFS(board);
        if (solutions.size() > 0) {
            for (int[][] solvedBoard : solutions){
                for (int[] row : solvedBoard) {
                    System.out.println(Arrays.toString(row));
                }
                System.out.println();
            }
            
        
        } else {
            System.out.println("No solution found!");
            
        }
        long endTime = System.nanoTime(); // End the timer
        long elapsedTime = endTime - startTime; // Calculate elapsed time
        System.out.println("Execution Time: " + (elapsedTime / 1_000_000.0) + " ms");
    }
}
