package model;

/**
 * Factory class for creating GameObject instances from character symbols.
 * Used when loading room data from CSV files.
 */
public class GameObjectFactory {

    /**
     * Creates a GameObject based on the given symbol character.
     *
     * @param ch character representing the object (e.g., 'G' for Goblin, 'S' for Stick)
     * @param currentRoomFile the current room filename, used for determining door target
     * @return a new GameObject instance, or null if symbol is unrecognized or empty
     */
    public static GameObject createFromSymbol(char ch, String currentRoomFile) {
        switch (ch) {
            case '@': return new Hero(); // Hero starting point
            case 'S': return new Weapon("Stick", 1, 'S'); // Weakest weapon
            case 'W': return new Weapon("Weak Sword", 2, 'W'); // Mid-tier weapon
            case 'X': return new Weapon("Strong Sword", 3, 'X'); // Strongest weapon
            case 'm': return new Potion("Minor Flask", 6, 'm'); // Small potion
            case 'B': return new Potion("Big Flask", 12, 'B'); // Large potion
            case 'G': return new Monster("Goblin", 3, 1, 'G'); // Weak monster
            case 'O': return new Monster("Orc", 8, 3, 'O'); // Stronger monster
            case 'T': return new Monster("Troll", 15, 4, 'T'); // Strongest monster, drops key
            case 'D': return new Door(calculateNextRoom(currentRoomFile)); // Door to next room
            case '*': return new Key(); // Key object
            case ' ': return null; // Empty space
            default:  return null; // Unrecognized character
        }
    }

    /**
     * Determines the next room to link to based on the current room's filename.
     * Used specifically when creating Door objects.
     *
     * @param currentRoomFile the name of the current room CSV file
     * @return the filename of the next room
     */
    private static String calculateNextRoom(String currentRoomFile) {
        if (currentRoomFile.contains("room1")) return "rooms/room2.csv";
        if (currentRoomFile.contains("room2")) return "rooms/room3.csv";
        if (currentRoomFile.contains("room3")) return "rooms/room4.csv";
        return "rooms/room1.csv"; // Final door loops back or ends the game
    }
}