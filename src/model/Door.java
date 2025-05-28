package model;

/**
 * Represents a door in the game that connects to another room.
 * There are two types of doors:
 * - Regular door ('d'): does NOT require a key.
 * - Master door ('D'): requires a key to pass through.
 */
public class Door extends GameObject {
    private String targetRoomFilename;
    private boolean requiresKey;

    /**
     * Constructs a door that links to another room.
     * @param targetRoomFilename the CSV file name of the target room
     * @param requiresKey whether the door requires a key to enter
     */
    public Door(String targetRoomFilename, boolean requiresKey) {
        this.targetRoomFilename = targetRoomFilename;
        this.requiresKey = requiresKey;
    }

    /**
     * Returns the filename of the room this door leads to.
     * @return the target room's CSV file name
     */
    public String getTargetRoomFilename() {
        return targetRoomFilename;
    }

    /**
     * Checks if this door requires a key.
     * @return true if the door requires a key, false otherwise
     */
    public boolean requiresKey() {
        return requiresKey;
    }

    /**
     * Returns the symbol representing a door on the grid.
     * @return Unicode character ☗
     */
    @Override
    public char getSymbol() {
        return '\u2617'; // ☗
    }
}