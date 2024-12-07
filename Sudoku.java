import java.util.ArrayList;

/*
 * DFS on a preprocessed board of cells - Implementation of Sudoku Solver
 * 
 * This program creates a Sudoku board with empty cells, then every time a number is added, it eliminates the possible candidates for the cells in its row, column, and 3x3 grid.
 * After all numbers have been added to the board, it solves the puzzle using DFS (Depth First Search) and updates candidate options for each cell as it goes.
 */
public class Sudoku {
    Cell[][] board; // The 9x9 Sudoku board

    // Constructor to initialize the Sudoku board
    public Sudoku() {
        board = new Cell[9][9]; // Create a 9x9 array

        // Fill the board with empty Cell objects
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                board[row][col] = new Cell(); // Initialize each cell as empty
            }
        }
    }

    // Copy constructor for Sudoku
    public Sudoku(Sudoku other) {
        this.board = copyBoard(other.board); // Create a deep copy of the board
    }

    /**
     * Adds a number to the specified cell in the Sudoku board without eliminating
     * other candidates.
     */
    public void addNoEliminate(int n, int row, int col) {
        this.board[row][col].setNumber(n); // Set the number in the specified cell
    }

    /**
     * Adds a number to the specified cell in the Sudoku board and eliminates the
     * number as a candidate
     * in the same row, column, and 3x3 box.
     */
    public void addNumber(int n, int row, int col) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            throw new IllegalArgumentException("Row and column must be between 0 and 8.");
        }
        if (n < 1 || n > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9.");
        }
        board[row][col].setNumber(n); // Set the number in the specified cell
        eliminateCandidates(n, row, col); // Eliminate the number as a candidate in the row, column, and box
    }

    /**
     * Static method to add a number to the specified cell in the board (for use
     * with a copied board).
     * This method also eliminates candidates in the related cells.
     */
    private static Cell[][] addNumber(int n, int row, int col, Cell[][] board) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            throw new IllegalArgumentException("Row and column must be between 0 and 8.");
        }
        if (n < 1 || n > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9.");
        }
        board[row][col].setNumber(n); // Set the number in the specified cell
        return eliminateCandidates(n, row, col, board); // Eliminate candidates in the related cells
    }

    /**
     * Eliminates a number as a possible candidate from the same row, column, and
     * 3x3 grid of the given cell.
     */
    private void eliminateCandidates(int n, int row, int col) {
        for (int i = 0; i < 9; i++) {
            board[row][i].removeCandidate(n); // Remove candidate from same row
            board[i][col].removeCandidate(n); // Remove candidate from same column
        }

        // Eliminate candidates from the 3x3 grid that contains the cell
        int startRow = 3 * (row / 3); // Starting row of the 3x3 grid
        int startCol = 3 * (col / 3); // Starting column of the 3x3 grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (startRow + i != row || startCol + j != col) {
                    board[startRow + i][startCol + j].removeCandidate(n); // Eliminate candidate from 3x3 box
                }
            }
        }
    }

    /**
     * Static version of the eliminateCandidates method used for the board copy.
     */
    private static Cell[][] eliminateCandidates(int n, int row, int col, Cell[][] board) {
        for (int i = 0; i < 9; i++) {
            board[row][i].removeCandidate(n); // Remove candidate from same row
            board[i][col].removeCandidate(n); // Remove candidate from same column
        }

        // Eliminate candidates from the 3x3 grid that contains the cell
        int startRow = 3 * (row / 3); // Starting row of the 3x3 grid
        int startCol = 3 * (col / 3); // Starting column of the 3x3 grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (startRow + i != row || startCol + j != col) {
                    board[startRow + i][startCol + j].removeCandidate(n); // Eliminate candidate from 3x3 box
                }
            }
        }
        return board;
    }

    /**
     * Checks if the Sudoku board is fully solved.
     */
    private boolean isSolved(Cell[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getNumber() == null) {
                    return false; // If any cell is still empty, the board is not solved
                }
            }
        }
        return true; // All cells are filled, the board is solved
    }

    /**
     * Starts the DFS algorithm to solve the Sudoku puzzle.
     */
    public Cell[][] solveDFS() {
        this.board = solveDFS(this.board); // Solve using DFS
        return this.board;
    }

    /**
     * Solves the Sudoku puzzle using Depth First Search (DFS) by backtracking.
     * Tries each candidate for the next empty cell and proceeds recursively.
     */
    private Cell[][] solveDFS(Cell[][] board) {
        // If the board is solved, return it
        if (isSolved(board)) {
            return board;
        }

        // Find the next empty cell (with the fewest candidates)
        int[] nextEmptyCell = findNextEmptyCell(board);
        if (nextEmptyCell == null) {
            return null; // No valid moves, backtrack
        }

        int row = nextEmptyCell[0];
        int col = nextEmptyCell[1];
        int[] candidates = board[row][col].getCandidates(); // Get candidates for the next empty cell

        // Try placing each candidate
        for (int candidate : candidates) {
            Cell[][] newBoard = copyBoard(board); // Copy the board to simulate placing the candidate
            addNumber(candidate, row, col, newBoard); // Place the candidate in the cell

            // Recursively solve the board
            Cell[][] result = solveDFS(newBoard);
            if (result != null) {
                return result; // Return the solution if found
            }
        }

        // If no solution is found, backtrack
        return null;
    }

    /**
     * Finds the next empty cell on the board that has the fewest candidates.
     * If there is a cell with only one candidate, it is prioritized.
     */
    private int[] findNextEmptyCell(Cell[][] board) {
        int minCandidates = 10; // Start with a high number of candidates
        int[] bestCell = null;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getNumber() == null) { // If the cell is empty
                    int numCandidates = board[i][j].getCandidates().length; // Get the number of candidates for the cell
                    if (numCandidates < minCandidates) {
                        minCandidates = numCandidates; // Update the minimum number of candidates
                        bestCell = new int[] { i, j }; // Update the best cell

                        // If a cell has only one candidate, return it immediately
                        if (minCandidates == 1) {
                            return bestCell;
                        }
                    }
                }
            }
        }

        return bestCell; // Return the cell with the fewest candidates
    }

    /**
     * Creates a deep copy of the Sudoku board.
     */
    private static Cell[][] copyBoard(Cell[][] board) {
        Cell[][] copy = new Cell[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = new Cell(board[i][j]); // Create a new cell from the existing one
            }
        }
        return copy;
    }

    /**
     * Clears the Sudoku board, resetting all cells to empty.
     */
    public void clearBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j].reset(); // Reset each cell to empty
            }
        }
    }

    /**
     * Displays the Sudoku board in a readable format.
     */
    public void displayBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j].getNumber() == null ? ". " : board[i][j].getNumber() + " "); // Print
                                                                                                          // number or
                                                                                                          // "." if
                                                                                                          // empty
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        ArrayList<int[][]> boards = BoardParser.parseBoardsFromFile("boards.txt"); // Parse boards from file
        long startTime;

        long numEnd;
        long solveEnd;

        long endTime;
        long nooverhead = 0;
        int count = 0;
        startTime = System.nanoTime(); // Start time measurement
        for (int[][] board : boards) {

            Sudoku sudoku = new Sudoku();
            System.out.println();
            System.out.println("Board " + count);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board[i][j] != 0)
                        sudoku.addNumber(board[i][j], i, j); // Add initial numbers to the board
                }
            }

            numEnd = System.nanoTime(); // Record the time when adding numbers is done
            sudoku.solveDFS(); // Solve the Sudoku puzzle
            solveEnd = System.nanoTime(); // Record the time after solving

            sudoku.displayBoard(); // Display the solved board

            System.out.println("Time taken to solve: " + (solveEnd - numEnd) / 1_000_000.0 + " ms"); // Print solving
                                                                                                     // time
            nooverhead += (solveEnd - numEnd);

            count++;

        }
        endTime = System.nanoTime(); // End time measurement

        // Print overall execution time
        System.out.println("Time taken to execute all: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Time to solve no overhead:" + (nooverhead) / 1_000_000.0 + " ms");

    }
}
