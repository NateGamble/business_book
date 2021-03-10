package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.models.*;
import com.revature.repos.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository repo;

    @Autowired
    public PostService(PostRepository repo) {
        this.repo = repo;
    }

    public List<Post> findAllPosts() {
        return (List<Post>) repo.findAll();
    }

    public Optional<Post> findPostByPostId(int id) {
        return repo.findById(id);
    }

    public List<Post> findPostsByBusiness(Business bus) {
        return repo.findPostsByBusiness(bus);
    }

    public void createPost(Post post) {
        if (!isPostValid(post))
            throw new InvalidRequestException();
        
        repo.save(post);
    }

    public void editPost(Post post) {
        if (!isPostValid(post))
            throw new InvalidRequestException();
        
        repo.save(post);
    }

    public void deletePost(Post post) {
        if (!isPostValid(post))
            throw new InvalidRequestException();

        repo.delete(post);
    }


    protected boolean isPostValid(Post post) {
        if (post.getBusiness() == null) return false;
        if (post.getBody() == null || post.getBody().trim().equals("")) return false;
        if (post.getPostType() == null) return false;

        return true;
    }
    
}
