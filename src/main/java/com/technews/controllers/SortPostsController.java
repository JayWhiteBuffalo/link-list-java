package com.technews.controllers;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.CommentRepository;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
public class SortPostsController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/posts/new")
    public String newPostsPageSetup(Model model, HttpServletRequest request) {
        User sessionUser = new User();

        if (request.getSession(false) != null) {
            sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        } else {
            model.addAttribute("loggedIn", false);
        }

        List<Post> postList = postRepository.findAllByOrderByPostedAtDesc();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            User user = userRepository.findUserById(p.getUserId());
            p.setUserName(user.getUsername());
        }
        model.addAttribute("userRepository", userRepository);
        model.addAttribute("postList", postList);
        model.addAttribute("point", "point");
        model.addAttribute("points", "points");

        return ("homepage");
    }


    @GetMapping("/posts/old")
    public String oldPostsPageSetup(Model model, HttpServletRequest request) {
        User sessionUser = new User();

        if (request.getSession(false) != null) {
            sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        } else {
            model.addAttribute("loggedIn", false);
        }

        List<Post> postList = postRepository.findAllByOrderByPostedAtAsc();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            User user = userRepository.findUserById(p.getUserId());
            p.setUserName(user.getUsername());
        }
        model.addAttribute("userRepository", userRepository);
        model.addAttribute("postList", postList);
        model.addAttribute("point", "point");
        model.addAttribute("points", "points");

        return ("homepage");
    }


    @GetMapping("/posts/top")
    public String topPostsPageSetup(Model model, HttpServletRequest request) {
        User sessionUser = new User();

        if (request.getSession(false) != null) {
            sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        } else {
            model.addAttribute("loggedIn", false);
        }

        List<Post> postList = postRepository.findAllOrderByVoteCountDesc();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            User user = userRepository.findUserById(p.getUserId());
            p.setUserName(user.getUsername());
        }

        model.addAttribute("userRepository", userRepository);
        model.addAttribute("postList", postList);
        model.addAttribute("point", "point");
        model.addAttribute("points", "points");

        return ("homepage");
    }

    @GetMapping("/posts/comments/top")
    public String mostPostsCommentsPageSetup(Model model, HttpServletRequest request) {
        User sessionUser = new User();

        if (request.getSession(false) != null) {
            sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            model.addAttribute("loggedIn", sessionUser.isLoggedIn());
        } else {
            model.addAttribute("loggedIn", false);
        }

        List<Post> postList = postRepository.findAllOrderByCommentCountDesc();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            User user = userRepository.findUserById(p.getUserId());
            p.setUserName(user.getUsername());
        }

        model.addAttribute("userRepository", userRepository);
        model.addAttribute("postList", postList);
        model.addAttribute("point", "point");
        model.addAttribute("points", "points");

        return ("homepage");
    }
}
