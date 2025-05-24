package app;

import model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main class for launching the AdventureGame.
 * Handles session creation, file copying, and game loop control.
 */
public class Game {
    private static Room currentRoom;  // The currently active room
    private static Hero hero;         // The player's hero

    public static void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void main(String[] args) {
        // 1. Create session directory (if it doesn't exist)
        File sessionDir = new File("sessions/session_001");
        if (!sessionDir.exists()) {
            sessionDir.mkdirs();
        }

        // 2. Copy original room files into the session directory
        String[] roomFiles = { "room1.csv", "room2.csv", "room3.csv", "room4.csv" };
        for (String file : roomFiles) {
            Path source = Paths.get("rooms", file);
            Path dest = Paths.get("sessions/active_session/", file);
            try {
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("[ERROR] Failed to copy " + file + ": " + e.getMessage());
            }
        }

        // 3. Load the initial room and place the hero
        currentRoom = Room.loadFromCSV("rooms/room1.csv");
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
                input = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("[ERROR] Input stream closed unexpectedly.");
                return;
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
                    System.out.println("Invalid command.");
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