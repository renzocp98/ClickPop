package clickgame.click_game_project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import clickgame.click_game_project.entities.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer>{

}
