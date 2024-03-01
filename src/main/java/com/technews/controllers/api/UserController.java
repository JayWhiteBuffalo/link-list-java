package com.technews.controllers.api;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.model.UserAttributes;
import com.technews.repository.UserAttributeRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserAttributeRepository userAttributeRepository;

    //Get All User
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        List <User> userList = userRepository.findAll();
        for (User u : userList) {
            List<Post> postList = u.getPosts();
            for (Post p: postList) {
                p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            }
        }
        return userList;
    }

    //Get User by ID
    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable Integer id){
        User returnUser = userRepository.getById(id);
        List<Post> postList = returnUser.getPosts();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }
        return returnUser;
    }

    //Add User
    @PostMapping("/api/users")
    public User addUser(@RequestBody User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        UserAttributes userAttributes = new UserAttributes();
        userAttributes.setCreated_date(new Date());
        userAttributes.setCreated_time(new Date());
        userAttributes.updateLastLogin();

        // Set userAttributes for the user - Set user for the userAttributes - Share one-to-one relationship
        user.setUserAttributes(userAttributes);
        userAttributes.setUser(user);

        userRepository.save(user);
        return user;
    }

    //Update User
    @PutMapping("/api/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user){
        User tempUser = userRepository.getById(id);

        if (tempUser != null) {
            user.setId(tempUser.getId());
            userRepository.save(user);
        }
        return user;
    }

    //Delete User
    @DeleteMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

}
