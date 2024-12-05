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

    /**
     * Adds a number to the specified cell in the Sudoku board.
     *
     * @param n   The number to add (1-9)
     * @param row The row index (0-8)
     * @param col The column index (0-8)
     */
    public void addNumber(int n, int row, int col) {
        // Validate the row and column indices
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            throw new IllegalArgumentException("Row and column must be between 0 and 8.");
        }
        // Validate the number to be between 1 and 9
        if (n < 1 || n > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9.");
        }
        // Set the number in the specified cell
        board[row][col].setNumber(n);
        eliminateCandidates(n, row, col);
    }

    /**
     * Eliminates candidates from the row, column, and 3x3 subgrid of the specified
     * cell.
     *
     * @param n   The number to eliminate
     * @param row The row index of the cell
     * @param col The column index of the cell
     */
    private void eliminateCandidates(int n, int row, int col) {
        // Eliminate from row and column
        for (int i = 0; i < 9; i++) {
            board[row][i].removeCandidate(n); // Remove from row
            board[i][col].removeCandidate(n); // Remove from column
        }

        // Eliminate from the 3x3 subgrid
        int startRow = 3 * (row / 3); // Get the starting row of the subgrid
        int startCol = 3 * (col / 3); // Get the starting column of the subgrid

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Only remove candidates in the subgrid that are not the cell itself
                if (startRow + i != row || startCol + j != col) {
                    board[startRow + i][startCol + j].removeCandidate(n);
                }
            }
        }
    }

    private boolean isSolved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].number == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Cell[][] solve() {
        if (isSolved()) {
            return board;
        }
        return solve(this.board, 1);
    }

    private Cell[][] solve(Cell[][] board, int match) {

        return board;
    }

    // Method to display the board for testing
    public void displayBoard() {
        boolean SHOWCANIDATES = false;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Print cell numbers or candidates
                if (board[row][col].number == null) {
                    if (SHOWCANIDATES == true) {
                        System.out.print("[");
                        // Print candidates for the cell
                        for (int candidate : board[row][col].candidates) {
                            System.out.print(candidate + " ");
                        }
                        System.out.print("] ");
                    } else {
                        System.out.print(". ");
                    }
                } else {
                    // Print the number in the cell
                    System.out.print(board[row][col].number + " ");
                }
            }
            System.out.println(); // New line after each row
        }
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku(); // Create a Sudoku board

        // Test adding a number to the board
        sudoku.addNumber(5, 0, 0);
        // Add the number 5 at row 0, column 0
        sudoku.displayBoard(); // Display the board after adding the number
    }
}
