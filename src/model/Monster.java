package model;

public class Monster extends GameObject {
    private String name;
    private int hp;
    private int damage;
    private char symbol;

    public Monster(String name, int hp, int damage, char symbol) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.symbol = symbol;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
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
            case 'G': return '\u2618'; // ☘ Goblin
            case 'O': return '\u2689'; // ⚉ Orc
            case 'T': return '\u2656'; // ♖ Troll
            default: return '?';
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }
}