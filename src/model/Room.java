package model;

import util.CSVUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Represents a single room in the game, composed of a 2D grid of cells.
 * Also maintains lists of all monsters and items in the room.
 */
public class Room {
    private int rows;
    private int cols;
    private Cell[][] grid;
    private String filename;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private List<GameObject> items = new ArrayList<>();

    /**
     * Constructs an empty room with the given dimensions.
     * @param rows number of rows
     * @param cols number of columns
     */
    public Room(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
    }

    /**
     * Loads a room from a CSV file, initializing the grid and registering monsters and items.
     * Supports extended symbols such as "d:room3.csv" or "D:room1.csv" for doors.
     * @param filename path to the CSV file
     * @return the loaded Room object, or null if an error occurred
     */
    public static Room loadFromCSV(String filename) {
        try {
            if (!filename.contains("/") && !filename.startsWith("sessions/")) {
                filename = "sessions/active_session/" + filename;
            }

            List<String[]> lines = CSVUtils.readCSV(filename);
            String[] sizeInfo = lines.get(0); // First line: size info
            int rows = Integer.parseInt(sizeInfo[0].trim());
            int cols = Integer.parseInt(sizeInfo[1].trim());

            Room room = new Room(rows, cols);
            room.filename = filename;

            for (int r = 0; r < rows; r++) {
                String[] row = lines.get(r + 1); // Grid data starts from second line
                for (int c = 0; c < cols; c++) {
                    String raw = row[c].trim(); // Read full string (e.g., "d:room3.csv")
                    GameObject obj = GameObjectFactory.createFromSymbol(raw, filename);
                    room.grid[r][c] = new Cell(obj, raw); 

                    // Register monster in list
                    if (obj instanceof Monster) {
                        room.monsters.add((Monster) obj);
                    }

                    // Register item in list (weapons, potions, keys)
                    if (obj instanceof Weapon || obj instanceof Potion || obj instanceof Key) {
                        room.items.add(obj);
                    }
                }
            }
            return room;

        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] File not found: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid row/col format in CSV: " + filename);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to load room: " + e.getMessage());
        }
        return null;
    }

    /**
     * Displays the current room grid in the console with borders and symbols.
     */
    public void displayRoom() {
        System.out.println(); // Blank line

        // Top border
        System.out.print("+");
        for (int i = 0; i < cols; i++) {
            System.out.print("---");
        }
        System.out.println("+");

        // Grid rows
        for (int r = 0; r < rows; r++) {
            System.out.print("|");
            for (int c = 0; c < cols; c++) {
                char symbol = grid[r][c].getSymbol();
                System.out.print(" " + symbol + " "); // Add spacing for alignment
            }
            System.out.println("|");
        }

        // Bottom border
        System.out.print("+");
        for (int i = 0; i < cols; i++) {
            System.out.print("---");
        }
        System.out.println("+");
    }

    /**
     * Places the hero into this room.
     * Priority:
     * 1. Use saved position if available and valid.
     * 2. Use @ position from CSV.
     * 3. Use (1,1) if empty.
     * 4. Use first available empty cell.
     * @param hero the hero to place
     */
    public void placeHero(Hero hero) {
        // 1. Use previously saved position (if available)
        int[] saved = hero.getSavedPosition(this.filename);
        if (saved != null) {
            int r = saved[0];
            int c = saved[1];
            if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c].isEmpty()) {
                hero.setPosition(r, c);
                grid[r][c].setObject(hero);
                return;
            }
        }

        // 2. Use @ position if exists
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameObject obj = grid[r][c].getObject();
                if (obj instanceof Hero) {
                    hero.setPosition(r, c);
                    grid[r][c].setObject(hero);
                    return;
                }
            }
        }

        // 3. Use (1,1) if empty
        if (rows > 1 && cols > 1 && grid[1][1].isEmpty()) {
            hero.setPosition(1, 1);
            grid[1][1].setObject(hero);
            return;
        }

        // 4. Use any empty cell
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].isEmpty()) {
                    hero.setPosition(r, c);
                    grid[r][c].setObject(hero);
                    return;
                }
            }
        }

        // If no placement was possible
        System.out.println("[WARNING] Could not place hero in room: " + filename);
    }

    /**
     * Saves the current state of the room into a CSV file.
     * This method ensures that the output is written to the session directory
     * It serializes the grid using each cell's original raw symbol
     */
    public void saveToCSV() {
        // Determine the session path for saving the file
        String savePath = filename;

        // If current file is not already in the session path, redirect it
        if (!filename.contains("sessions/active_session/")) {
            String baseName = new File(filename).getName();  // Extracts "room2.csv" from full path
            savePath = "sessions/active_session/" + baseName;
        }

        // Create a 2D string array to store room data
        String[][] data = new String[rows + 1][cols];

        // First row stores the size information
        data[0][0] = String.valueOf(rows);
        data[0][1] = String.valueOf(cols);

        // Fill in the grid data using the original raw symbols
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameObject obj = grid[r][c].getObject();
                String raw = grid[r][c].getOriginalSymbol();

                // Do not save the hero — they are placed dynamically when the room loads
                if (obj instanceof Hero) {
                    data[r + 1][c] = " ";
                } else if (obj != null) {
                    data[r + 1][c] = raw;
                //} else if (raw != null && !raw.trim().isEmpty()) {
                   // data[r + 1][c] = raw;
                } else {
                    data[r + 1][c] = " ";
                }
            }
        }

        // Write the CSV data to file
        util.CSVUtils.writeCSV(savePath, data);
    }

    // === Getters ===

    public Cell[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String getFileName() {
        return filename;
    }

    /**
     * Returns the list of monsters currently in the room.
     */
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Returns the list of items currently in the room.
     */
    public List<GameObject> getItems() {
        return items;
    }
}