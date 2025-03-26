package com.coco.mygem.controller;

import com.coco.mygem.entity.Post;
import com.coco.mygem.entity.PostStatus;
import com.coco.mygem.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping("/submit/{postId}")
    public ResponseEntity<Void> submitForReview(@PathVariable Long postId) {
        boolean success = auditService.submitForReview(postId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/review/{postId}")
    public ResponseEntity<Void> reviewPost(
            @PathVariable Long postId,
            @RequestParam PostStatus status,
            @RequestParam Long reviewerId,
            @RequestParam(required = false) String reviewComment) {
        boolean success = auditService.reviewPost(postId, status, reviewerId, reviewComment);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Post>> getPendingReviewPosts() {
        return ResponseEntity.ok(auditService.getPendingReviewPosts());
    }

    @GetMapping("/reviewed/{reviewerId}")
    public ResponseEntity<List<Post>> getReviewedPosts(@PathVariable Long reviewerId) {
        return ResponseEntity.ok(auditService.getReviewedPosts(reviewerId));
    }

    @GetMapping("/history/{postId}")
    public ResponseEntity<List<Map<String, Object>>> getPostReviewHistory(@PathVariable Long postId) {
        return ResponseEntity.ok(auditService.getPostReviewHistory(postId));
    }

    @GetMapping("/statistics/{reviewerId}")
    public ResponseEntity<Map<String, Object>> getReviewStatistics(@PathVariable Long reviewerId) {
        return ResponseEntity.ok(auditService.getReviewStatistics(reviewerId));
    }

    @PutMapping("/batch-review")
    public ResponseEntity<Void> batchReviewPosts(
            @RequestParam List<Long> postIds,
            @RequestParam PostStatus status,
            @RequestParam Long reviewerId,
            @RequestParam(required = false) String reviewComment) {
        boolean success = auditService.batchReviewPosts(postIds, status, reviewerId, reviewComment);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/rules")
    public ResponseEntity<Map<String, Object>> getReviewRules() {
        return ResponseEntity.ok(auditService.getReviewRules());
    }

    @PutMapping("/rules")
    public ResponseEntity<Void> updateReviewRules(@RequestBody Map<String, Object> rules) {
        boolean success = auditService.updateReviewRules(rules);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
} 