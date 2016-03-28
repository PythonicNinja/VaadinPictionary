package pl.Wojtek.model;

/**
 *
 */
public class Player extends User {
    private Double points;

    public Player(User user) {
        this.setPoints(0.0);

        this.setId(user.getId());
        this.setPassword(user.getPassword());
        this.setRoom(user.getRoom());
        this.setUsername(user.getUsername());
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public void addPoints(Double points) {
        this.points += points;
    }


    public String toString() {
        return "Player(\n" + this.getUsername() +
                "Points: " + this.getPoints() + '\n' +
                ")";
    }
}
