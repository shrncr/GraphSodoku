import java.util.*;

public class SudokuSolverDLS {

    // Define a constant for the maximum depth
    private static final int MAX_DEPTH = 81;

    // Check if the Sudoku board is valid
    public static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        // Check the 3x3 subgrid
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) return false;
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
        if (row == 9) return true;

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
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] board = {
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

        // Start the timer
        long startTime = System.nanoTime();

        // Solve the Sudoku puzzle
        if (solveDLS(board, 0, 0, 0)) {
            long endTime = System.nanoTime(); // End the timer
            long elapsedTime = endTime - startTime; // Calculate elapsed time
            System.out.println("Solved Sudoku:");
            printBoard(board); // Print solved board
            System.out.println("Execution Time: " + (elapsedTime / 1_000_000.0) + " ms");
        } else {
            long endTime = System.nanoTime(); // End the timer
            long elapsedTime = endTime - startTime; // Calculate elapsed time
            System.out.println("No solution found!");
            System.out.println("Execution Time: " + (elapsedTime / 1_000_000.0) + " ms");
        }
    }
}
