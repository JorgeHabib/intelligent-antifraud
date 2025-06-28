package com.jorgehabib.intelligent_antifraud.domain.service;

import org.springframework.stereotype.Service;

import com.jorgehabib.intelligent_antifraud.domain.model.Transaction;

@Service
public class DecisionService {
  public Transaction.Decision makeDecision(Transaction transaction, int score) {
    if (score <= 30) return Transaction.Decision.APPROVE;
    else if (score <= 70) return Transaction.Decision.REVIEW;
    else return Transaction.Decision.BLOCK;
  }
}
