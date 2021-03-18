package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.models.*;
import com.revature.repos.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles data validation for {@link Post} and uses a {@link PostRepository}
 * to interact with the database
 */
@Service
public class PostService {

    /**
     * PostRepository that is managed by Spring
     */
    private PostRepository repo;

    /**
     * Constructor for PostService
     * @param repo PostRepository from Spring
     */
    @Autowired
    public PostService(PostRepository repo) {
        this.repo = repo;
    }

    /**
     * Gets a List of all of the Posts
     * @return a List of Post objects
     */
    public List<Post> findAllPosts() {
        return (List<Post>) repo.findAll();
    }

    /**
     * Gets a singular Post by id
     * @param id the id value of the Post
     * @return an Optional of Post
     */
    public Optional<Post> findPostByPostId(int id) {
        return repo.findById(id);
    }

    /**
     * Gets a List of Posts by Business
     * @param bus the Business object to get the Posts from
     * @return a List of Posts from the desired Business
     */
    public List<Post> findPostsByBusiness(Business bus) {
        return repo.findPostsByBusiness(bus);
    }

    /**
     * Add a Post object to the database
     * @param post the Post object to add
     */
    public void createPost(Post post) {
        if (!isPostValid(post))
            throw new InvalidRequestException();
        
        repo.save(post);
    }

    /**
     * Update a Post object
     * @param post the Post object to update
     */
    public void editPost(Post post) {
        if (!isPostValid(post))
            throw new InvalidRequestException();
        
        repo.save(post);
    }

    /**
     * Deletes a Post object
     * @param post the Post object to delete
     */
    public void deletePost(Post post) {
        if (!isPostValid(post))
            throw new InvalidRequestException();

        repo.delete(post);
    }

    /**
     * Checks a Post object's fields for validity
     * @param post the Post object to check
     * @return a boolean for if the Post is valid or not
     */
    protected boolean isPostValid(Post post) {
        if (post.getBusiness() == null) return false;
        if (post.getBody() == null || post.getBody().trim().equals("")) return false;
        if (post.getPostType() == null) return false;

        return true;
    }
    
}
