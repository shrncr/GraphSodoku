import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BoardParser {
    
    public static ArrayList<int[][]> parseBoardsFromFile(String filename) {
        ArrayList<int[][]> boards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int[][] board = new int[9][9];
            int row = 0;

            while ((line = br.readLine()) != null) {
                // Trim extra spaces and handle empty lines (separators for new boards)
                line = line.trim();
                if (line.isEmpty()) {
                    // If we encounter an empty line, it means we've reached the end of a board
                    if (row == 9) {
                        boards.add(board);
                    }
                    board = new int[9][9]; // Reset for the next board
                    row = 0;
                    continue;
                }

                // Remove square brackets and split the line by commas
                line = line.replaceAll("[\\[\\]]", "");
                String[] numbers = line.split(",");

                // Convert each number from String to int
                for (int col = 0; col < numbers.length; col++) {
                    board[row][col] = Integer.parseInt(numbers[col].trim());
                }
                row++;
            }

            // After the loop, add the last board if it was read completely
            if (row == 9) {
                boards.add(board);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number: " + e.getMessage());
        }
        
        return boards;
    }

    public static void main(String[] args) {
        ArrayList<int[][]> boards = parseBoardsFromFile("boards.txt");
        // Print boards to verify the parsing
        for (int[][] board : boards) {
            for (int[] row : board) {
                for (int num : row) {
                    System.out.print(num + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
