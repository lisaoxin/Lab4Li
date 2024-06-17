import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ship {
    private String name;
    private int speed;
    private int damage;
    private int defense;

    // Default constructor
    public Ship() {
    }

    @JsonCreator
    public Ship(@JsonProperty("name") String name,
                @JsonProperty("speed") int speed,
                @JsonProperty("damage") int damage,
                @JsonProperty("defense") int defense) {
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        this.defense = defense;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    // toString method
    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", speed=" + speed +
                ", damage=" + damage +
                ", defense=" + defense +
                '}';
    }
}
