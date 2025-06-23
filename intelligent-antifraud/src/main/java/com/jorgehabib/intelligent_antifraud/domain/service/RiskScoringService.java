package com.jorgehabib.intelligent_antifraud.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgehabib.intelligent_antifraud.domain.model.Transaction;
import com.jorgehabib.intelligent_antifraud.application.service.CacheService;

@Service
public class RiskScoringService {
  @Autowired
  private CacheService cacheService;
  
  @Autowired
  private FingerprintRiskService fingerprintRiskService;
  
  @Autowired
  private TransactionAmountRiskService transactionAmountRiskService;

  public int calculateRiskScore(Transaction transaction) {
      int score = 0;

      if (cacheService.getRecentRisk(transaction.getIp()).isPresent()) {
        score += 40;
      }

      int transactionRiskScore = transactionAmountRiskService.calculateAmountRisk(transaction);
      score += transactionRiskScore;

      int fingerprintRiskScore = fingerprintRiskService.getFingerprintRiskScore(transaction.getFingerprint());
      score += fingerprintRiskScore;

      return Math.min(score, 100);
  }
}