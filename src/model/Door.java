package model;

/**
 * Represents a door in the game that connects to another room.
 * Each door stores the filename of the next room it leads to.
 */
public class Door extends GameObject {
    private String targetRoomFilename;

    /**
     * Constructs a door that links to another room.
     * @param targetRoomFilename the CSV file name of the target room
     */
    public Door(String targetRoomFilename) {
        this.targetRoomFilename = targetRoomFilename;
    }

    /**
     * Returns the filename of the room this door leads to.
     * @return the target room's CSV file name
     */
    public String getTargetRoomFilename() {
        return targetRoomFilename;
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