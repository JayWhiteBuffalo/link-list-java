package com.technews.controllers;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.model.Vote;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    //Get All Posts
    @GetMapping("/api/posts")
    public List<Post> getAllPosts() {
        List<Post> postList = postRepository.findAll();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }
        return postList;
    }

    //Get Post by ID
    @GetMapping("/api/posts/{id}")
    public Post getPost(@PathVariable Integer id) {
        Post returnPost = postRepository.getById(id);
        returnPost.setVoteCount(voteRepository.countVotesByPostId((returnPost.getId())));

        return returnPost;
    }

    //Create Post
    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public  Post addPost(@RequestBody Post post) {
        postRepository.save(post);
        return post;
    }

    //Update Post
    @PutMapping("/api/posts/{id}")
    public Post updatePost(@PathVariable int id, @RequestBody Post post) {
        Post tempPost = postRepository.getById(id);
        tempPost.setTitle(post.getTitle());
        return tempPost;
    }

    //Update Votes
    @PutMapping("/api/posts/upvote")
    public String addVote(@RequestBody Vote vote, HttpServletRequest request) {
        String returnValue = "";
        // Check if there's an active session
        if(request.getSession(false) != null){
            Post returnPost = null;

            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            vote.setUserId(sessionUser.getId());
            voteRepository.save(vote);

            returnPost = postRepository.getById(vote.getPostId());
            returnPost.setVoteCount(voteRepository.countVotesByPostId((vote.getPostId())));

            returnValue  = "";
        } else {
            // If there's no active session, set the return value to "login"
            returnValue ="login";
        }
            return  returnValue;
    }

    //Downvote
    @PutMapping("/api/posts/downvote")
    public String removeVote(@RequestBody Vote vote, HttpServletRequest request) {
        String returnValue = "";

        // Check if there's an active session
        if (request.getSession(false) != null) {
            // Fetch the post and user information
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");

            // Set user id for the vote
            vote.setUserId(sessionUser.getId());

            // Check if the user has already voted on this post
            Vote existingVote = voteRepository.findByUserIdAndPostId(sessionUser.getId(), vote.getPostId());
            if (existingVote != null) {
                // If the user has already voted, remove the existing vote (downvote)
                voteRepository.delete(existingVote);
            } else {
                returnValue = "No vote exists to remove.";
            }

            // Set the return value to an empty string
            returnValue = "";
        } else {
            // If there's no active session, set the return value to "login"
            returnValue = "login";
        }

        return returnValue;
    }

    //Delete Post
    @DeleteMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable int id) {
        postRepository.deleteById(id);
    }

}
