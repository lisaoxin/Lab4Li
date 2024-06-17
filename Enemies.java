
/**
 *

 * Project: Lab 3
 * Purpose Details: Space Game
 * Course: IST 242
 * Author: Alvin Li
 * Date Developed: 06/06/2024
 * Last Date Changed: 06/09/2024
 * Rev: 06/09/2024

 */
// Enemies class
public class Enemies {

    /**
     * The name of the enemy
     */
    private String name;
    /**
     * The health of the enemy.
     */
    private int health;
    /**
     * The damage the enemy deals.
     */
    private int damage;

    /**
     * Adds three numbers and returns the result.
     *
     * @param name This is name of the enemy.
     * @param health This is the health of the enemy.
     * @param damage This is the damage the enemy deals.
     */
    public Enemies(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }
    // getters and setters
    public String toString() {
        return "Name: " + name + ", Health: " + health + ", Damage: " + damage;
    }
}
