package model;

import util.CSVUtils;
import java.util.*;

public class Room {
    private int rows;
    private int cols;
    private Cell[][] grid;
    private String filename;

    // 생성자
    public Room(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
    }

    public static Room loadFromCSV(String filename) {
        try {
            List<String[]> lines = CSVUtils.readCSV(filename);
            String[] sizeInfo = lines.get(0);  // 첫 줄: 크기 정보
            int rows = Integer.parseInt(sizeInfo[0].trim());
            int cols = Integer.parseInt(sizeInfo[1].trim());

            Room room = new Room(rows, cols);
            room.filename = filename;

            for (int r = 0; r < rows; r++) {
                String[] row = lines.get(r + 1);  // 두 번째 줄부터 방 내용
                for (int c = 0; c < cols; c++) {
                    char ch = row[c].charAt(0);
                    GameObject obj = GameObjectFactory.createFromSymbol(ch, filename);
                    room.grid[r][c] = new Cell(obj);
                }
            }
            return room;

        } catch (Exception e) {
            System.out.println("Failed to load room: " + e.getMessage());
            return null;
        }
    }

    public void displayRoom() {
        System.out.println(); // 빈 줄

        System.out.print("+");
        for (int i = 0; i < cols; i++) {
            System.out.print("---"); // 여백 넓히기용
        }
        System.out.println("+");

        // 내부 내용
        for (int r = 0; r < rows; r++) {
            System.out.print("|");
            for (int c = 0; c < cols; c++) {
                char symbol = grid[r][c].getSymbol();
                System.out.print(" " + symbol + " "); // ← 각 기호 뒤에 한 칸 공백 추가
            }
            System.out.println("|");
        }

        // 하단 테두리
        System.out.print("+");
        for (int i = 0; i < cols; i++) {
            System.out.print("---");
        }
        System.out.println("+");
    }

    public void placeHero(Hero hero) {
        boolean placed = false;

        // 1. 우선 CSV에서 @ 기호가 있었는지 찾아봄
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameObject obj = grid[r][c].getObject();
                if (obj instanceof Hero) {
                    // 이미 배치되어 있는 경우
                    hero.setPosition(r, c);
                    grid[r][c].setObject(hero); // 실제 Hero 인스턴스 덮어쓰기
                    placed = true;
                    return;
                }
            }
        }

        // 2. @ 기호가 없고 (1,1)이 비어 있다면
        if (!placed && rows > 1 && cols > 1 && grid[1][1].isEmpty()){
            hero.setPosition(1, 1);
            grid[1][1].setObject(hero);
            return;
        }

        // 3. 그 외 빈 칸 아무 곳이나 랜덤 배치
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].isEmpty()) {
                    hero.setPosition(r, c);
                    grid[r][c].setObject(hero);
                    return;
                }
            }
        }
    }

    public void saveToCSV() {
        String[][] data = new String[rows + 1][cols];
    
        // 첫 줄: 행과 열 정보
        data[0][0] = String.valueOf(rows);
        data[0][1] = String.valueOf(cols);
    
        // 그리드 정보
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                GameObject obj = grid[r][c].getObject();
                data[r + 1][c] = (obj != null) ? String.valueOf(obj.getSymbol()) : " ";
            }
        }
    
        // 파일로 저장
        util.CSVUtils.writeCSV(filename, data);
    }    

    public Cell[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}