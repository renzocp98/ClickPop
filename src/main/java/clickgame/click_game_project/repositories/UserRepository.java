package clickgame.click_game_project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import clickgame.click_game_project.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

    @Query("select u.username from User u , Game g group by u.username order by g.numClicks desc")
    List<User> rankUsers();
    
    @Query("select u.username from User u , Game g where u.country = ?1 group by u.username order by u.numClicks desc")
    List<User> rankUsersPerCountry(String country);

}
