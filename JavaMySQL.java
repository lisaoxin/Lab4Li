import java.sql.*;

/**
 * File: Mysql.java
 * Author: [Your Name]
 * Description: This class handles CRUD operations for the Ship's health points in a MySQL database.
 */
public class JavaMySQL {
    private static final String DB_URL = "jdbc:mysql://localhost:127.0.0.1:3306/shipshealth";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "88888888";

    /**
     * Inserts the Ship's health points into the MySQL database.
     *
     * @param healthPoints The Ship's health points to be inserted.
     */
    public static void insert(int healthPoints) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO ship_health (health_points) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, healthPoints);
            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted + " row(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error inserting health points: " + e.getMessage());
        }
    }

    /**
     * Updates the Ship's health points in the MySQL database.
     *
     * @param healthPoints The new health points value.
     */
    public static void update(int healthPoints) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE ship_health SET health_points = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, healthPoints);
            int rowsUpdated = statement.executeUpdate();
            System.out.println(rowsUpdated + " row(s) updated.");
        } catch (SQLException e) {
            System.out.println("Error updating health points: " + e.getMessage());
        }
    }

    /**
     * Reads the Ship's health points from the MySQL database.
     */
    public static void read() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT health_points FROM ship_health";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int healthPoints = resultSet.getInt("health_points");
                System.out.println("Ship health points: " + healthPoints);
            } else {
                System.out.println("No health points found.");
            }
        } catch (SQLException e) {
            System.out.println("Error reading health points: " + e.getMessage());
        }
    }

    /**
     * Deletes the Ship's health points from the MySQL database.
     */
    public static void delete() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM ship_health";
            Statement statement = conn.createStatement();
            int rowsDeleted = statement.executeUpdate(sql);
            System.out.println(rowsDeleted + " row(s) deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting health points: " + e.getMessage());
        }
    }
}
