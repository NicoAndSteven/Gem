package com.coco.mygem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco.mygem.entity.Post;
import com.coco.mygem.entity.PostStatus;
import com.coco.mygem.entity.PostType;
import com.coco.mygem.mapper.PostMapper;
import com.coco.mygem.service.PostService;
import com.coco.mygem.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public Post createPost(Post post) {
        try {
            // 设置初始状态,用数值代替
            post.setStatus(PostStatus.TEMP.getCode());
            post.setProgress(0);
            post.setRaisedAmount(0L);
            post.setInvestorCount(0);
            post.setCommentCount(0);
            
            // 设置创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            post.setCreateTime(now);
            post.setUpdateTime(now);
            
            // 插入数据
            postMapper.insert(post);
            LogUtil.info("创建帖子成功，帖子ID: {}", post.getPostId());
            return post;
        } catch (Exception e) {
            LogUtil.error("创建帖子失败", e);
            throw new RuntimeException("创建帖子失败", e);
        }
    }

    @Override
    @Transactional
    public Post updatePost(Post post) {
        try {
            // 设置更新时间
            post.setUpdateTime(LocalDateTime.now());
            
            // 更新数据
            postMapper.updateById(post);
            LogUtil.info("更新帖子成功，帖子ID: {}", post.getPostId());
            return post;
        } catch (Exception e) {
            LogUtil.error("更新帖子失败", e);
            throw new RuntimeException("更新帖子失败", e);
        }
    }

    @Override
    public Post getPostById(Long postId) {
        try {
            Post post = postMapper.selectById(postId);
            if (post == null) {
                LogUtil.warn("帖子不存在，帖子ID: {}", postId);
                return null;
            }
            return post;
        } catch (Exception e) {
            LogUtil.error("获取帖子失败", e);
            throw new RuntimeException("获取帖子失败", e);
        }
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        try {
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                  .orderByDesc("create_time");
            return postMapper.selectList(wrapper);
        } catch (Exception e) {
            LogUtil.error("获取用户帖子失败，用户ID: {}", userId, e);
            throw new RuntimeException("获取用户帖子失败", e);
        }
    }

    @Override
    public List<Post> getPostsByType(PostType type) {
        try {
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.eq("type", type.name())
                  .orderByDesc("create_time");
            return postMapper.selectList(wrapper);
        } catch (Exception e) {
            LogUtil.error("获取类型帖子失败，类型: {}", type, e);
            throw new RuntimeException("获取类型帖子失败", e);
        }
    }

    @Override
    public List<Post> getPostsByTechTag(String tag) {
        try {
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.like("tech_tags", tag)
                  .orderByDesc("create_time");
            return postMapper.selectList(wrapper);
        } catch (Exception e) {
            LogUtil.error("获取技术标签帖子失败，标签: {}", tag, e);
            throw new RuntimeException("获取技术标签帖子失败", e);
        }
    }

    @Override
    @Transactional
    public boolean updatePostStatus(Long postId, PostStatus status, Long reviewerId, String reviewComment) {
        try {
            Post post = new Post();
            post.setPostId(postId);
            post.setStatus(status.getCode());
            post.setReviewerId(reviewerId);
            post.setReviewComment(reviewComment);
            post.setReviewTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            post.setUpdateTime(LocalDateTime.now());
            
            postMapper.updateById(post);
            LogUtil.info("更新帖子状态成功，帖子ID: {}, 状态: {}", postId, status.getName());
        } catch (Exception e) {
            LogUtil.error("更新帖子状态失败，帖子ID: {}", postId, e);
            throw new RuntimeException("更新帖子状态失败", e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateProgress(Long postId, Integer progress) {
        try {
            Post post = new Post();
            post.setPostId(postId);
            post.setProgress(progress);
            post.setUpdateTime(LocalDateTime.now());
            
            postMapper.updateById(post);
            LogUtil.info("更新帖子进度成功，帖子ID: {}, 进度: {}", postId, progress);
        } catch (Exception e) {
            LogUtil.error("更新帖子进度失败，帖子ID: {}", postId, e);
            throw new RuntimeException("更新帖子进度失败", e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateRaisedAmount(Long postId, Long amount) {
        try {
            Post post = new Post();
            post.setPostId(postId);
            post.setRaisedAmount(amount);
            post.setUpdateTime(LocalDateTime.now());
            
            postMapper.updateById(post);
            LogUtil.info("更新帖子筹款金额成功，帖子ID: {}, 金额: {}", postId, amount);
        } catch (Exception e) {
            LogUtil.error("更新帖子筹款金额失败，帖子ID: {}", postId, e);
            throw new RuntimeException("更新帖子筹款金额失败", e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deletePost(Long postId) {
        try {
            postMapper.deleteById(postId);
            LogUtil.info("删除帖子成功，帖子ID: {}", postId);
        } catch (Exception e) {
            LogUtil.error("删除帖子失败，帖子ID: {}", postId, e);
            throw new RuntimeException("删除帖子失败", e);
        }
        return false;
    }

    @Override
    public List<Post> getPendingReviewPosts() {
        try {
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.eq("status", PostStatus.PUSH.getCode())
                  .orderByDesc("create_time");
            return postMapper.selectList(wrapper);
        } catch (Exception e) {
            LogUtil.error("获取待审核帖子失败", e);
            throw new RuntimeException("获取待审核帖子失败", e);
        }
    }

    @Override
    public List<Post> getRecommendedPosts(Long userId) {
        // TODO: 实现基于用户兴趣的推荐算法
        return getTrendingPosts();
    }

    @Override
    public List<Post> getTrendingPosts() {
        try {
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("investor_count")
                  .orderByDesc("comment_count")
                  .orderByDesc("create_time")
                  .last("LIMIT 10");
            return postMapper.selectList(wrapper);
        } catch (Exception e) {
            LogUtil.error("获取热门帖子失败", e);
            throw new RuntimeException("获取热门帖子失败", e);
        }
    }
} 