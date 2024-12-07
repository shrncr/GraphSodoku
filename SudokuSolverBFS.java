
/*
 * BFS Implementation of Sodoku Solver
 * Implementation based on https://github.com/erickfunier/sudoku-solver
 * Was originally in Python
 * Asked ChatGPT, "can you transform *program* from python to java"
 * Original transformation did not work
 * Changed board to 0's, returned program to Chat, Chat returned fixed program
 * Comments and majority implementation from chat
 */
import java.util.*;

public class SudokuSolverBFS {
    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static boolean isSolved(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    return false; // unsolved
                }
                if (!isValid(board, row, col, board[row][col])) {
                    return false; // not valid
                }
            }
        }
        return true;
    }

    public static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num && i != col)
                return false;
            if (board[i][col] == num && i != row)
                return false;
        }
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int r = startRow + i, c = startCol + j;
                if (board[r][c] == num && !(r == row && c == col))
                    return false;
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
        outerLoop: for (int row = 0; row < 9; row++) {
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
                boolean isDuplicate = solutions.stream()
                        .anyMatch(solution -> Arrays.deepEquals(solution, currentState));
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
        ArrayList<int[][]> boards = BoardParser.parseBoardsFromFile("boards.txt");
        long startTimeOverall = System.nanoTime();
        long startTimeTemp;
        long endTimeTemp;
        long elapsedTimeTemp;
        long overhead = 0;
        for (int[][] board : boards) {
            startTimeTemp = System.nanoTime();
            ArrayList<int[][]> solutions = solveBFS(board);
            endTimeTemp = System.nanoTime();
            if (solutions.size() > 0) {
                for (int[][] solvedBoard : solutions) {
                    printBoard(solvedBoard);
                }
            } else {
                System.out.println("No solution found!");

            }
            // End the timer
            elapsedTimeTemp = endTimeTemp - startTimeTemp;
            overhead += elapsedTimeTemp; // Calculate elapsed time
            System.out.println("Execution Time To Solve: " + (elapsedTimeTemp / 1_000_000.0) + " ms");
        }
        long endTimeOverall = System.nanoTime();
        long elapsedTimeOverall = endTimeOverall - startTimeOverall;
        System.out.println("Overall Execution Time for All Boards: " + (elapsedTimeOverall / 1_000_000.0) + " ms");
        System.out.println("Overall Execution Time to Solve all Boards No Overhead: "
                + (elapsedTimeOverall / 1_000_000.0) + " ms");

    }

}
