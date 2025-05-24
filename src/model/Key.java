package model;

/**
 * Represents a key that allows the hero to unlock and pass through locked doors.
 * Typically dropped by Trolls upon defeat.
 */
public class Key extends GameObject {

    /**
     * Returns the character symbol representing the key.
     * @return Unicode star symbol ✪ (U+272A)
     */
    @Override
    public char getSymbol() {
        return '\u272A'; // ✪
    }
}