package model;

/**
 * Represents a monster in the game with name, health points (HP), damage, and a unique symbol.
 * Monsters block movement and can be attacked by the hero.
 */
public class Monster extends GameObject {
    private String name;
    private int hp;
    private int damage;
    private char symbol;

    /**
     * Constructs a monster with the given attributes.
     *
     * @param name    the name of the monster (e.g., "Goblin")
     * @param hp      the monster's health points
     * @param damage  the amount of damage this monster deals when counterattacking
     * @param symbol  character used to identify monster type (used for rendering)
     */
    public Monster(String name, int hp, int damage, char symbol) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.symbol = symbol;
    }

    /**
     * Returns the monster's current health.
     * @return current HP value
     */
    public int getHp() {
        return hp;
    }

    /**
     * Sets the monster's health to a new value.
     * @param hp the new HP value
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Returns the amount of damage this monster deals.
     * @return damage value
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the monster's name.
     * @return monster name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the symbol representing the monster on the grid.
     * ☘ (Goblin), ⚉ (Orc), ♖ (Troll)
     */
    @Override
    public char getSymbol() {
        switch (symbol) {
            case 'G': return '\u2618'; // ☘ Goblin
            case 'O': return '\u2689'; // ⚉ Orc
            case 'T': return '\u2656'; // ♖ Troll
            default: return '?';
        }
    }

    /**
     * Checks if the monster is dead (HP ≤ 0).
     */
    public boolean isDead() {
        return hp <= 0;
    }
}