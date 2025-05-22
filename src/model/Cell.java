package model;

public class Cell {
    private GameObject object;

    public Cell(GameObject object) {
        this.object = object;
    }

    public GameObject getObject() {
        return object;
    }

    public void setObject(GameObject object) {
        this.object = object;
    }

    public char getSymbol() {
        // 객체가 있으면 그 객체의 심볼 반환, 없으면 공백
        return (object != null) ? object.getSymbol() : ' ';
    }

    public boolean isEmpty() {
        return object == null;
    }
}