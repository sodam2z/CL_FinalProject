package model;

/**
 * Represents a healing potion that restores health to the hero.
 * There are two types: Minor Flask and Big Flask.
 */
public class Potion extends GameObject {
    private String name;
    private int healAmount;
    private char symbol;

    /**
     * Constructs a potion with a name, healing value, and symbol type.
     *
     * @param name        name of the potion (e.g., "Minor Flask")
     * @param healAmount  amount of HP restored when used
     * @param symbol      character that determines potion type ('m' or 'B')
     */
    public Potion(String name, int healAmount, char symbol) {
        this.name = name;
        this.healAmount = healAmount;
        this.symbol = symbol;
    }

    /**
     * Returns the amount of health this potion restores.
     * @return healing value
     */
    public int getHealAmount() {
        return healAmount;
    }

    /**
     * Returns the name of the potion.
     * @return potion name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the character symbol representing the potion on the map.
     * Minor Flask: ♡ (U+2661), Big Flask: ♥ (U+2665)
     * @return Unicode character
     */
    @Override
    public char getSymbol() {
        return (symbol == 'm') ? '\u2661' : '\u2665'; // ♡ / ♥
    }
}