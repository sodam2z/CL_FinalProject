package model;

public class Door extends GameObject {
    private String targetRoomFilename;

    public Door(String targetRoomFilename) {
        this.targetRoomFilename = targetRoomFilename;
    }

    public String getTargetRoomFilename() {
        return targetRoomFilename;
    }

    @Override
    public char getSymbol() {
        return '\u2617'; // ☗
    }
}