/*MAIN FILE
 * New method which mimics typical in-person sudoku game play
 * Populates a graph with vertices of candidates
 * Continuously narrows down solution based on "Single Candidates," "Naked Pairs," and "Naked Groups"
 * Above functionalities found in Graph.java
 * Once candidates are narrowed down, we solve with DLS
 */
import java.util.*;

public class NovelMethod {

    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));     
        }
    }

    public static Integer[] possible = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    Set<Integer> BOARD_SIZE = new HashSet<>(Arrays.asList(possible));
    public static ArrayList<int[][]> boards = BoardParser.parseBoardsFromFile("boards.txt");

    public static HashSet<Integer> CreateCandidates(int row, int col, int[] boxID, int[][] board) {
        HashSet<Integer> candidates = new HashSet(Arrays.asList(possible));
        int[] colItems = new int[9];
        ArrayList<Integer> boxItems = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            colItems[i] = board[i][col];
        }
        for (int i = boxID[0] * 3; i < boxID[0] * 3 + 3; i++) {
            for (int j = boxID[1] * 3; j < boxID[1] * 3 + 3; j++) {
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
        long totalSolvingTime = 0;
        long endTime;
        long elapsedTime;
        long startTime;
        for (int[][] board : boards) {

            System.out.println("Solving new board:");
            printBoard(board);
            HashSet<Integer> cand;
            Graph CandidateGraph = new Graph();
            int[] boxID = new int[2];

            // Build the graph for the current board
            for (int i = 0; i < board.length; i++) {
                int rowIndex = i / 3; // Determine box row
                boxID[0] = rowIndex;
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 0) {
                        int colIndex = j / 3; // Determine box column
                        boxID[1] = colIndex;
                        cand = CreateCandidates(i, j, boxID, board);
                        CandidateGraph.add(i, j, boxID, new Vertex(i, j, boxID, cand));
                    }
                }
            }
            startTime = System.nanoTime();
            // Solve the Sudoku
            boolean changesMade;
            while (!CandidateGraph.isEmpty()) {
                ArrayList<int[]> solutions = CandidateGraph.eliminateSingleCandidates(); // Solve single candidates
                if (solutions.isEmpty()) { // If no single candidates, attempt naked groups
                    changesMade = false;
                    for (int groupSize = 2; groupSize <= 4; groupSize++) { // Handle pairs, trios, etc.
                        if (CandidateGraph.eliminateNakedGroups(groupSize)) {
                            // Debug check to see how many groups deep it had to go
                            changesMade = true;
                            break; // Re-evaluate the graph
                        }
                    }
                    if (!changesMade) {
                        break; // Exit loop if no progress is made
                    }
                } else {
                    for (int[] solution : solutions) {
                        board[solution[0]][solution[1]] = solution[2]; // Apply solutions to the board
                    }
                }
            }

            // Verify the solution with BFS or DLS
            if (!SudokuSolverBFS.isSolved(board)) {
                SudokuSolverDLS.solveDLS(board, 0, 0, 0); // Attempt depth-limited search if unsolved
            }

            // Print the solved board and timing
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
            System.out.println("Solved board:");
            printBoard(board);
            totalSolvingTime += elapsedTime;
            System.out.println("Elapsed Time for board solve " + (elapsedTime / 1_000_000.0) + " ms");
        }

        System.out.println("Overall Execution Time for All Boards (excluding overhead): "
                + (totalSolvingTime / 1_000_000.0) + " ms");

    }
}
