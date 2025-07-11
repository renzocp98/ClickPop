package clickgame.click_game_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.services.UserService;
import jakarta.validation.Valid;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> list(){
        return userService.findAll();
    }

    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user,BindingResult result){

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }
        if (user.getRole().getName().equals("ADMIN") ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nombre reservado");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }
}
