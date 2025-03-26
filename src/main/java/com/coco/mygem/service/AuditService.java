package com.coco.mygem.service;

import com.coco.mygem.entity.Post;
import com.coco.mygem.entity.PostStatus;
import java.util.List;
import java.util.Map;

public interface AuditService {
    // 提交帖子审核
    boolean submitForReview(Long postId);
    
    // 审核帖子
    boolean reviewPost(Long postId, PostStatus status, Long reviewerId, String reviewComment);
    
    // 获取待审核的帖子列表
    List<Post> getPendingReviewPosts();
    
    // 获取已审核的帖子列表
    List<Post> getReviewedPosts(Long reviewerId);
    
    // 获取帖子的审核历史
    List<Map<String, Object>> getPostReviewHistory(Long postId);
    
    // 获取审核统计数据
    Map<String, Object> getReviewStatistics(Long reviewerId);
    
    // 批量审核帖子
    boolean batchReviewPosts(List<Long> postIds, PostStatus status, Long reviewerId, String reviewComment);
    
    // 获取审核规则
    Map<String, Object> getReviewRules();
    
    // 更新审核规则
    boolean updateReviewRules(Map<String, Object> rules);
} 