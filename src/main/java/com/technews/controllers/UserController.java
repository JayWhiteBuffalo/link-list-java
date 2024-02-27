package com.technews.controllers;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

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
