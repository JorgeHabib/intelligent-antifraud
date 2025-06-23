package com.jorgehabib.intelligent_antifraud.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  private String transactionId;
  private String userId;
  private BigDecimal amount;
  private String ip;
  private String fingerprint;
  private LocalDateTime timestamp;

  private Integer riskScore;
  private Decision decision;

  public enum Decision {
      APPROVE,
      REVIEW,
      BLOCK
  }
}
