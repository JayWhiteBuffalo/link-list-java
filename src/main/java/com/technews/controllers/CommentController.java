package com.technews.controllers;

import com.technews.model.Comment;
import com.technews.repository.CommentRepository;
import com.technews.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    //Get All Comments
    @GetMapping("/api/comments")
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    //Get Comment by Id
    @GetMapping("/api/comments/{id}")
    public Comment getComment(@PathVariable int id){
        return commentRepository.getById(id);
    }

    //Add Comment
    @PostMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    //Update Comment
    @PutMapping("api/updateComment")
    public Comment updateComment(@RequestBody Comment comment){
        return commentRepository.save(comment);
    }

    //Delete Comment
    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int id){
        commentRepository.deleteById(id);
    }
}
