package clickgame.click_game_project.repositories;

import org.springframework.data.repository.CrudRepository;

import clickgame.click_game_project.entities.Game;

public interface GameRepository extends CrudRepository<Game, Integer>{

}
