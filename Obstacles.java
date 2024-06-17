/**

 * Project: Lab 3
 * Purpose Details: Space Game
 * Course: IST 242
 * Author: Alvin Li
 * Date Developed: 06/06/2024
 * Last Date Changed: 06/09/2024
 * Rev: 06/09/2024

 */

// Obstacles class
public class Obstacles {
    /**
     * The name of the obstacles
     */
    private String name;
    /**
     * The size of the obstacles
     */
    private int size;
    /**
     * The damage that the obstacles deal
     */
    private int damage;
    /**
     * The speed of the obstacles
     */
    private int speed;

    /**
     * Adds four numbers and returns the result.
     *
     * @param name This is the color of the bike frame.
     * @param size This is the size of the bike frame.
     * @param damage This is the size of the bike frame.
     * @param speed This is the size of the bike frame.
     */
    public Obstacles(String name, int size, int damage, int speed) {
        this.name = name;
        this.size = size;
        this.damage = damage;
        this.speed = speed;
    }
    // getters and setters
    public String toString() {
        return "Name: " + name + ", Size: " + size + ", Damage: " + damage + ", Speed: " + speed;
    }

}
