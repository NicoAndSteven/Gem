package com.coco.mygem.controller;

import com.coco.mygem.dto.ApiResponse;
import com.coco.mygem.entity.Investment;
import com.coco.mygem.entity.InvestmentStatus;
import com.coco.mygem.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createInvestment(@RequestBody Investment investment) {
        Investment created = investmentService.createInvestment(investment);
        return ResponseEntity.ok(ApiResponse.success("投资创建成功", created));
    }

    @GetMapping("/{investmentId}")
    public ResponseEntity<ApiResponse> getInvestment(@PathVariable Long investmentId) {
        Investment investment = investmentService.getInvestmentById(investmentId);
        return ResponseEntity.ok(ApiResponse.success(investment));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserInvestments(@PathVariable Long userId) {
        List<Investment> investments = investmentService.getInvestmentsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(investments));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> getPostInvestments(@PathVariable Long postId) {
        List<Investment> investments = investmentService.getInvestmentsByPostId(postId);
        return ResponseEntity.ok(ApiResponse.success(investments));
    }

    @PutMapping("/{investmentId}/status")
    public ResponseEntity<ApiResponse> updateInvestmentStatus(
            @PathVariable Long investmentId,
            @RequestParam InvestmentStatus status) {
        boolean success = investmentService.updateInvestmentStatus(investmentId, status);
        return ResponseEntity.ok(ApiResponse.success("投资状态更新为: " + status));
    }

    @PutMapping("/{investmentId}/confirm")
    public ResponseEntity<ApiResponse> confirmInvestment(@PathVariable Long investmentId) {
        boolean success = investmentService.confirmInvestment(investmentId);
        return ResponseEntity.ok(ApiResponse.success("投资已确认"));
    }

    @PutMapping("/{investmentId}/cancel")
    public ResponseEntity<ApiResponse> cancelInvestment(@PathVariable Long investmentId) {
        boolean success = investmentService.cancelInvestment(investmentId);
        return ResponseEntity.ok(ApiResponse.success("投资已取消"));
    }

    @GetMapping("/post/{postId}/total")
    public ResponseEntity<ApiResponse> getTotalInvestment(@PathVariable Long postId) {
        Long total = investmentService.getTotalInvestment(postId);
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse> hasInvested(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        boolean hasInvested = investmentService.hasInvested(userId, postId);
        return ResponseEntity.ok(ApiResponse.success(hasInvested));
    }

    @GetMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<ApiResponse> getUserInvestment(
            @PathVariable Long userId,
            @PathVariable Long postId) {
        Investment investment = investmentService.getUserInvestment(userId, postId);
        return ResponseEntity.ok(ApiResponse.success(investment));
    }
} 