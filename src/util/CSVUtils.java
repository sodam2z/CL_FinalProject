package util;

import java.io.*;
import java.util.*;

/**
 * Utility class for reading from and writing to CSV files.
 */
public class CSVUtils {

    /**
     * Reads a CSV file and returns the content as a list of String arrays.
     *
     * @param filename the path to the CSV file
     * @return list of rows, where each row is a String array of values
     * @throws IOException if the file cannot be read
     */
    public static List<String[]> readCSV(String filename) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        // Read each line and split it by commas
        while ((line = br.readLine()) != null) {
            data.add(line.split(","));
        }

        br.close(); // Close the file after reading
        return data;
    }

    /**
     * Writes a 2D string array to a CSV file.
     *
     * @param filename the path to the output CSV file
     * @param data     the 2D array to write
     */
    public static void writeCSV(String filename, String[][] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] row : data) {
                // Join each row's elements with commas
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine(); // Write a new line after each row
            }
            writer.flush(); // Ensure all data is written to the file
        } catch (IOException e) {
            System.out.println("Failed to write CSV: " + e.getMessage());
        }
    }
}