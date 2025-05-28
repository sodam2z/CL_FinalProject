package model;

/**
 * Factory class for creating GameObject instances from character symbols.
 * Used when loading room data from CSV files.
 */
public class GameObjectFactory {

    /**
     * Creates a GameObject based on the given symbol character.
     *
     * @param raw the raw string value from the CSV cell (e.g., "G", "S", "d:room3.csv")
     * @param currentRoomFile the current room filename, used for determining door target
     * @return a new GameObject instance, or null if symbol is unrecognized or empty
     */
    public static GameObject createFromSymbol(String raw, String currentRoomFile) {
        if (raw == null || raw.trim().isEmpty()) return null;
    
        // --- Door parsing ---
        if (raw.startsWith("d:")) return new Door(raw.substring(2).trim(), false);
        if (raw.equals("D")) return new Door("rooms/room1.csv", true);

        // --- Monster with HP parsing ---
        if (raw.matches("^[GOT]:\\d+$")) {
            char symbol = raw.charAt(0);
            int hp = Integer.parseInt(raw.split(":")[1]);

            switch (symbol) {
                case 'G': return new Monster("Goblin", hp, 1, 'G');
                case 'O': return new Monster("Orc", hp, 3, 'O');
                case 'T': return new Monster("Troll", hp, 4, 'T');
            }
        }
    
        // --- Standard symbols ---
        switch (raw) {
            case "@": return new Hero();
            case "S": return new Weapon("Stick", 1, 'S');
            case "W": return new Weapon("Weak Sword", 2, 'W');
            case "X": return new Weapon("Strong Sword", 3, 'X');
            case "m": return new Potion("Minor Flask", 6, 'm');
            case "B": return new Potion("Big Flask", 12, 'B');
            case "G": return new Monster("Goblin", 3, 1, 'G');
            case "O": return new Monster("Orc", 8, 3, 'O');
            case "T": return new Monster("Troll", 15, 4, 'T');
            case "*": return new Key();
            default: return null; // unknown symbol
        }
    }
}