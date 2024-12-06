
/*
 * DLS Implementation of Sodoku Solver
 * Implementation based on https://github.com/elondemi/Sudoku-Solver
 * Was originally in Python
 * Asked ChatGPT, "can you transform *program* from python to java"
 * Comments and majority implementation from chat
 */
import java.util.*;

public class SudokuSolverDLS {

    // Define a constant for the maximum depth
    private static final int MAX_DEPTH = 75;

    // Check if the Sudoku board is valid
    public static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num)
                return false;
        }
        // Check the 3x3 subgrid
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num)
                    return false;
            }
        }
        return true;
    }

    // Solve Sudoku using Depth-Limited Search (DLS)
    public static boolean solveDLS(int[][] board, int row, int col, int depth) {
        if (depth > MAX_DEPTH) {
            return false; // Exceeded depth limit
        }

        // If the entire board is filled, return true
        if (row == 9)
            return true;

        // Move to the next row if we are at the end of the column
        if (col == 9) {
            return solveDLS(board, row + 1, 0, depth + 1);
        }

        // Skip filled cells
        if (board[row][col] != 0) {
            return solveDLS(board, row, col + 1, depth);
        }

        // Try filling the empty cell with numbers 1-9
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num; // Assign the number
                if (solveDLS(board, row, col + 1, depth + 1)) {
                    return true; // Continue to next cells if valid
                }
                board[row][col] = 0; // Backtrack if solution isn't found
            }
        }

        return false; // No valid solution found for this cell
    }

    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) {

        ArrayList<int[][]> boards = BoardParser.parseBoardsFromFile("boards.txt");
        long startTimeOverall = System.nanoTime();
        long startTimeTemp;
        long endTimeTemp;
        long elapsedTimeTemp;
        for (int[][] board : boards) {
            startTimeTemp = System.nanoTime();
            solveDLS(board, 0, 0, 0);
            endTimeTemp = System.nanoTime();
            printBoard(board);

            // End the timer
            elapsedTimeTemp = endTimeTemp - startTimeTemp; // Calculate elapsed time
            System.out.println("Solve Time: " + (elapsedTimeTemp / 1_000_000.0) + " ms");
        }
        long endTimeOverall = System.nanoTime();
        long elapsedTimeOverall = endTimeOverall - startTimeOverall;
        System.out.println("Overall Execution Time for All Boards: " + (elapsedTimeOverall / 1_000_000.0) + " ms");
    }
}
