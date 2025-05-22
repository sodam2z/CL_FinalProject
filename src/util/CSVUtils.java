package util;

import java.io.*;
import java.util.*;

public class CSVUtils {
    public static List<String[]> readCSV(String filename) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            data.add(line.split(","));
        }

        br.close();
        return data;
    }

    public static void writeCSV(String filename, String[][] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] row : data) {
                // 쉼표로 조인해서 라인 만들기
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to write CSV: " + e.getMessage());
        }
    }
}