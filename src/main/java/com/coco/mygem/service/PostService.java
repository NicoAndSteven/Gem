package com.coco.mygem.service;

import com.coco.mygem.entity.Post;
import com.coco.mygem.entity.PostStatus;
import com.coco.mygem.entity.PostType;
import java.util.List;

public interface PostService {
    // 创建帖子
    Post createPost(Post post);
    
    // 更新帖子
    Post updatePost(Post post);
    
    // 获取帖子详情
    Post getPostById(Long postId);
    
    // 获取用户的帖子列表
    List<Post> getPostsByUserId(Long userId);
    
    // 获取特定类型的帖子列表
    List<Post> getPostsByType(PostType type);
    
    // 获取特定技术标签的帖子列表
    List<Post> getPostsByTechTag(String tag);
    
    // 更新帖子状态
    boolean updatePostStatus(Long postId, PostStatus status, Long reviewerId, 
                           String reviewComment);
    
    // 更新项目进度
    boolean updateProgress(Long postId, Integer progress);
    
    // 更新已筹金额
    boolean updateRaisedAmount(Long postId, Long amount);
    
    // 删除帖子
    boolean deletePost(Long postId);
    
    // 获取待审核的帖子列表
    List<Post> getPendingReviewPosts();
    
    // 获取推荐帖子列表
    List<Post> getRecommendedPosts(Long userId);

    List<Post> getTrendingPosts();
}