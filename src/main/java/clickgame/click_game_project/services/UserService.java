package clickgame.click_game_project.services;

import java.util.List;
import java.util.Optional;

import clickgame.click_game_project.entities.User;

public interface UserService {
    User save(User user);
    void delete(int id);
    List<User> findAll();
    Optional<User>  update(int id, User user);


}
