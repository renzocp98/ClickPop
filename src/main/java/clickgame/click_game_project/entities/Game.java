package clickgame.click_game_project.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private List<User> users;

    private int numClicks;

    @Transient
    private int maxClicks;
    

    public Game() {
            this.users = new ArrayList<>();
    }

    public int getNumClicks() {
        return numClicks;
    }

    public int getMaxClicks() {
        return maxClicks;
    }

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }

    public void setNumClicks(int numClicks) {
        this.numClicks = numClicks;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
