package com.coco.mygem.controller;

import com.coco.mygem.entity.Post;
import com.coco.mygem.entity.PostStatus;
import com.coco.mygem.entity.PostType;
import com.coco.mygem.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody Post post) {
        post.setPostId(postId);
        return ResponseEntity.ok(postService.updatePost(post));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Post>> getPostsByType(@PathVariable PostType type) {
        return ResponseEntity.ok(postService.getPostsByType(type));
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Post>> getPostsByTag(@PathVariable String tag) {
        return ResponseEntity.ok(postService.getPostsByTechTag(tag));
    }

    @PutMapping("/{postId}/status")
    public ResponseEntity<Void> updatePostStatus(
            @PathVariable Long postId,
            @RequestParam PostStatus status,
            @RequestParam Long reviewerId,
            @RequestParam(required = false) String reviewComment) {
        boolean success = postService.updatePostStatus(postId, status, reviewerId, reviewComment);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{postId}/progress")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long postId,
            @RequestParam Integer progress) {
        boolean success = postService.updateProgress(postId, progress);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{postId}/raised-amount")
    public ResponseEntity<Void> updateRaisedAmount(
            @PathVariable Long postId,
            @RequestParam Long amount) {
        boolean success = postService.updateRaisedAmount(postId, amount);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        boolean success = postService.deletePost(postId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/pending-review")
    public ResponseEntity<List<Post>> getPendingReviewPosts() {
        return ResponseEntity.ok(postService.getPendingReviewPosts());
    }

    @GetMapping("/recommended/{userId}")
    public ResponseEntity<List<Post>> getRecommendedPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getRecommendedPosts(userId));
    }
} 