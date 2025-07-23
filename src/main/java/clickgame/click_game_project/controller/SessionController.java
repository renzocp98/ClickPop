package clickgame.click_game_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.services.UserService;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/SessionInfo")
public class SessionController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
        User existingUser = userService.findByUsername(user.getUsername());

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario/contraseña incorrecta");
        }

    
        if (!existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario/contraseña incorrecta");
        }

        session.setAttribute("user", existingUser); 
        return ResponseEntity.ok("Inicio de sesión exitoso");
    }   

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); 
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }

}
