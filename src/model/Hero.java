package model;

import app.Game;
import java.util.Scanner;

public class Hero extends GameObject {
    private int maxHp = 25;
    private int currentHp = 25;
    private Weapon weapon = null;
    private boolean hasKey = false;

    private int row; // 현재 위치
    private int col;

    public Hero() {
        this.row = 0;
        this.col = 0;
    }

    // getter/setter
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

    // 상태 출력
    public void printStats() {
        System.out.println("HP: " + currentHp + "/" + maxHp +
                " | Weapon: " + (weapon != null ? weapon.getName() : "None") +
                " | Key: " + (hasKey ? "Yes" : "No"));
    }

    // 이동 처리 (방 경계 확인 및 빈 공간 또는 아이템 있는 경우만 이동)
    public void move(char direction, Room room) {
        int newRow = row;
        int newCol = col;

        switch (direction) {
            case 'u': newRow--; break;
            case 'd': newRow++; break;
            case 'l': newCol--; break;
            case 'r': newCol++; break;
            default:
                System.out.println("Invalid direction.");
                return;
        }

        // 범위 검사
        if (newRow < 0 || newRow >= room.getRows() || newCol < 0 || newCol >= room.getCols()) {
            System.out.println("You can't move there.");
            return;
        }

        Cell[][] grid = room.getGrid();
        Cell targetCell = grid[newRow][newCol];
        GameObject obj = targetCell.getObject();

        // --- 💥 몬스터가 있다면 이동 불가 ---
        if (obj instanceof Monster) {
            System.out.println("A monster blocks your way!");
            return;
        }

        // --- 🍖 포션 자동 사용 ---
        if (obj instanceof Potion) {
            Potion potion = (Potion) obj;
            if (currentHp < maxHp) {
                int healed = potion.getHealAmount();
                currentHp = Math.min(maxHp, currentHp + healed);
                System.out.println("You drank a " + potion.getName() + " and restored " + healed + " HP!");
                targetCell.setObject(null);  // 포션 사용 후 사라짐
            } else {
                System.out.println("You're already at full health.");
            }
        }

        // --- 🗡 무기 습득 또는 교체 ---
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
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("y")) {
                    // 교체: 이전 무기를 바닥에 둠
                    targetCell.setObject(weapon);
                    weapon = newWeapon;
                    System.out.println("You switched weapons.");
                } else {
                    System.out.println("You kept your current weapon.");
                }
                scanner.close();
            }
        }

        // --- 🔑 열쇠 획득 ---
        if (obj instanceof Key) {
            hasKey = true;
            System.out.println("You picked up a key!");
            targetCell.setObject(null);
        }

        // --- 🚶 실제 이동 처리 ---
        grid[row][col].setObject(null); // 현재 자리 비우기
        row = newRow;
        col = newCol;
        grid[row][col].setObject(this); // 새 자리로 이동
        
        // --- 🚪 문 위에 도달한 경우 ---
        if (obj instanceof Door) {
            if (hasKey) {
                Door door = (Door) obj;
                System.out.println("You used the key and entered the next room: " + door.getTargetRoomFilename());

                // 현재 상태 저장하고 다음 방 로딩 요청
                room.saveToCSV(); // 방 저장 메서드는 따로 구현 필요
                Room nextRoom = Room.loadFromCSV(door.getTargetRoomFilename());

                // 히어로 위치 초기화 (room1.csv처럼)
                nextRoom.placeHero(this);  // 아래에 설명될 메서드
                Game.setCurrentRoom(nextRoom); // Game 클래스에서 현재 방 갱신

                return;
            } else {
                System.out.println("The door is locked. You need a key.");
                return;
            }
        }
    }

    public void attack(Room room) {
        if (weapon == null) {
            System.out.println("You have no weapon to attack with!");
            return;
        }
    
        Cell[][] grid = room.getGrid();
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} }; // 상하좌우
    
        boolean attacked = false;
    
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
    
            if (r >= 0 && r < room.getRows() && c >= 0 && c < room.getCols()) {
                GameObject obj = grid[r][c].getObject();
    
                if (obj instanceof Monster) {
                    Monster monster = (Monster) obj;
    
                    // 전투 메뉴 출력
                    System.out.println("You are next to a " + monster.getName() + " (HP: " + monster.getHp() + ")");
                    System.out.print("Do you want to attack? (y/n): ");
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();
    
                    if (input.equalsIgnoreCase("y")) {
                        // 공격 로직
                        monster.setHp(monster.getHp() - weapon.getDamage());
                        this.damage(monster.getDamage());
    
                        System.out.println("You attacked with " + weapon.getName() + " (Damage: " + weapon.getDamage() + ")");
                        System.out.println("Monster retaliated! You took " + monster.getDamage() + " damage.");
                        System.out.println("Your HP: " + currentHp + "/" + maxHp);
                        System.out.println("Monster HP: " + monster.getHp());
    
                        if (monster.isDead()) {
                            System.out.println("You defeated the " + monster.getName() + "!");
                            grid[r][c].setObject(null);
    
                            // Troll만 키를 드롭
                            if (monster.getName().equalsIgnoreCase("Troll")) {
                                grid[r][c].setObject(new Key());
                                System.out.println("The Troll dropped a key!");
                            }
                        }
    
                        attacked = true;
                        break; // 한 번에 한 마리만 공격
                    }
                    scanner.close();
                }
            }
        }
    
        if (!attacked) {
            System.out.println("There is no monster next to you.");
        }
    }    
}