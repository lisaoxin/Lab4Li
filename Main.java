/**

 * Project: Lab 3
 * Purpose Details: Space Game
 * Course: IST 242
 * Author: Alvin Li
 * Date Developed: 06/06/2024
 * Last Date Changed: 06/09/2024
 * Rev: 06/09/2024

 */
//main class
public class Main {
    public static void main(String[] args) {

        Game game = new Game("SpaceGame", "1.0");


        Player player = new Player("Player1", 100);


        Ship ship = new Ship("Fighter3000", 50, 10, 5);


        Obstacles obstacle1 = new Obstacles("Small Asteroid", 20, 5, 10);
        Obstacles obstacle2 = new Obstacles("Large Asteroid", 40, 10, 5);
        Obstacles obstacle3 = new Obstacles("BlackHole", 30, 10, 20);


        PowerUps powerUp1 = new PowerUps("Shield", 25);
        PowerUps powerUp2 = new PowerUps("ExtraLife", 50);
        PowerUps powerUp3 = new PowerUps("ExtraDamage", 60);


        Enemies enemy1 = new Enemies("Alien Soldier", 40, 8);
        Enemies enemy2 = new Enemies("Alien Space Lord", 200, 50);
        Enemies enemy3 = new Enemies("Alien Captain", 60, 15);


        System.out.println("Game: " + game);
        System.out.println("Player: " + player);
        System.out.println("Ship: " + ship);
        System.out.println("Obstacle 1: " + obstacle1);
        System.out.println("Obstacle 2: " + obstacle2);
        System.out.println("Obstacle 3: " + obstacle3);
        System.out.println("Power-up 1: " + powerUp1);
        System.out.println("Power-up 2: " + powerUp2);
        System.out.println("Power-up 3: " + powerUp3);
        System.out.println("Enemy 1: " + enemy1);
        System.out.println("Enemy 2: " + enemy3);
        System.out.println("Enemy 3: " + enemy2);
    }
}
