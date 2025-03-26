package com.coco.mygem.controller;

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
    public ResponseEntity<Investment> createInvestment(@RequestBody Investment investment) {
        return ResponseEntity.ok(investmentService.createInvestment(investment));
    }

    @GetMapping("/{investmentId}")
    public ResponseEntity<Investment> getInvestment(@PathVariable Long investmentId) {
        Investment investment = investmentService.getInvestmentById(investmentId);
        return investment != null ? ResponseEntity.ok(investment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Investment>> getUserInvestments(@PathVariable Long userId) {
        return ResponseEntity.ok(investmentService.getInvestmentsByUserId(userId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Investment>> getPostInvestments(@PathVariable Long postId) {
        return ResponseEntity.ok(investmentService.getInvestmentsByPostId(postId));
    }

    @PutMapping("/{investmentId}/status")
    public ResponseEntity<Void> updateInvestmentStatus(
            @PathVariable Long investmentId,
            @RequestParam InvestmentStatus status) {
        boolean success = investmentService.updateInvestmentStatus(investmentId, status);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{investmentId}/confirm")
    public ResponseEntity<Void> confirmInvestment(@PathVariable Long investmentId) {
        boolean success = investmentService.confirmInvestment(investmentId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{investmentId}/cancel")
    public ResponseEntity<Void> cancelInvestment(@PathVariable Long investmentId) {
        boolean success = investmentService.cancelInvestment(investmentId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/post/{postId}/total")
    public ResponseEntity<Long> getTotalInvestment(@PathVariable Long postId) {
        return ResponseEntity.ok(investmentService.getTotalInvestment(postId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasInvested(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        return ResponseEntity.ok(investmentService.hasInvested(userId, postId));
    }

    @GetMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<Investment> getUserInvestment(
            @PathVariable Long userId,
            @PathVariable Long postId) {
        Investment investment = investmentService.getUserInvestment(userId, postId);
        return investment != null ? ResponseEntity.ok(investment) : ResponseEntity.notFound().build();
    }
} 