package clickgame.click_game_project.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.Role;
import clickgame.click_game_project.entities.User;

@DataJpaTest
@ActiveProfiles("test")
public class GameRepositoryTests {

    @Autowired
    GameRepository gameRepository;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    private Game game;
    private Role role;
    private User user;

    @BeforeEach
    void setup() {

        role= new Role();
        role.setName("USER");
        roleRepository.save(role);

        user = new User();
        user.setUsername("Renzo");
        user.setPassword("12345");
        user.setCountry("Spain");
        user.setRole(role);
        userRepository.save(user);

        game = new Game();
        game.setUser(user);
        game.setScore(20);

    }

    @Test
    void testSaveGame(){

        Game game1 = new Game();
        game1.setUser(user);
        game1.setScore(54);

        Game gameSaved = gameRepository.save(game1);

        assertThat(gameSaved).isNotNull();
        assertThat(gameSaved.getId()).isGreaterThan(0);
    }

    @Test
    void testExistingGame(){

        gameRepository.save(game);

        Game existGame = gameRepository.findById(game.getId()).orElse(null);

        assertThat(existGame).isNotNull();
        assertThat(existGame.getScore()).isEqualTo(20);

    }


    @Test
    void testAllGames(){

        gameRepository.save(game);

        Game game1 = new Game();
        game1.setUser(user);
        game1.setScore(54);
        gameRepository.save(game1);

        List<Game> listGames = (List<Game>) gameRepository.findAll();

        assertThat(listGames).isNotEmpty();
        assertThat(listGames.size()).isEqualTo(2);
    }
 
}
