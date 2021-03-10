package com.revature.repos;

import com.revature.models.Business;
import com.revature.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    
    List<Post> findPostsByBusiness(Business bus);

}