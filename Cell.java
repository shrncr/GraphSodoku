import java.util.Arrays;

public class Cell {
    public Integer number; // Use Integer to allow null values
    public int[] candidates; // Correct spelling from "canidates" to "candidates"

    public Cell() {
        this.number = null; // Use null for an uninitialized state
        setCandidates(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }); // Proper array initialization
    }

    public Cell(Integer n) {
        if (isCanidate(n)) {
            this.number = n;
            setCandidates(new int[0]); // No candidates if the cell already has a number
        }
    }

    private void setCandidates(int[] c) {
        this.candidates = c;
    }

    private boolean isCanidate(int n) {
        for (int i = 0; i < this.candidates.length; i++) {
            if (this.candidates[i] == n) {
                return true;
            }
        }
        return false;
    }

    public void setNumber(int n) {
        this.number = n;
        setCandidates(new int[0]);
    }

    public void removeCandidate(int c) {
        // Check if the candidate exists in the array
        boolean found = false;
        for (int candidate : this.candidates) {
            if (candidate == c) {
                found = true;
                break;
            }
        }

        // If the candidate is not found, do nothing
        if (!found) {
            return;
        }

        // Create a new array with a reduced size
        int[] newCandidates = new int[this.candidates.length - 1];
        int index = 0;
        // Copy all candidates except the one to be removed
        for (int i = 0; i < this.candidates.length; i++) {
            if (this.candidates[i] != c) {
                newCandidates[index++] = this.candidates[i];
            }
        }

        // Update the candidates array
        setCandidates(newCandidates);
    }

    public static void main(String[] args) {
        // Initialize a cell and print initial candidates
        Cell cell = new Cell();
        System.out.println("Initial candidates: " + Arrays.toString(cell.candidates));

        // Test removing an existing candidate
        cell.removeCandidate(5);
        System.out.println("After removing 5: " + Arrays.toString(cell.candidates));

        // Test removing a non-existing candidate
        cell.removeCandidate(10);
        System.out.println("After attempting to remove 10: " + Arrays.toString(cell.candidates));

        // Test setting the number, which clears candidates
        cell.setNumber(7);
        System.out.println("After setting number to 7: " + Arrays.toString(cell.candidates));
        System.out.println("Cell number: " + cell.number);

        // Test initializing a cell with a number
        Cell cellWithNumber = new Cell(3);
        System.out.println("Cell initialized with number 3: " + Arrays.toString(cellWithNumber.candidates));
        System.out.println("Cell number: " + cellWithNumber.number);

        // Test removing a candidate on a cell with no candidates
        cellWithNumber.removeCandidate(3);
        System.out.println("After attempting to remove candidate from cell with no candidates: "
                + Arrays.toString(cellWithNumber.candidates));
    }
}
