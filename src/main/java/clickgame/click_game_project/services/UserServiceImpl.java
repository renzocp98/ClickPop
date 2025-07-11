package clickgame.click_game_project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clickgame.click_game_project.entities.Role;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.repositories.RoleRepository;
import clickgame.click_game_project.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    @Transactional
    public User save(User user){
        
        Optional<Role> optionalrole = roleRepository.findByName("USER");
        
        if (optionalrole.isPresent()) {
            user.setRole(optionalrole.get());
        } else {
            // no deberia ejecutarse ya que busca directamente en la BBDD
            throw new RuntimeException("Role not found");
        }
        
        return userRepository.save(user);
        
    }
    
    @Override
    @Transactional
    public void delete(int id) {
        
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(userRepository::delete);
    }
    
    @Override
    @Transactional
    public List<User> findAll() {
        return (List<User>)userRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<User> update(int id, User user) {

        Optional<User> userOptional = userRepository.findById(id);
        
        if(userOptional.isPresent()){

            User userdb = userOptional.orElseThrow();
            userdb.setUsername(user.getUsername());
            userdb.setCountry(user.getCountry());
            return Optional.of(userRepository.save(userdb));
        }
        return userOptional;


    }
    
}
