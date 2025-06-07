package app;

import model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Main class for launching the AdventureGame.
 * Handles session creation, file copying, and game loop control.
 */
public class Game {
    private static Room currentRoom;  // The currently active room
    private static Hero hero;         // The player's hero
    private static Map<String, Room> roomCache = new HashMap<>(); // Cached rooms to preserve state


    /**
     * Sets the current room.
     * @param room the Room object to set as current
     */
    public static void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    /**
     * Returns the current active room.
     * @return the Room the hero is currently in
     */
    public static Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Retrieves a Room from cache or loads it from file if not yet loaded.
     * This preserves room state (e.g., items, monsters) across visits.
     *
     * @param filename full path to the room CSV file
     * @return the Room instance (from cache or loaded fresh)
     */
    public static Room getRoom(String filename) {
        if (!roomCache.containsKey(filename)) {
            Room loaded = Room.loadFromCSV(filename);
            if (loaded != null) {
                roomCache.put(filename, loaded);
            }
        }
        return roomCache.get(filename);
    }

    /**
     * Entry point of the AdventureGame.
     * Initializes session files, loads first room, and starts the main game loop.
     */
    public static void main(String[] args) {
        // Create session directory (if it doesn't exist)
        File sessionDir = new File("sessions/active_session");
        if (!sessionDir.exists()) {
            boolean created = sessionDir.mkdirs();
            if (!created) {
                System.out.println("[ERROR] Failed to create session directory. Exiting the game.");
                return;
            }
        }

        // Copy original room files into the session directory
        String[] roomFiles = { "room1.csv", "room2.csv", "room3.csv", "room4.csv" };
        for (String file : roomFiles) {
            Path source = Paths.get("rooms", file);
            Path dest = Paths.get("sessions/active_session/", file);
            try {
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("[ERROR] Could not copy " + file + ". Please make sure the file exists in the 'rooms' folder.");
                return; // Exit if any room file cannot be copied
            }
        }

        // Load the initial room and place the hero
        currentRoom = Room.loadFromCSV("sessions/active_session/room1.csv");
        if (currentRoom == null) {
            System.out.println("[ERROR] Failed to load the initial room. Make sure 'room1.csv' is valid.");
            return;
        }

        hero = new Hero();
        currentRoom.placeHero(hero);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Main game loop
        while (running) {
            System.out.println("\nAdventureGame");
            hero.printStats();                // Display HP, weapon, and key status
            getCurrentRoom().displayRoom();   // Render the current room

            System.out.print("Enter command (u/d/l/r to move, a to attack, q to quit): ");
            String input = "";

            try {
                if (scanner.hasNextLine()) {
                    input = scanner.nextLine().trim();
                } else {
                    System.out.println("[ERROR] No input detected. Exiting game.");
                    break;
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("[ERROR] Problem reading input. Exiting game.");
                break;
            }

            // Process player command
            switch (input) {
                case "u": case "d": case "l": case "r":
                    hero.move(input.charAt(0), currentRoom);  // Move in specified direction
                    break;
                case "a":
                    hero.attack(Game.getCurrentRoom());        // Attack adjacent monster
                    break;
                case "q":
                    System.out.println("Quitting the game.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }

            // Game ends if hero dies
            if (hero.getHp() <= 0) {
                System.out.println("You died. Game Over.");
                running = false;
            }
        }

        scanner.close(); // Clean up scanner
    }
}