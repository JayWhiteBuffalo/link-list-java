package com.technews.controllers;

import com.technews.model.*;
import com.technews.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class TechNewController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAttributeRepository userAttributeRepository;

    @PostMapping("users/login")
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) throws Exception{

        if ((user.getPassword().equals(null) || user.getPassword().isEmpty()) || (user.getEmail().equals(null) || user.getEmail().isEmpty())) {
            model.addAttribute("notice", "Email address and password must be populated in order to login.");
            return "login";
        }

        User sessionUser = userRepository.findUserByEmail(user.getEmail());

        try{
            if (sessionUser.equals(null)) {

            }
        } catch (NullPointerException e) {
            model.addAttribute("notice", "Email address is not recognized.");
            return "login";
        }

        //Validate Password
        String sessionUserPassword = sessionUser.getPassword();
        boolean isPasswordValid = BCrypt.checkpw(user.getPassword(), sessionUserPassword);
        if(isPasswordValid == false){
            model.addAttribute("notice", "Password is not valid.");
            return "login";
        }


        UserAttributes userAttributes = userAttributeRepository.findByUserId(sessionUser.getId());
        System.out.println(sessionUser);
        System.out.println(userAttributes);
        sessionUser.setLoggedIn(true);
//        userAttributes.updateLastLogin();
        request.getSession().setAttribute("SESSION_USER", sessionUser);

        return "redirect:/dashboard";
    }

    //Add User
    @PostMapping("/users")
    public String signup(@ModelAttribute User user, Model model, HttpServletRequest request) throws Exception{

        if((user.getUsername().equals(null) || user.getUsername().isEmpty()) || (user.getPassword().equals(null) || user.getPassword().isEmpty()) || (user.getEmail().equals(null) || user.getEmail().isEmpty())) {
            model.addAttribute("notice", "Must complete all fields to signup.");
            return "login";
        }

        try {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            UserAttributes userAttributes = new UserAttributes();
            userAttributes.setCreated_date(new Date());
            userAttributes.setCreated_time(new Date());
            userAttributes.updateLastLogin();
            user.setUserAttributes(userAttributes);
            userAttributes.setUser(user);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("notice", "Email address is not available. Please choose a diffrent email address");
            return "login";
        }

        User sessionUser = userRepository.findUserByEmail(user.getEmail());

        try {
            if (sessionUser.equals(null)) {
            }
            } catch (NullPointerException e){
                model.addAttribute("notice", "user is not recognized.");
                return "login";
            }


            sessionUser.setLoggedIn(true);
            request.getSession().setAttribute("SESSION_USER", sessionUser);

            return "redirect:/dashboard";
        }



    @PostMapping("/posts")
    public String addPostDashboardPage(@ModelAttribute Post post, Model model, HttpServletRequest request) {

        if ((post.getTitle().equals(null) || post.getTitle().isEmpty()) || (post.getPostUrl().equals(null) || post.getPostUrl().isEmpty())) {
            return "redirect:/dashboardEmptyTitleAndLink";
        }
        if (request.getSession(false) == null) {
            return "redirect:/login";
        } else {
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            post.setUserId(sessionUser.getId());
            postRepository.save(post);

            return "redirect:/dashboard";
        }
    }

    @PostMapping("/posts/{id}")
    public String updatePostDashboardPage(@PathVariable int id, @ModelAttribute Post post, Model model, HttpServletRequest request){

        if(request.getSession(false) == null) {
            model.addAttribute("user", new User());
            return "redirect/dashboard";
        } else {
            Post tempPost = postRepository.getById(id);
            tempPost.setTitle(post.getTitle());
            tempPost.setPostText((post.getPostText()));
            postRepository.save(tempPost);

            return "redirect:/dashboard";
        }
    }

    @PostMapping("/comments")
    public String createCommentCommentsPage(@ModelAttribute Comment comment, Model model, HttpServletRequest request) {

        if(comment.getCommentText().isEmpty() || comment.getCommentText().equals(null)) {
            return "redirect:/singlePostEmptyComment/" + comment.getPostId();
        } else {
            if (request.getSession(false) != null) {
                User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
                comment.setUserId(sessionUser.getId());
                commentRepository.save(comment);
                return "redirect:/post/" + comment.getPostId();
            } else {
                return "login";
            }
        }
    }

    @PostMapping("comments/edit")
    public String createCommentEditPage(@ModelAttribute Comment comment, Model model, HttpServletRequest request) {

        if (comment.getCommentText().equals("") || comment.getCommentText().equals(null)) {
            return "redirect:/editPostEmptyComment/" + comment.getPostId();
        } else {
            if (request.getSession(false) != null) {
                User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
                comment.setUserId(sessionUser.getId());
                commentRepository.save(comment);

                return "redirect:/dashboard/edit/" + comment.getPostId();
            } else {
                return "redirect:/login";
            }
        }
    }

    @PutMapping("posts/upvote")
    public String addVoteCommentsPage(@RequestBody Vote vote, HttpServletRequest request, HttpServletResponse response) {

        if (request.getSession(false) != null){
            Post returnPost = null;
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            vote.setUserId(sessionUser.getId());
            voteRepository.save(vote);

            returnPost = postRepository.getById(vote.getPostId());
            returnPost.setVoteCount(voteRepository.countVotesByPostId(vote.getPostId()));

            return "redirect:/posts";
        } else {
            return "redirect:/login";
        }
    }


    @PostMapping("/user/edit/{id}")
    public String processEditProfileForm(@ModelAttribute  UserAttributes userAttributes, Model model, HttpServletRequest request, HttpServletResponse response){
        if(request.getSession(false) == null) {
            return "redirect:/login";
        } else {

            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            Integer id = sessionUser.getId();
            userAttributes.setUser(sessionUser);
            sessionUser.setUserAttributes(userAttributes);

            userAttributeRepository.save(userAttributes);

            return "redirect:/user/edit/" + id;

        }
    }
}
