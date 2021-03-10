package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import com.revature.models.Business;
import com.revature.models.Post;
import com.revature.models.User;
import com.revature.repos.PostRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTester {
    //injectmocks actually creates the mock and also injects the dependent mocks that are marked with @mock (userrepo here)
    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    Business bus;
    Post post0, post1;
    User u;
    List<Post> postList;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        u = new User();
        u.setEmail("nathan.gamble@revature.net");
        u.setUsername("ngamble");
        u.setPassword("password");
        bus = new Business();
        bus.setBusinessName("fake");
        bus.setBusinessType("pets");
        bus.setEmail("fake.email@impostor.gov");
        bus.setLocation("123 Cherry Wood Ln");
        bus.setOwner(u);
        post0 = new Post();
        post0.setBusiness(bus);
        post0.setBody("fake post");
        post0.setPostType("sale");
        post1 = new Post();
        post1.setBusiness(bus);
        post1.setBody("fake post 1");
        post1.setPostType("sale");
        postList = new ArrayList<>();
        postList.add(post0);
        postList.add(post1);
    }

    @Test
    public void testGetAllBusinessPosts() {
        when(postRepository.findAll()).thenReturn(postList);

        List<Post> result = postService.findAllPosts();

        assertEquals(postList, result);
    }

    @Test
    public void testGetPostsByBusiness_givenValidBusiness() {
        when(postRepository.findPostsByBusiness(bus)).thenReturn(postList);

        List<Post> result = postService.findPostsByBusiness(bus);

        assertEquals(postList, result);
    }

    @Test
    public void testCreatePost_withValidPost() {
        Post minimumValidPost = new Post();
        minimumValidPost.setBusiness(bus);
        minimumValidPost.setBody("valid");
        when(postRepository.save(minimumValidPost)).thenReturn(null);

        
    }
}
