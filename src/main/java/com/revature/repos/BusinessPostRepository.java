package com.revature.repos;

import com.revature.models.Business;
import com.revature.models.Posts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessPostRepository extends CrudRepository<Posts, Integer> {
    
    List<Posts> findPostsByBusiness(Business bus);

}