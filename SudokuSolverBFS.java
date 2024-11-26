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
        int[][] board = {
            {0, 0, 0, 0, 0, 0, 5, 0, 0},
            {0, 0, 5, 2, 7, 6, 9, 8, 0},
            {0, 4, 9, 0, 0, 0, 0, 3, 0},
            {0, 0, 0, 0, 0, 8, 0, 7, 0},
            {0, 0, 4, 1, 6, 0, 0, 0, 0},
            {0, 0, 0, 5, 0, 0, 0, 0, 3},
            {0, 9, 0, 0, 0, 0, 0, 0, 0},
            {4, 7, 0, 0, 1, 0, 0, 0, 0},
            {3, 5, 8, 0, 0, 9, 0, 0, 0}
        };

        int[][] solvedBoard = solveBFS(board);
        if (solvedBoard != null) {
            for (int[] row : solvedBoard) {
                System.out.println(Arrays.toString(row));
            }
        } else {
            System.out.println("No solution found!");
        }
    }
}
