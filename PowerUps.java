/**

 * Project: Lab 3
 * Purpose Details: Space Game
 * Course: IST 242
 * Author: Alvin Li
 * Date Developed: 06/06/2024
 * Last Date Changed: 06/09/2024
 * Rev: 06/09/2024

 */

// PowerUps class
public class PowerUps {
    /**
     * The names of the powerups
     */
    private String name;
    /**
     * The health you gain back from the powerups and
     */
    private int value;

    /**
     * Adds two numbers and returns the result.
     *
     * @param name This is the name of the powerups.
     * @param value This is the health or extra damage you gain from powerups.
     */
    public PowerUps(String name, int value) {
        this.name = name;
        this.value = value;
    }
    // getters and setters
    public String toString() {
        return "Name: " + name + ", Value: " + value;
    }
}
