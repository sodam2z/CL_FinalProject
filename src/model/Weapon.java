package model;

/**
 * Represents a weapon used by the hero to attack monsters.
 * Each weapon has a name, damage value, and a visual symbol.
 */
public class Weapon extends GameObject {
    private String name;
    private int damage;
    private char symbol;

    /**
     * Constructs a weapon with the given name, damage, and type symbol.
     *
     * @param name    name of the weapon (e.g., "Stick", "Weak Sword")
     * @param damage  the amount of damage this weapon deals
     * @param symbol  character indicating weapon type ('S', 'W', 'X')
     */
    public Weapon(String name, int damage, char symbol) {
        this.name = name;
        this.damage = damage;
        this.symbol = symbol;
    }

    /**
     * Returns the damage dealt by this weapon.
     * @return damage value
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the name of the weapon.
     * @return weapon name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the character symbol representing the weapon on the grid.
     * Stick: † (U+2020), Weak Sword: ⚔ (U+2694), Strong Sword: ⚒ (U+2692)
     * @return Unicode symbol for display
     */
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