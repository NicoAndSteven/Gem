package com.coco.mygem.service;

import com.coco.mygem.entity.Post;
import java.util.List;
import java.util.Map;

public interface RecommendationService {
    // 获取用户推荐帖子列表
    List<Post> getRecommendedPosts(Long userId);
    
    // 获取基于技术标签的推荐帖子
    List<Post> getPostsByTechTags(List<String> tags);
    
    // 获取热门帖子列表
    List<Post> getHotPosts(int limit);
    
    // 获取最新帖子列表
    List<Post> getLatestPosts(int limit);
    
    // 获取相似帖子列表
    List<Post> getSimilarPosts(Long postId, int limit);
    
    // 获取个性化推荐
    List<Post> getPersonalizedRecommendations(Long userId, int limit);
    
    // 更新用户兴趣标签
    boolean updateUserInterests(Long userId, List<String> interests);
    
    // 获取用户兴趣标签
    List<String> getUserInterests(Long userId);
    
    // 获取推荐统计数据
    Map<String, Object> getRecommendationStats(Long userId);
    
    // 记录用户行为
    boolean recordUserBehavior(Long userId, Long postId, String behaviorType);

    List<Post> getTrendingPosts();
} 