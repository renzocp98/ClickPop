package clickgame.click_game_project.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import clickgame.click_game_project.entities.Role;
import clickgame.click_game_project.entities.User;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    private User user;
    private Role role; 

    @BeforeEach
    void setup() {
        
        role = new Role();
        role.setName("USER");
        role = roleRepository.save(role);

        user = new User();
        user.setUsername("Renzo");
        user.setPassword("12345");
        user.setCountry("Spain");
        user.setRole(role); 
    }

    @Test
    void testSaveUser() {
        
        User user1 = new User();
        user1.setUsername("Carlos");
        user1.setPassword("67890");
        user1.setCountry("Spain");
        user1.setRole(role);

        User userSaved = userRepository.save(user1);

        assertThat(userSaved).isNotNull();
        assertThat(userSaved.getId()).isGreaterThan(0);
    }

    @Test
    void testUserExists() {
        
        userRepository.save(user);

        User userExist = userRepository.findById(user.getId()).orElse(null);
        
        assertThat(userExist).isNotNull();
        assertThat(userExist.getUsername()).isEqualTo("Renzo");
    }

    @Test
    void testAllusers(){

        User user1 = new User();
        user1.setUsername("Carlos");
        user1.setPassword("67890");
        user1.setCountry("Spain");
        user1.setRole(role);
        userRepository.save(user1);
        userRepository.save(user);

        List<User> listUsers = (List<User>) userRepository.findAll();

        assertThat(listUsers).isNotEmpty();
        assertThat(listUsers.size()).isEqualTo(2);

    }

    @Test
    void testFindByUsername_returnsUser_whenExists() {
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("Renzo");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("Renzo");
    }

    @Test
    void testFindByUsername_returnsEmpty_whenNotExists() {
        Optional<User> result = userRepository.findByUsername("doesNotExist");

        assertThat(result).isEmpty();
    }

}
