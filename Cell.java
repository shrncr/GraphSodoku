import java.util.Arrays;

public class Cell {
    private Integer number; // Use Integer to allow null values
    private int[] candidates; // Array of possible candidates

    // Default constructor initializes the cell with all candidates
    public Cell() {
        this.number = null; // No number assigned initially
        this.candidates = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }; // Full range of candidates
    }

    // Constructor to initialize a cell with a specific number
    public Cell(Integer number) {
        this.number = number;
        this.candidates = new int[0]; // No candidates when a number is already assigned
    }

    // COpy Constructor
    public Cell(Cell c) {
        this.number = c.getNumber();
        this.candidates = c.getCandidates();
    }

    public void reset() {
        this.number = null; // No number assigned initially
        this.candidates = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    // Getters and setters
    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        // this.candidates = new int[0]; // Clear candidates
    }

    public int[] getCandidates() {
        return candidates;
    }

    // Set the list of candidates
    private void setCandidates(int[] candidates) {
        this.candidates = candidates;
    }

    // Check if a value is a candidate for this cell
    public boolean isCandidate(int n) {
        for (int candidate : this.candidates) {
            if (candidate == n) {
                return true;
            }
        }
        return false;
    }

    // Remove a specific candidate
    public void removeCandidate(int candidate) {
        // If the candidate is not in the list, do nothing
        if (!isCandidate(candidate)) {
            return;
        }

        // Create a new array with the candidate removed
        int[] newCandidates = new int[this.candidates.length - 1];
        int index = 0;
        for (int c : this.candidates) {
            if (c != candidate) {
                newCandidates[index++] = c;
            }
        }
        this.candidates = newCandidates; // Update the candidates list
    }

    // Main method for testing
    public static void main(String[] args) {
        Cell cell = new Cell();
        System.out.println("Initial candidates: " + Arrays.toString(cell.getCandidates()));

        cell.removeCandidate(5);
        System.out.println("After removing 5: " + Arrays.toString(cell.getCandidates()));

        cell.removeCandidate(10);
        System.out.println("After attempting to remove 10: " + Arrays.toString(cell.getCandidates()));

        cell.setNumber(7);
        System.out.println("After setting number to 7: " + Arrays.toString(cell.getCandidates()));
        System.out.println("Cell number: " + cell.getNumber());

        Cell cellWithNumber = new Cell(3);
        System.out.println("Cell initialized with number 3: " + Arrays.toString(cellWithNumber.getCandidates()));
        System.out.println("Cell number: " + cellWithNumber.getNumber());

        cellWithNumber.removeCandidate(3);
        System.out.println("After attempting to remove candidate from cell with no candidates: "
                + Arrays.toString(cellWithNumber.getCandidates()));
    }
}
