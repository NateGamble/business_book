package com.revature.services;

import com.revature.models.Business;
import com.revature.models.Post;
import com.revature.models.User;
import com.revature.repos.PostRepository;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceTester {
    //injectmocks actually creates the mock and also injects the dependent mocks that are marked with @mock (userrepo here)
    @InjectMocks
    PostService userService;

    @Mock
    PostRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllBusinessPosts() {
        User u = new User();
        u.setEmail("nathan.gamble@revature.net");
        u.setUsername("ngamble");
        u.setPassword("password");
        Business bus = new Business();
        bus.setBusinessName("fake");
        bus.setBusinessType("pets");
        bus.setEmail("fake.email@impostor.gov");
        bus.setLocation("123 Cherry Wood Ln");
        bus.setOwner(u);
        Post post = new Post();
        post.setBusiness(bus);
        post.setBody("fake post");
        // post.setPostType(postType);
    }
}
