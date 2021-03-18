package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Business;
import com.revature.models.Post;
import com.revature.models.User;
import com.revature.repos.PostRepository;

import org.jeasy.random.EasyRandom;
import com.revature.repos.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import java.sql.Timestamp;


public class PostServiceTester {
    //injectmocks actually creates the mock and also injects the dependent mocks that are marked with @mock (userrepo here)
    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    Business bus;
    Post post0, badPost;
    User user;
    List<Post> postList;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setEmail("nathan.gamble@revature.net");
        user.setUsername("ngamble");
        user.setPassword("password");
        bus = new Business();
        bus.setBusinessName("fake");
        bus.setBusinessType("pets");
        bus.setEmail("fake.email@impostor.gov");
        bus.setLocation("123 Cherry Wood Ln");
        bus.setOwner(user);
        post0 = new Post();
        post0.setBusiness(bus);
        post0.setBody("fake post");
        post0.setPostType("sale");
        badPost = new Post();
        badPost.setBusiness(null);
        badPost.setBody("fake post 1");
        badPost.setPostType("sale");
        postList = new ArrayList<>();
        postList.add(post0);
        postList.add(badPost);
    }

    @Test
    @DisplayName("Verifying findAllPosts() works as expected and pulls all posts")
    public void testFindAllPosts() {
        when(postRepository.findAll()).thenReturn(postList);
        assertEquals(postList, postService.findAllPosts());
    }

    @Test
    @DisplayName("Verifying getPostByPostId() works as expected and pulls a post by id")
    public void testgetPostByPostId() {
        post0.setPostId(1);
        when(postRepository.findById(post0.getPostId())).thenReturn(java.util.Optional.of(post0));
        assertEquals(post0, postService.findPostByPostId(post0.getPostId()).get());
    }

    @Test
    @DisplayName("Verifying findPostsByBusiness() works as expected and pulls posts by business name")
    public void testFindPostsByBusiness() {
        when(postRepository.findPostsByBusiness(bus)).thenReturn(postList);
        assertEquals(postList, postService.findPostsByBusiness(bus));
    }

    @Test
    @DisplayName("Verifying createPost() works as expected and call repo to create new post")
    public void testCreatePost() {
        postService.createPost(post0);
        verify(postRepository, times(1)).save(post0);
    }
    @Test
    @DisplayName("Verifying createPost() throws error when expected given invalid post")
    public void testCreatePostInvalid() {
        assertThrows(InvalidRequestException.class, () -> postService.createPost(badPost));
        verify(postRepository, times(0)).save(badPost);
    }

    @Test
    @DisplayName("Verifying editPost() works as expected and call repo to create update given post")
    public void testEditPost() {
        postService.editPost(post0);
        verify(postRepository, times(1)).save(post0);
    }
    @Test
    @DisplayName("Verifying editPost() throws error when expected given invalid post")
    public void testEditPostInvalid() {
        assertThrows(InvalidRequestException.class, () -> postService.editPost(badPost));
        verify(postRepository, times(0)).save(badPost);
    }

    @Test
    @DisplayName("Verifying deletePost() works as expected and call repo to delete given post")
    public void testDeletePost() {
        postService.deletePost(post0);
        verify(postRepository, times(1)).delete(post0);
    }
    @Test
    @DisplayName("Verifying deletePost() throws error when expected given invalid post")
    public void testDeletePostInvalid() {
        assertThrows(InvalidRequestException.class, () -> postService.deletePost(badPost));
        verify(postRepository, times(0)).delete(badPost);
    }

    @Test
    @DisplayName("Verifying error thrown on isPostValid() returns true for valid entry")
    public void testIsPostValid() {
        assertTrue(postService.isPostValid(post0));
    }

    @Test
    @DisplayName("Verifying error thrown on isHoursValid() for null business or day on hours obj")
    public void testIsPostValidInvalid() {
        assertFalse(postService.isPostValid(badPost));
        badPost.setBusiness(bus);
        badPost.setBody(null);
        assertFalse(postService.isPostValid(badPost));
        badPost.setBody("");
        assertFalse(postService.isPostValid(badPost));
        badPost.setBody("valid");
        badPost.setPostType(null);
        assertFalse(postService.isPostValid(badPost));
        badPost.setPostType("valid check");
        assertTrue(postService.isPostValid(badPost));
    }

}
