package com.technews.controllers;


import com.technews.model.Comment;
import com.technews.model.Post;
import com.technews.model.User;
import com.technews.model.UserAttributes;
import com.technews.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomePageController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserAttributeRepository userAttributeRepository;

    //Login
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){

        if (request.getSession(false) != null){
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "login";
    }

    //Logout
    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
        return "redirect:/login";
    }

    public Model setupDashboardPage(Model model, HttpServletRequest request) throws Exception{
        User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");

        Integer userId = sessionUser.getId();
        UserAttributes userAttributes = userAttributeRepository.findByUserId(sessionUser.getId());

        List<Post> postList = postRepository.findAllPostsByUserId(userId);
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getUserId()));
            User user = userRepository.findUserById(p.getUserId());
            p.setUserName(user.getUsername());
        }
        model.addAttribute("userRepository", userRepository);
        model.addAttribute("attributes", userAttributes);
        model.addAttribute("user", sessionUser);
        model.addAttribute("postList", postList);
        model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        model.addAttribute("post", new Post());

        return model;
    }

    //Dashboard
    @GetMapping("/dashboard")
    public String dashboardPageSetup(Model model, HttpServletRequest request) throws Exception{

        if (request.getSession(false) != null){
            setupDashboardPage(model, request);
            return "dashboard";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }

    public Model setupSinglePostPage(int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null){
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        }

        Post post = postRepository.getById(id);
        post.setVoteCount(voteRepository.countVotesByPostId(post.getId()));

        User postUser = userRepository.findUserById(post.getUserId());
        post.setUserName(postUser.getUsername());

        List<Comment> commentList = commentRepository.findAllCommentsByPostId(post.getId());

        List<String> userNames = new ArrayList<>();
        for (Comment comment : commentList) {
            User user = userRepository.findUserById(comment.getUserId());
            if (user != null) {
                userNames.add(user.getUsername());
            } else {
                userNames.add("Anon");
            }
        }
        model.addAttribute("userRepository", userRepository);
        model.addAttribute("commentUserNames", userNames);
        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);
        model.addAttribute("comment", new Comment());

        return model;
    }

    //Single Post Set up
    @GetMapping("/post/{id}")
    public String singlePostPageSetup(@PathVariable int id, Model model, HttpServletRequest request) {
        User sessionUser = new User();

        if (request.getSession(false) != null){
            sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        } else {
            model.addAttribute("loggedIn", false);
        }


        setupSinglePostPage(id, model, request);
        return "single-post";
    }

    public Model setupEditPostPage(int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null) {
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");

            Post returnPost = postRepository.getById(id);
            User tempUser = userRepository.findUserById(returnPost.getUserId());
            returnPost.setUserName(tempUser.getUsername());
            returnPost.setVoteCount(voteRepository.countVotesByPostId(returnPost.getId()));

            List<Comment> commentList = commentRepository.findAllCommentsByPostId(returnPost.getId());

            model.addAttribute("post", returnPost);
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
            model.addAttribute("commentList", commentList);
            model.addAttribute("comment", new Comment());
        }

        return model;
    }
    @GetMapping("/dashboard/edit/{id}")
    public String editPostPageSetup(@PathVariable int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null){
            setupEditPostPage(id, model, request);
            return "edit-post";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }

    public void setupEditUserProfilePage(int id, Model model, HttpServletRequest request) throws Exception {
        if (request.getSession(false) != null) {
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");

            if (sessionUser != null) {
                UserAttributes userAttributes = userAttributeRepository.findByUserId(sessionUser.getId());

                model.addAttribute("user", sessionUser);
                model.addAttribute("loggedIn", sessionUser.isLoggedIn());
                model.addAttribute("attributes", userAttributes);
            }
        }
    }

    @GetMapping("/user/edit/{id}")
    public String userProfilePageSetup(@PathVariable int id, Model model, HttpServletRequest request) throws Exception{
        setupEditUserProfilePage(id, model, request);
        return"edit-user";
    }

    public Model setupSingleUserPage(int id, Model model, HttpServletRequest request) throws Exception {
        if (request.getSession(false) != null){
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        }

        User user = userRepository.findUserById(id);
        List<Post> postList = postRepository.findAllPostsByUserId(id);
        UserAttributes userAttributes = userAttributeRepository.findByUserId(user.getId());
//        for (Post p : postList) {
//            p.setVoteCount(voteRepository.countVotesByPostId(p.getUserId()));
//            User user = userRepository.getById(p.getUserId());
//            p.setUserName(user.getUsername());
//        }

        model.addAttribute("attributes", userAttributes);
        model.addAttribute("userRepository", userRepository);
        model.addAttribute("user", user);
        model.addAttribute("postList", postList);

        return model;
    }


    @GetMapping("/user/{id}")
    public String singleUserPageSetup(@PathVariable int id, Model model, HttpServletRequest request) throws Exception {
        setupSingleUserPage(id, model, request);

        return"single-user";
    }





    //Index
    @GetMapping("/")
    public String homepageSetup(Model model, HttpServletRequest request) throws Exception {
        User sessionUser = new User();

        if (request.getSession(false) != null){
            sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        } else {
            model.addAttribute("loggedIn", false);
        }

        List<Post> postList = postRepository.findAll();
        for (Post p : postList){
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            User user = userRepository.findUserById(p.getUserId());
            p.setUserName(user.getUsername());
        }

        model.addAttribute("userRepository", userRepository);
        model.addAttribute("postList", postList);
        //Refers to Up-votes
        model.addAttribute("point", "point");
        model.addAttribute("points", "points");

        return "homepage";
    }





    //Title and Link Error
    @GetMapping("/dashboardEmptyTitleAndLink")
    public String dashboardEmptyTitleAndLinkHandler(Model model, HttpServletRequest request) throws Exception{
        setupDashboardPage(model, request);
        model.addAttribute("notice", "To create a post the Title and Link must be populated.");
        return "dashboard";
    }

    @GetMapping("/singlePostEmptyComment/{id}")
    public String singlePostEmptyCommentHandler(@PathVariable int id, Model model, HttpServletRequest request) {
        setupSinglePostPage(id, model, request);
        model.addAttribute("notice", "To add a comment you must enter the comment in the comment text area.");
        return "single-post";
    }



    @GetMapping("/editPostEmptyComment/{id}")
    public String editPostEmptyCommentHandler(@PathVariable int id, Model model, HttpServletRequest request) {
        if (request.getSession(false) != null){
            setupEditPostPage(id, model, request);
            model.addAttribute("notice", "To add a comment you must enter the comment in the text area.");
            return "edit-post";
        } else {
            model.addAttribute("user", new User());
            return "login";
        }
    }

}
