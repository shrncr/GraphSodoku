import java.util.ArrayList;

/*
 * DFS on a preproccessed board of cells Implementation of Sodoku Solver
 * 
 * It creates a sudoku of empty cells then every time a number is added it eliminates cells around it
 * after all numbers have been added it solves using DFS and updates canidates as it goes
 */
public class Sudoku {
    Cell[][] board; // The 9x9 Sudoku board

    // Constructor to initialize the Sudoku board
    public Sudoku() {
        board = new Cell[9][9]; // Create a 9x9 array

        // Fill the board with empty Cell objects
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                board[row][col] = new Cell(); // Initialize each cell
            }
        }
    }

    // Copy constructor for Sudoku
    public Sudoku(Sudoku other) {
        this.board = copyBoard(other.board);
    }

    /**
     * Adds a number to the specified cell in the Sudoku board.
     */
    public void addNoEliminate(int n, int row, int col) {
        this.board[row][col].setNumber(n);
    }

    public void addNumber(int n, int row, int col) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            throw new IllegalArgumentException("Row and column must be between 0 and 8.");
        }
        if (n < 1 || n > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9.");
        }
        board[row][col].setNumber(n);
        eliminateCandidates(n, row, col);
    }

    private static Cell[][] addNumber(int n, int row, int col, Cell[][] board) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            throw new IllegalArgumentException("Row and column must be between 0 and 8.");
        }
        if (n < 1 || n > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9.");
        }
        board[row][col].setNumber(n);
        return eliminateCandidates(n, row, col, board);
    }

    private void eliminateCandidates(int n, int row, int col) {
        for (int i = 0; i < 9; i++) {
            board[row][i].removeCandidate(n);
            board[i][col].removeCandidate(n);
        }

        int startRow = 3 * (row / 3);
        int startCol = 3 * (col / 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (startRow + i != row || startCol + j != col) {
                    board[startRow + i][startCol + j].removeCandidate(n);
                }
            }
        }
    }

    private static Cell[][] eliminateCandidates(int n, int row, int col, Cell[][] board) {
        for (int i = 0; i < 9; i++) {
            board[row][i].removeCandidate(n);
            board[i][col].removeCandidate(n);
        }

        int startRow = 3 * (row / 3);
        int startCol = 3 * (col / 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (startRow + i != row || startCol + j != col) {
                    board[startRow + i][startCol + j].removeCandidate(n);
                }
            }
        }
        return board;
    }

    private boolean isSolved(Cell[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getNumber() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Cell[][] solveDFS() {

        this.board = solveDFS(this.board);

        return this.board;
    }

    private Cell[][] solveDFS(Cell[][] board) {
        // If the board is solved, return it
        if (isSolved(board)) {
            return board;
        }

        // Find the next empty cell (with the fewest candidates)
        int[] nextEmptyCell = findNextEmptyCell(board);
        if (nextEmptyCell == null) {
            return null; // No valid moves
        }

        int row = nextEmptyCell[0];
        int col = nextEmptyCell[1];
        int[] candidates = board[row][col].getCandidates();

        // Try placing each candidate
        for (int candidate : candidates) {
            Cell[][] newBoard = copyBoard(board); // Copy the board
            addNumber(candidate, row, col, newBoard); // Place the candidate

            // Recursively solve the board
            Cell[][] result = solveDFS(newBoard);
            if (result != null) {
                return result; // Return the solution if found
            }
        }

        // If no solution is found, backtrack
        return null;
    }

    private int[] findNextEmptyCell(Cell[][] board) {
        int minCandidates = 10;
        int[] bestCell = null;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getNumber() == null) {
                    int numCandidates = board[i][j].getCandidates().length;
                    if (numCandidates < minCandidates) {
                        minCandidates = numCandidates;
                        bestCell = new int[] { i, j };

                        // If a cell has only one candidate, we can prioritize it
                        if (minCandidates == 1) {
                            return bestCell;
                        }
                    }
                }
            }
        }

        return bestCell;
    }

    private static Cell[][] copyBoard(Cell[][] board) {
        Cell[][] copy = new Cell[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = new Cell(board[i][j]);
            }
        }
        return copy;
    }

    public void clearBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j].reset();
            }
        }
    }

    public void displayBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j].getNumber() == null ? ". " : board[i][j].getNumber() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        ArrayList<int[][]> boards = BoardParser.parseBoardsFromFile("boards.txt");
        long startTime;

        long numEnd;
        long solveEnd;

        long endTime;
        long nooverhead = 0;
        int count = 0;
        startTime = System.nanoTime();
        for (int[][] board : boards) {

            Sudoku sudoku = new Sudoku();
            System.out.println();
            System.out.println("Board " + count);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board[i][j] != 0)
                        sudoku.addNumber(board[i][j], i, j);
                }
            }

            numEnd = System.nanoTime();
            sudoku.solveDFS();
            solveEnd = System.nanoTime();
            sudoku.displayBoard();

            System.out.println("Time taken to solve: " + (solveEnd - numEnd) / 1_000_000.0 + " ms");
            nooverhead += (solveEnd - numEnd);

            count++;

        }
        endTime = System.nanoTime(); // End time measurement

        System.out.println("Time taken to execute all: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Time to solve no overhead:" + (nooverhead) / 1_000_000.0 + " ms");

    }
}
