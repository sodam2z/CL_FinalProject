package model;

/**
 * Represents a single cell in the game room grid.
 * Each cell may contain a GameObject, or be empty.
 */
public class Cell {
    private GameObject object;

    /**
     * Constructs a cell with a given GameObject.
     * @param object the object in the cell (can be null)
     */
    public Cell(GameObject object) {
        this.object = object;
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