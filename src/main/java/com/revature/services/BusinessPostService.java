package com.revature.services;

import com.revature.exceptions.ResourcePersistenceException;
import com.revature.models.*;
import com.revature.repos.BusinessPostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessPostService {

    private BusinessPostRepository repo;

    @Autowired
    public BusinessPostService(BusinessPostRepository repo) {
        super();
        this.repo = repo;
    }

    public Optional<Posts> findPostByPostId(int id) {
        return repo.findById(id);
    }

    public void createPost(Posts post) {
        if (!isValid(post))
            throw new ResourcePersistenceException();
        
        repo.save(post);
    }

    public void editPost(Posts post) {
        if (!isValid(post))
            throw new ResourcePersistenceException();
        
        repo.save(post);
    }


    private boolean isValid(Posts post) {
        if (post.getBusiness() == null) return false;
        if (post.getBody() == null || post.getBody().trim().equals("")) return false;
        if (post.getPostType() == null) return false;

        return true;
    }
    
}
