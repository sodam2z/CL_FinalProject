package model;

import app.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents the player's controllable hero character.
 * Handles movement, combat, inventory (weapon and key), and status.
 */
public class Hero extends GameObject {
    private int maxHp = 25;
    private int currentHp = 25;
    private Weapon weapon = null;
    private boolean hasKey = false;

    private int row; // current row position
    private int col; // current column position

    // Stores the last known position per room
    private Map<String, int[]> roomPositions = new HashMap<>();

    public Hero() {
        this.row = 0;
        this.col = 0;
    }

    // === Basic Getters and Setters ===

    public int getHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public void setHp(int hp) { currentHp = Math.min(hp, maxHp); }
    public void damage(int amount) { currentHp = Math.max(0, currentHp - amount); }

    public Weapon getWeapon() { return weapon; }
    public void setWeapon(Weapon w) { this.weapon = w; }

    public boolean hasKey() { return hasKey; }
    public void obtainKey() { hasKey = true; }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public char getSymbol() {
        return '\u263A'; // ☺
    }

    /**
     * Prints the hero's current stats to the console.
     */
    public void printStats() {
        System.out.println("HP: " + currentHp + "/" + maxHp +
                " | Weapon: " + (weapon != null ? weapon.getName() : "None") +
                " | Key: " + (hasKey ? "Yes" : "No"));
    }

    /**
     * Records the hero's position in a specific room.
     * @param roomFilename the room file name
     * @param row the row position of the hero
     * @param col the column position of the hero
     */
    public void savePosition(String roomFilename, int row, int col) {
        roomPositions.put(roomFilename, new int[]{row, col});
    }

    /**
     * Retrieves the last known position of the hero in a room.
     * @param roomFilename the room file name
     * @return an int array [row, col] if saved, or null if not
     */
    public int[] getSavedPosition(String roomFilename) {
        return roomPositions.getOrDefault(roomFilename, null);
    }

    /**
     * Handles movement logic for the hero.
     * Interacts with items, doors, monsters, and performs bounds checking.
     * Handles automatic item pickup and room transitions.
     */
    public void move(char direction, Room room) {
        int newRow = row;
        int newCol = col;

        // Determine direction
        switch (direction) {
            case 'u': newRow--; break;
            case 'd': newRow++; break;
            case 'l': newCol--; break;
            case 'r': newCol++; break;
            default:
                System.out.println("Invalid direction.");
                return;
        }

        // Check bounds
        if (newRow < 0 || newRow >= room.getRows() || newCol < 0 || newCol >= room.getCols()) {
            System.out.println("You can't move there.");
            return;
        }

        Cell[][] grid = room.getGrid();
        Cell targetCell = grid[newRow][newCol];
        GameObject obj = targetCell.getObject();

        // === Door logic ===
        if (obj instanceof Door) {
            Door door = (Door) obj;
            String nextRoom = door.getTargetRoomFilename();

            // Save current position before moving
            roomPositions.put(room.getFileName(), new int[]{row, col});

            // Escape through Master Door
            if (door.requiresKey() && nextRoom.contains("room1")) {
                if (hasKey) {
                    System.out.println("You used the key and escaped the maze! Congratulations!");
                    System.exit(0); // End the game
                } else {
                    System.out.println("The Master Door is locked. You need a key to escape.");
                    return;
                }
            }

            // Regular door or valid master door entry
            if (!door.requiresKey() || hasKey) {
                System.out.println(
                    (door.requiresKey() ? "You used the key" : "You entered") +
                    " and moved to the next room: " + nextRoom
                );

                // Save current room state
                room.saveToCSV();

                // Load or retrieve the next room from cache
                Room nextRoomObj = Game.getRoom(nextRoom);
                if (nextRoomObj == null) {
                    System.out.println("[ERROR] Failed to load room: " + nextRoom);
                    return;
                }

                // Determine position for re-entry if known
                if (roomPositions.containsKey(nextRoom)) {
                    int[] pos = roomPositions.get(nextRoom);
                    this.setPosition(pos[0], pos[1]);
                    nextRoomObj.getGrid()[pos[0]][pos[1]].setObject(this);
                } else {
                    nextRoomObj.placeHero(this);
                }

                Game.setCurrentRoom(nextRoomObj);
                return;
            } else {
                System.out.println("The door is locked. You need a key.");
                return;
            }
        }

        // === Monster block check ===
        if (obj instanceof Monster) {
            System.out.println("A monster blocks your way!");
            return;
        }

        // === Potion auto-healing ===
        if (obj instanceof Potion) {
            Potion potion = (Potion) obj;
            if (currentHp < maxHp) {
                int healed = potion.getHealAmount();
                currentHp = Math.min(maxHp, currentHp + healed);
                System.out.println("You drank a " + potion.getName() + " and restored " + healed + " HP!");
                targetCell.setObject(null);  // Remove used potion
            } else {
                System.out.println("You're already at full health.");
            }
        }

        // === Weapon pickup or swap ===
        if (obj instanceof Weapon) {
            Weapon newWeapon = (Weapon) obj;
            if (weapon == null) {
                weapon = newWeapon;
                System.out.println("You picked up a " + newWeapon.getName() + "!");
                targetCell.setObject(null);
            } else {
                System.out.println("Found a " + newWeapon.getName() + ". Current weapon: " + weapon.getName());
                System.out.print("Do you want to switch? (y/n): ");
                Scanner scanner = new Scanner(System.in);
                String input = "";
                try {
                    input = scanner.nextLine().trim().toLowerCase();
                } catch (NoSuchElementException e) {
                    System.out.println("[ERROR] Unable to read your input.");
                    return;
                }

                if (input.equals("y")) {
                    // Drop current weapon and equip new one
                    targetCell.setObject(weapon);
                    weapon = newWeapon;
                    System.out.println("You switched weapons.");
                } else if (input.equals("n")) {
                    System.out.println("You kept your current weapon.");
                } else {
                    System.out.println("[ERROR] Invalid input. Weapon not switched.");
                }
            }
        }

        // === Key pickup ===
        if (obj instanceof Key) {
            hasKey = true;
            System.out.println("You picked up a key!");
            targetCell.setObject(null);
        }

        // === Perform movement ===
        grid[row][col].setObject(null); // Clear old position
        row = newRow;
        col = newCol;
        grid[row][col].setObject(this); // Move to new position
    }

    /**
     * Attacks a monster in an adjacent cell (up/down/left/right).
     * Damage is exchanged between hero and monster.
     * Handles monster death and Troll's key drop.
     */
    public void attack(Room room) {
        if (weapon == null) {
            System.out.println("You have no weapon to attack with!");
            return;
        }

        Cell[][] grid = room.getGrid();
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        boolean attacked = false;
        boolean inputInvalid = false;

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            // Check bounds
            if (r >= 0 && r < room.getRows() && c >= 0 && c < room.getCols()) {
                GameObject obj = grid[r][c].getObject();

                if (obj instanceof Monster) {
                    Monster monster = (Monster) obj;

                    // Prompt user to attack
                    System.out.println("You are next to a " + monster.getName() + " (HP: " + monster.getHp() + ")");
                    System.out.print("Do you want to attack? (y/n): ");
                    Scanner scanner = new Scanner(System.in);
                    String input = "";
                    try {
                        input = scanner.nextLine();
                    } catch (NoSuchElementException | IllegalStateException e) {
                        System.out.println("[ERROR] Could not read your response. Attack canceled.");
                        return;
                    }

                    if (input.equalsIgnoreCase("y")) {
                        // Attack exchange
                        monster.setHp(monster.getHp() - weapon.getDamage());
                        this.damage(monster.getDamage());

                        System.out.println("You attacked with " + weapon.getName() + " (Damage: " + weapon.getDamage() + ")");
                        System.out.println("Monster retaliated! You took " + monster.getDamage() + " damage.");
                        System.out.println("Your HP: " + currentHp + "/" + maxHp);
                        System.out.println("Monster HP: " + monster.getHp());

                        // If monster dies
                        if (monster.isDead()) {
                            System.out.println("You defeated the " + monster.getName() + "!");
                            grid[r][c].setObject(null);
                            room.getMonsters().remove(monster);

                            if (room.getMonsters().isEmpty()) {
                                System.out.println("🎉 You have cleared all monsters in this room!");
                            }

                            // Only Troll drops a key
                            if (monster.getName().equalsIgnoreCase("Troll")) {
                                grid[r][c].setObject(new Key());
                                System.out.println("The Troll dropped a key!");
                                room.getItems().add(grid[r][c].getObject());
                            }
                        }

                        attacked = true;
                        break;
                    } else if (input.equalsIgnoreCase("n")) {
                        System.out.println("You chose not to attack.");
                        attacked = true;
                        break;
                    } else {
                        System.out.println("[ERROR] Invalid input. Attack canceled.");
                        inputInvalid = true;
                        break;
                    }
                }
            }
        }

        if (!attacked && !inputInvalid) {
            System.out.println("There is no monster next to you.");
        }
    }
}