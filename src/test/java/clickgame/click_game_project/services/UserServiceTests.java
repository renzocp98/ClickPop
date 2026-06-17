package clickgame.click_game_project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import clickgame.click_game_project.entities.Role;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.repositories.RoleRepository;
import clickgame.click_game_project.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setName("USER");

        user = new User();
        user.setId(1);
        user.setUsername("Renzo");
        user.setPassword("12345");
        user.setCountry("Spain");
        user.setRole(role);
    }

    // ─── save ─────────────────────────────────────────────────────────────────

    @Test
    void testSave_assignsUserRoleAndSaves() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.save(user);

        assertThat(saved).isNotNull();
        assertThat(saved.getRole().getName()).isEqualTo("USER");
        verify(userRepository).save(user);
    }

    @Test
    void testSave_throwsWhenRoleNotFound() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.save(user))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Role not found");

        verify(userRepository, never()).save(any());
    }

    // ─── findByUsername ───────────────────────────────────────────────────────

    @Test
    void testFindByUsername_returnsUser() {
        when(userRepository.findByUsername("Renzo")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("Renzo");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("Renzo");
    }

    @Test
    void testFindByUsername_throwsWhenNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("unknown"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Usuario no encontrado");
    }

    // ─── delete ───────────────────────────────────────────────────────────────

    @Test
    void testDelete_deletesUser_whenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.delete(1);

        verify(userRepository).delete(user);
    }

    @Test
    void testDelete_doesNothing_whenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        userService.delete(99);

        verify(userRepository, never()).delete(any());
    }

    // ─── update ───────────────────────────────────────────────────────────────

    @Test
    void testUpdate_updatesPassword_whenUserExists() {
        User updatedData = new User();
        updatedData.setPassword("newpass");

        when(userRepository.findByUsername("Renzo")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> result = userService.update("Renzo", updatedData);

        assertThat(result).isPresent();
        assertThat(user.getPassword()).isEqualTo("newpass");
        verify(userRepository).save(user);
    }

    @Test
    void testUpdate_returnsEmpty_whenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.update("unknown", new User());

        assertThat(result).isEmpty();
        verify(userRepository, never()).save(any());
    }

    // ─── findAll ──────────────────────────────────────────────────────────────

    @Test
    void testFindAll_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("Renzo");
    }
}
