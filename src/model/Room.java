package model;

import util.CSVUtils;

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
     * @param filename path to the CSV file
     * @return the loaded Room object, or null if an error occurred
     */
    public static Room loadFromCSV(String filename) {
        try {
            List<String[]> lines = CSVUtils.readCSV(filename);
            String[] sizeInfo = lines.get(0); // First line: size info
            int rows = Integer.parseInt(sizeInfo[0].trim());
            int cols = Integer.parseInt(sizeInfo[1].trim());

            Room room = new Room(rows, cols);
            room.filename = filename;

            for (int r = 0; r < rows; r++) {
                String[] row = lines.get(r + 1); // Grid data starts from second line
                for (int c = 0; c < cols; c++) {
                    char ch = row[c].charAt(0);
                    GameObject obj = GameObjectFactory.createFromSymbol(ch, filename);
                    room.grid[r][c] = new Cell(obj);

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
     * Places the hero into the room based on the following order of precedence:
     * 1. If '@' is found, place hero there.
     * 2. If cell (1,1) is empty, place hero there.
     * 3. Otherwise, place hero in the first available empty cell.
     * @param hero the hero to place
     */
    public void placeHero(Hero hero) {
        boolean placed = false;

        // 1. Check for '@' symbol in the room
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameObject obj = grid[r][c].getObject();
                if (obj instanceof Hero) {
                    hero.setPosition(r, c);
                    grid[r][c].setObject(hero);
                    placed = true;
                    return;
                }
            }
        }

        // 2. If (1,1) is empty, place hero there
        if (!placed && rows > 1 && cols > 1 && grid[1][1].isEmpty()) {
            hero.setPosition(1, 1);
            grid[1][1].setObject(hero);
            return;
        }

        // 3. Otherwise, find first available empty cell
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].isEmpty()) {
                    hero.setPosition(r, c);
                    grid[r][c].setObject(hero);
                    return;
                }
            }
        }
    }

    /**
     * Saves the current room state back to the CSV file.
     */
    public void saveToCSV() {
        String[][] data = new String[rows + 1][cols];

        // First line: size
        data[0][0] = String.valueOf(rows);
        data[0][1] = String.valueOf(cols);

        // Grid content
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameObject obj = grid[r][c].getObject();
                data[r + 1][c] = (obj != null) ? String.valueOf(obj.getSymbol()) : " ";
            }
        }

        CSVUtils.writeCSV(filename, data);
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