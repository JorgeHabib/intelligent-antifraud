package com.jorgehabib.intelligent_antifraud.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jorgehabib.intelligent_antifraud.domain.model.Transaction;
import com.jorgehabib.intelligent_antifraud.domain.model.TransactionResponse;
import com.jorgehabib.intelligent_antifraud.domain.service.DecisionService;
import com.jorgehabib.intelligent_antifraud.domain.service.RiskScoringService;
import com.jorgehabib.intelligent_antifraud.infrastructure.messaging.TransactionEventPublisher;

@RestController
public class AntifraudController {

  @Autowired
  private RiskScoringService riskScoringService;
  @Autowired
  private DecisionService decisionService;
  @Autowired
  private TransactionEventPublisher transactionEventPublisher;

  @PostMapping("/evaluate/{tenantId}")
  public ResponseEntity<TransactionResponse> evaluateTransaction(
      @PathVariable("tenantId") String tenantId,
      @RequestBody Transaction transactionDTO) {

      int score = riskScoringService.calculateRiskScore(transactionDTO);
      Transaction.Decision decision = decisionService.makeDecision(transactionDTO, score);

      transactionDTO.setRiskScore(score);
      transactionDTO.setDecision(decision);

      transactionEventPublisher.publishTransactionEvent(tenantId, transactionDTO);

      TransactionResponse response = new TransactionResponse(decision, score);
      return ResponseEntity.ok(response);
  }
}
