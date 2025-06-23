package com.jorgehabib.intelligent_antifraud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
  private Transaction.Decision decision;
  private Integer riskScore;
}
