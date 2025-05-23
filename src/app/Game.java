package app;

import model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Game {
    private static Room currentRoom;
    private static Hero hero;

    public static void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void main(String[] args) {
        // 1. 세션 폴더 생성
        File sessionDir = new File("sessions/session_001");
        if (!sessionDir.exists()) {
            sessionDir.mkdirs();
        }

        // 2. 파일 복사
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

        // 3. 방 로딩 및 게임 시작
        currentRoom = Room.loadFromCSV("rooms/room1.csv"); // 초기 방
        hero = new Hero();
        currentRoom.placeHero(hero);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nAdventureGame");
            hero.printStats();
            getCurrentRoom().displayRoom();

            System.out.print("Enter command (u/d/l/r to move, a to attack, q to quit): ");
            String input = scanner.nextLine();

            switch (input) {
                case "u": case "d": case "l": case "r":
                    hero.move(input.charAt(0), currentRoom);
                    break;
                case "a":
                    hero.attack(Game.getCurrentRoom());
                    break;
                case "q":
                    System.out.println("Quitting the game.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command.");
            }

            // 종료 조건: 플레이어 사망
            if (hero.getHp() <= 0) {
                System.out.println("You died. Game Over.");
                running = false;
            }
        }

        scanner.close();
    }
}