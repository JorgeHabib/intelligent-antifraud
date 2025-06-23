package com.jorgehabib.intelligent_antifraud.domain.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgehabib.intelligent_antifraud.application.service.CacheService;
import com.jorgehabib.intelligent_antifraud.domain.model.Transaction;

@Service
public class TransactionAmountRiskService {
  @Autowired
  private CacheService cacheService;

  public int calculateAmountRisk(Transaction transaction) {
    BigDecimal avgAmount = cacheService.getAverageTransactionAmountByUser(transaction.getUserId());
    BigDecimal stdDevAmount = cacheService.getStdDevTransactionAmountByUser(transaction.getUserId());

    if (avgAmount == null || stdDevAmount == null) return 0;

    if (transaction.getAmount().compareTo(avgAmount.add(stdDevAmount.multiply(new BigDecimal("2")))) > 0) {
        return 20;
    }

    if (transaction.getAmount().compareTo(avgAmount) > 0) {
        return 10;
    }

    return 0;
}
}
