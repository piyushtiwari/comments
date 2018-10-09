package com.example.comments.controller;


import java.util.List;

import com.example.comments.repository.CommentRepository;
import com.example.comments.repository.ProductRepository;
import com.example.comments.exception.ResourceNotFoundException;
import com.example.comments.model.Comment;
import com.example.comments.model.Product;
import com.example.comments.model.User;
import com.example.comments.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/product/{productId}/comment")
    public Comment createComment(@PathVariable (value = "productId") Long productId, @Valid @RequestBody Comment comment) {
        Product product = productRepository.findById(productId).isPresent() ? productRepository.findById(productId).get():null;
        comment.setProduct(product);

        long userId = 1;
        User user = userRepository.findById(userId).isPresent()? userRepository.findById(userId).get():null;
        return commentRepository.save(comment);
    }

    // Get a Single Comment
    @GetMapping("/comment/{id}")
    public Comment getCommentById(@PathVariable(value = "id") Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", commentId));
    }

    @PutMapping("/comment/{id}")
    public Comment updateComment(@PathVariable(value = "id") Long commentId,
                           @Valid @RequestBody Comment commentDetails) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", commentId));

        comment.setContent(commentDetails.getContent());
        comment.setIsPublished(commentDetails.getIsPublished());

        Comment updatedComment = commentRepository.save(comment);
        return updatedComment;
    }


    // Get all Comments
    @GetMapping("/comment")
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @PutMapping("/comment/{id}/moderate")
    public void moderateComment() {
        /*
         Call a comment-moderation service. It should have some static logic like checking the if the comment Text
         has some stop(abusive) words.

         We can also integrate it with some external self learning system like wit.ai, that can be trained to
         evaluate if a Comment is objectional or not
          */
    }
}
