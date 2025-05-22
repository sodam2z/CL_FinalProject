package model;

public class Potion extends GameObject {
    private String name;
    private int healAmount;
    private char symbol;

    public Potion(String name, int healAmount, char symbol) {
        this.name = name;
        this.healAmount = healAmount;
        this.symbol = symbol;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public String getName() {
        return name;
    }

    @Override
    public char getSymbol() {
        return (symbol == 'm') ? '\u2661' : '\u2665'; // ♡ / ♥
    }
}