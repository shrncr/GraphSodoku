import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public static Cell[][] addNumber(int n, int row, int col, Cell[][] board) {
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
        int minCandidates = Integer.MAX_VALUE;
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

    public ArrayList<Cell[][]> solveBFS() {
        return solveBFS(this.board);
    }

    private ArrayList<Cell[][]> solveBFS(Cell[][] board) {
        long startTime = System.nanoTime(); // Start time measurement

        Queue<Cell[][]> queue = new LinkedList<>();
        queue.add(copyBoard(board));
        ArrayList<Cell[][]> solutions = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            Cell[][] currentState = queue.poll();

            String serialized = serializeBoard(currentState);
            if (visited.contains(serialized))
                continue;
            visited.add(serialized);

            if (isSolved(currentState)) {
                solutions.add(copyBoard(currentState));
                continue;
            }

            queue.addAll(generateNextStates(currentState));
        }

        long endTime = System.nanoTime(); // End time measurement
        long elapsedTime = endTime - startTime; // Calculate elapsed time in nanoseconds
        System.out.println("Time taken to solve: " + elapsedTime / 1_000_000.0 + " ms");

        return solutions;
    }

    public static List<Cell[][]> generateNextStates(Cell[][] board) {
        List<Cell[][]> nextStates = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getNumber() == null) {
                    int[] candidates = board[i][j].getCandidates();
                    for (int candidate : candidates) {
                        Cell[][] copy = copyBoard(board);
                        addNumber(candidate, i, j, copy);
                        nextStates.add(copy);
                    }
                    return nextStates;
                }
            }
        }
        return nextStates;
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

    private String serializeBoard(Cell[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(board[i][j].getNumber() == null ? "0" : board[i][j].getNumber());
            }
        }
        return sb.toString();
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
        long numStart;
        long numEnd;
        long solveEnd;
        long displayEnd;
        long endTime;
        long nooverhead = 0;
        int count = 0;
        startTime = System.nanoTime();
        for (int[][] board : boards) {
            numStart = System.nanoTime();
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
            displayEnd = System.nanoTime();
            System.out.println("Time taken to add all numbers: " + (numEnd - numStart) / 1_000_000.0 + " ms");
            System.out.println("Time taken to solve: " + (solveEnd - numEnd) / 1_000_000.0 + " ms");
            nooverhead += (solveEnd - numEnd);
            System.out.println("Time taken to display: " + (displayEnd - solveEnd) / 1_000_000.0 + " ms");
            System.out.println("Time for board: " + (displayEnd - numStart) / 1_000_000.0 + " ms");

            count++;

        }
        endTime = System.nanoTime(); // End time measurement

        System.out.println("Time taken to execute all: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Time to solve no overhead:" + (nooverhead) / 1_000_000.0 + " ms");

    }
}
