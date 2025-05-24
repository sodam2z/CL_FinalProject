package model;

/**
 * Abstract base class for all objects that can be placed in a room cell.
 * Subclasses must implement the getSymbol() method for display purposes.
 */
public abstract class GameObject {

    /**
     * Returns the character symbol that visually represents the object on the grid.
     * Must be implemented by all subclasses.
     * @return a Unicode or ASCII character
     */
    public abstract char getSymbol();
}