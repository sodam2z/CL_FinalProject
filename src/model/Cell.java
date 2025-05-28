package model;

/**
 * Represents a single cell in the game room grid.
 * Each cell may contain a GameObject, or be empty.
 * It also stores the original symbol string used to recreate the object.
 */
public class Cell {
    private GameObject object;
    private String originalSymbol; 

    /**
     * Constructs a cell with a given GameObject.
     * @param object the object in the cell (can be null)
     * @param originalSymbol the raw string from the CSV representing this object
     */
    public Cell(GameObject object, String originalSymbol) {
        this.object = object;
        this.originalSymbol = originalSymbol;
    }

    /**
     * Returns the GameObject currently in the cell.
     * @return the object, or null if empty
     */
    public GameObject getObject() {
        return object;
    }

    /**
     * Sets or replaces the GameObject in the cell.
     * @param object the object to place in the cell
     */
    public void setObject(GameObject object) {
        this.object = object;
    }

    /**
     * Returns the original raw symbol string for this cell.
     * Used when saving the room back to CSV.
     * @return original symbol string (e.g., "W", "G:3", "d:room3.csv")
     */
    public String getOriginalSymbol() {
        return originalSymbol;
    }

    /**
     * Updates the original symbol string for this cell.
     * Should be called when the object changes significantly.
     * @param symbol the new original symbol to store
     */
    public void setOriginalSymbol(String symbol) {
        this.originalSymbol = symbol;
    }

    /**
     * Returns the display symbol for this cell.
     * If the cell contains an object, return its symbol.
     * If empty, return a space character.
     * @return character symbol for display
     */
    public char getSymbol() {
        return (object != null) ? object.getSymbol() : ' ';
    }

    /**
     * Checks if the cell is empty (no object inside).
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return object == null;
    }
}