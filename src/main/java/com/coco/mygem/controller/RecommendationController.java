package com.coco.mygem.controller;

import com.coco.mygem.entity.Post;
import com.coco.mygem.service.RecommendationService;
import com.coco.mygem.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/personal")
    public ResponseEntity<List<Post>> getPersonalRecommendations(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long userId = Long.parseLong(userDetails.getUsername());
            //获取10个推荐帖子
            List<Post> recommendations = recommendationService.getPersonalizedRecommendations(userId, 10);
            LogUtil.businessInfo("用户 " + userId + " 获取个性化推荐成功");
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            LogUtil.businessError("获取个性化推荐失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Post>> getPopularPosts() {
        try {
            List<Post> popularPosts = recommendationService.getLatestPosts(10);
            LogUtil.businessInfo("获取热门帖子成功");
            return ResponseEntity.ok(popularPosts);
        } catch (Exception e) {
            LogUtil.businessError("获取热门帖子失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/similar/{postId}")
    public ResponseEntity<List<Post>> getSimilarPosts(@PathVariable Long postId) {
        try {
            //获取10条相似贴
            List<Post> similarPosts = recommendationService.getSimilarPosts(postId,10);
            LogUtil.businessInfo("获取帖子 " + postId + " 的相似帖子成功");
            return ResponseEntity.ok(similarPosts);
        } catch (Exception e) {
            LogUtil.businessError("获取相似帖子失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<List<Post>> getTrendingPosts() {
        try {
            List<Post> trendingPosts = recommendationService.getTrendingPosts();
            LogUtil.businessInfo("获取趋势帖子成功");
            return ResponseEntity.ok(trendingPosts);
        } catch (Exception e) {
            LogUtil.businessError("获取趋势帖子失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 