/**

 * Project: Lab 3
 * Purpose Details: Space Game
 * Course: IST 242
 * Author: Alvin Li
 * Date Developed: 06/06/2024
 * Last Date Changed: 06/09/2024
 * Rev: 06/09/2024

 */

// Player Class
public class Player {
    /**
     * The player's name
     */
    private String name;
    /**
     * The player's health
     */
    private int health;

    /**
     * Adds two numbers and returns the result.
     *
     * @param name This is the name of the player.
     * @param health This is the health of the player.
     */
    public Player(String name, int health) {
        this.name = name;
        this.health = health;
    }
    // getters and setters
    public String toString() {
        return "Name: " + name + ", Health: " + health;
    }
}