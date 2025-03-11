package clickgame.click_game_project.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
public class User {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    
    @OneToMany
    @JoinColumn(name = "id_game")
    private List<Game> game;
    
    private String username;
    private String password;
    private String country;
    
    @Transient
    private boolean admin;

    @Transient
    private int numClicks;
    
    @OneToMany
    @JoinColumn(name = "id_role")
    private List<Role> role;
    
    public User() {
        this.role = new ArrayList<>();
        this.game = new ArrayList<>();
        
    }
    
    public List<Game> getGame() {
        return game;
    }

    public void setGame(List<Game> game) {
        this.game = game;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }



}
