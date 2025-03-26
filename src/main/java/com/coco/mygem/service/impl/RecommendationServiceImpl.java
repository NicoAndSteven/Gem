package com.coco.mygem.service.impl;

import com.coco.mygem.entity.Post;
import com.coco.mygem.entity.UserBehavior;
import com.coco.mygem.entity.UserInterest;
import com.coco.mygem.mapper.PostMapper;
import com.coco.mygem.mapper.UserBehaviorMapper;
import com.coco.mygem.mapper.UserInterestMapper;
import com.coco.mygem.service.PostService;
import com.coco.mygem.service.RecommendationService;
import com.coco.mygem.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserInterestMapper userInterestMapper;

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Override
    public List<Post> getRecommendedPosts(Long userId) {
        try {
            // 获取用户兴趣标签
            List<UserInterest> userInterests = userInterestMapper.findByUserId(userId);
            if (userInterests.isEmpty()) {
                return getTrendingPosts();
            }

            // 获取所有帖子
            List<Post> allPosts = postMapper.selectList(null);
            
            // 计算每个帖子与用户兴趣的匹配度
            return allPosts.stream()
                .map(post -> {
                    double matchScore = calculateMatchScore(post, userInterests);
                    post.setScore(matchScore);
                    return post;
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(10)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LogUtil.error("获取推荐帖子失败", e);
            return getTrendingPosts();
        }
    }

    private double calculateMatchScore(Post post, List<UserInterest> userInterests) {
        // 解析帖子的技术标签
        Set<String> postTags = new HashSet<>(Arrays.asList(post.getTechTags().split(",")));
        
        // 计算标签匹配度
        double tagScore = userInterests.stream()
            .mapToDouble(interest -> {
                if (postTags.contains(interest.getTag())) {
                    return interest.getWeight();
                }
                return 0.0;
            })
            .sum();
        
        // 考虑时间衰减因子
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime postTime = LocalDateTime.ofEpochSecond(post.getCreateTime().toEpochSecond(ZoneOffset.UTC), 0, ZoneOffset.UTC);
        long daysDiff = java.time.Duration.between(postTime, now).toDays();
        double timeFactor = Math.max(0, 1 - (daysDiff / 30.0));
        
        return tagScore * timeFactor;
    }

    @Override
    public List<Post> getPostsByTechTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取所有标签对应的帖子
        Set<Post> posts = new HashSet<>();
        for (String tag : tags) {
            posts.addAll(postService.getPostsByTechTag(tag));
        }
        
        return new ArrayList<>(posts);
    }

    @Override
    public List<Post> getHotPosts(int limit) {
        try {
            // 获取所有帖子
            List<Post> posts = postMapper.selectList(null);
            
            // 计算每个帖子的热度分数
            return posts.stream()
                .map(post -> {
                    // 计算热度分数
                    double score = (post.getInvestorCount() * 0.4 + 
                                  post.getCommentCount() * 0.3 + 
                                  (post.getRaisedAmount() / (double)post.getBudget()) * 0.3);
                    
                    post.setScore(score);
                    return post;
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LogUtil.error("获取热门帖子失败", e);
            return List.of();
        }
    }

    @Override
    public List<Post> getLatestPosts(int limit) {
        return postMapper.selectList(null).stream()
                .sorted(Comparator.comparing(Post::getCreateTime).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getSimilarPosts(Long postId, int limit) {
        try {
            Post targetPost = postMapper.selectById(postId);
            if (targetPost == null) {
                return List.of();
            }

            // 获取所有其他帖子
            List<Post> otherPosts = postMapper.selectList(null).stream()
                .filter(post -> !post.getPostId().equals(postId))
                .collect(Collectors.toList());

            // 计算相似度分数
            return otherPosts.stream()
                .map(post -> {
                    double similarity = calculateSimilarity(targetPost, post);
                    post.setScore(similarity);
                    return post;
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LogUtil.error("获取相似帖子失败", e);
            return List.of();
        }
    }

    private double calculateSimilarity(Post post1, Post post2) {
        // 计算技术标签相似度
        Set<String> tags1 = new HashSet<>(Arrays.asList(post1.getTechTags().split(",")));
        Set<String> tags2 = new HashSet<>(Arrays.asList(post2.getTechTags().split(",")));
        
        Set<String> intersection = new HashSet<>(tags1);
        intersection.retainAll(tags2);
        Set<String> union = new HashSet<>(tags1);
        union.addAll(tags2);
        
        double tagSimilarity = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
        
        // 计算类型相似度
        double typeSimilarity = post1.getType().equals(post2.getType()) ? 1.0 : 0.0;
        
        // 综合评分
        return tagSimilarity * 0.7 + typeSimilarity * 0.3;
    }

    @Override
    public List<Post> getPersonalizedRecommendations(Long userId, int limit) {
        try {
            // 获取用户行为数据
            List<UserBehavior> behaviors = userBehaviorMapper.selectList(null).stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());

            // 获取用户兴趣标签
            List<UserInterest> interests = userInterestMapper.findByUserId(userId);

            // 获取所有帖子
            List<Post> allPosts = postMapper.selectList(null);

            // 计算个性化推荐分数
            return allPosts.stream()
                .map(post -> {
                    double score = calculatePersonalizedScore(post, behaviors, interests);
                    post.setScore(score);
                    return post;
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LogUtil.error("获取个性化推荐失败", e);
            return getTrendingPosts();
        }
    }

    private double calculatePersonalizedScore(Post post, List<UserBehavior> behaviors, List<UserInterest> interests) {
        double behaviorScore = 0.0;
        double interestScore = 0.0;

        // 计算行为分数
        for (UserBehavior behavior : behaviors) {
            if (behavior.getPostId().equals(post.getPostId())) {
                switch (behavior.getBehaviorType()) {
                    case "VIEW":
                        behaviorScore += 0.1;
                        break;
                    case "LIKE":
                        behaviorScore += 0.3;
                        break;
                    case "INVEST":
                        behaviorScore += 0.5;
                        break;
                    case "COMMENT":
                        behaviorScore += 0.2;
                        break;
                }
            }
        }

        // 计算兴趣标签分数
        Set<String> postTags = new HashSet<>(Arrays.asList(post.getTechTags().split(",")));
        interestScore = interests.stream()
            .mapToDouble(interest -> {
                if (postTags.contains(interest.getTag())) {
                    return interest.getWeight();
                }
                return 0.0;
            })
            .sum();

        // 综合评分
        return behaviorScore * 0.4 + interestScore * 0.6;
    }

    @Override
    @Transactional
    public boolean updateUserInterests(Long userId, List<String> interests) {
        try {
            // 删除用户现有兴趣标签
            userInterestMapper.delete(null);

            // 添加新的兴趣标签
            for (String tag : interests) {
                UserInterest interest = new UserInterest();
                interest.setUserId(userId);
                interest.setTag(tag);
                interest.setWeight(1.0); // 初始权重
                userInterestMapper.insert(interest);
            }

            return true;
        } catch (Exception e) {
            LogUtil.error("更新用户兴趣标签失败", e);
            return false;
        }
    }

    @Override
    public List<String> getUserInterests(Long userId) {
        try {
            return userInterestMapper.findByUserId(userId).stream()
                .map(UserInterest::getTag)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LogUtil.error("获取用户兴趣标签失败", e);
            return List.of();
        }
    }

    @Override
    public Map<String, Object> getRecommendationStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        try {
            // 获取用户行为统计数据
            int totalViews = userBehaviorMapper.countViews(userId);
            int totalInvestments = userBehaviorMapper.countInvestments(userId);
            
            // 计算点击率（浏览/推荐次数）
            int totalRecommendations = userBehaviorMapper.countByUserIdAndType(userId, "VIEW");
            double clickRate = totalRecommendations > 0 ? (double) totalViews / totalRecommendations : 0.0;
            
            // 计算转化率（投资/浏览次数）
            double conversionRate = totalViews > 0 ? (double) totalInvestments / totalViews : 0.0;
            
            stats.put("totalRecommendations", totalRecommendations);
            stats.put("clickRate", clickRate);
            stats.put("conversionRate", conversionRate);
            stats.put("totalViews", totalViews);
            stats.put("totalInvestments", totalInvestments);
        } catch (Exception e) {
            LogUtil.error("获取推荐统计数据失败", e);
        }
        return stats;
    }

    @Override
    @Transactional
    public boolean recordUserBehavior(Long userId, Long postId, String behaviorType) {
        try {
            UserBehavior behavior = new UserBehavior();
            behavior.setUserId(userId);
            behavior.setPostId(postId);
            behavior.setBehaviorType(behaviorType);
            behavior.setCreateTime(LocalDateTime.now());
            
            // 记录行为
            userBehaviorMapper.insert(behavior);
            
            // 更新用户兴趣标签权重
            if ("INVEST".equals(behaviorType) || "LIKE".equals(behaviorType)) {
                Post post = postMapper.selectById(postId);
                if (post != null) {
                    String[] tags = post.getTechTags().split(",");
                    for (String tag : tags) {
                        UserInterest interest = new UserInterest();
                        interest.setUserId(userId);
                        interest.setTag(tag);
                        interest.setWeight("INVEST".equals(behaviorType) ? 0.5 : 0.2);
                        userInterestMapper.insert(interest);
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            LogUtil.error("记录用户行为失败", e);
            return false;
        }
    }

    @Override
    public List<Post> getTrendingPosts() {
        try {
            // 获取所有帖子
            List<Post> posts = postMapper.selectList(null);
            
            // 计算每个帖子的热度分数
            return posts.stream()
                .map(post -> {
                    // 计算时间衰减因子（最近7天内的帖子获得更高分数）
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime postTime = LocalDateTime.ofEpochSecond(post.getCreateTime().toEpochSecond(ZoneOffset.UTC), 0, ZoneOffset.UTC);
                    long daysDiff = java.time.Duration.between(postTime, now).toDays();
                    double timeFactor = Math.max(0, 1 - (daysDiff / 7.0));
                    
                    // 计算热度分数
                    double score = (post.getInvestorCount() * 0.4 + 
                                  post.getCommentCount() * 0.3 + 
                                  (post.getRaisedAmount() / (double)post.getBudget()) * 0.3) * timeFactor;
                    
                    post.setScore(score);
                    return post;
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(10)
                .collect(Collectors.toList());
        } catch (Exception e) {
            LogUtil.error("获取热门项目失败", e);
            return List.of();
        }
    }
} 