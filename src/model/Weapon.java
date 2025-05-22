package model;

public class Weapon extends GameObject {
    private String name;
    private int damage;
    private char symbol;

    public Weapon(String name, int damage, char symbol) {
        this.name = name;
        this.damage = damage;
        this.symbol = symbol;
    }

    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }

    @Override
    public char getSymbol() {
        switch (symbol) {
            case 'S': return '\u2020'; // † Stick
            case 'W': return '\u2694'; // ⚔ Weak Sword
            case 'X': return '\u2692'; // ⚒ Strong Sword
            default: return '?';
        }
    }
}