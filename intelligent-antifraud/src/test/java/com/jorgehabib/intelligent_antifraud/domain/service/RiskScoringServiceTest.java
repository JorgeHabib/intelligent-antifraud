package com.jorgehabib.intelligent_antifraud.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jorgehabib.intelligent_antifraud.application.service.CacheService;
import com.jorgehabib.intelligent_antifraud.domain.model.Transaction;

@ExtendWith(MockitoExtension.class)
class RiskScoringServiceTest {

    @Mock
    private CacheService cacheService;

    @Mock
    private FingerprintRiskService fingerprintRiskService;

    @Mock
    private TransactionAmountRiskService transactionAmountRiskService;

    @InjectMocks
    private RiskScoringService riskScoringService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setTransactionId("txn-1");
        transaction.setUserId("user-1");
        transaction.setAmount(new BigDecimal("20.00"));
        transaction.setIp("1.2.3.4");
        transaction.setFingerprint("fp-123");
    }

    @Test
    void testTudoNormal_BaixaSuspeita() {
        // IP não tem risco
        when(cacheService.getRecentRisk("1.2.3.4")).thenReturn(Optional.empty());
        // Valor da transação normal
        when(transactionAmountRiskService.calculateAmountRisk(transaction)).thenReturn(0);
        // Fingerprint pouco associado
        when(fingerprintRiskService.getFingerprintRiskScore("fp-123")).thenReturn(10);

        int result = riskScoringService.calculateRiskScore(transaction);

        assertEquals(10, result); // Apenas 10 do fingerprint
    }

    @Test
    void testApenasIpSuspeito() {
        // IP tem risco
        when(cacheService.getRecentRisk("1.2.3.4")).thenReturn(Optional.of("qualquer coisa"));
        // Valor da transação normal
        when(transactionAmountRiskService.calculateAmountRisk(transaction)).thenReturn(0);
        // Fingerprint pouco associado
        when(fingerprintRiskService.getFingerprintRiskScore("fp-123")).thenReturn(10);

        int result = riskScoringService.calculateRiskScore(transaction);

        assertEquals(50, result); // 40 (IP) + 10 (fingerprint)
    }

    @Test
    void testFingerprintBlacklisted_MuitosIPsContas_FreqAlta() {
        // IP não tem risco
        when(cacheService.getRecentRisk("1.2.3.4")).thenReturn(Optional.empty());
        // Valor da transação normal
        when(transactionAmountRiskService.calculateAmountRisk(transaction)).thenReturn(0);
        // Fingerprint: blacklistado + muitos IPs + contas + frequência alta
        // 40 (blacklist) + 40 (contas, cap 40) + 20 (IPs, cap 20) + 40 (freq, cap 40) = 140 (limita 100)
        when(fingerprintRiskService.getFingerprintRiskScore("fp-123")).thenReturn(140);

        int result = riskScoringService.calculateRiskScore(transaction);

        assertEquals(100, result); // Deve limitar a 100
    }

    @Test
    void testValorTransacaoMuitoAlto() {
        // IP não tem risco
        when(cacheService.getRecentRisk("1.2.3.4")).thenReturn(Optional.empty());
        // Valor da transação muito acima da média (score = 20)
        when(transactionAmountRiskService.calculateAmountRisk(transaction)).thenReturn(20);
        // Fingerprint pouco associado
        when(fingerprintRiskService.getFingerprintRiskScore("fp-123")).thenReturn(10);

        int result = riskScoringService.calculateRiskScore(transaction);

        assertEquals(30, result); // 20 (amount) + 10 (fingerprint)
    }

    @Test
    void testSoFingerprintFrequenciaAlta() {
        // IP não tem risco
        when(cacheService.getRecentRisk("1.2.3.4")).thenReturn(Optional.empty());
        // Valor da transação normal
        when(transactionAmountRiskService.calculateAmountRisk(transaction)).thenReturn(0);
        // Fingerprint apenas frequência alta
        when(fingerprintRiskService.getFingerprintRiskScore("fp-123")).thenReturn(40);

        int result = riskScoringService.calculateRiskScore(transaction);

        assertEquals(40, result); // 40 do fingerprint (frequência alta)
    }

    @Test
    void testSoMuitasContasEIPsFingerprint() {
        // IP não tem risco
        when(cacheService.getRecentRisk("1.2.3.4")).thenReturn(Optional.empty());
        // Valor da transação normal
        when(transactionAmountRiskService.calculateAmountRisk(transaction)).thenReturn(0);
        // Fingerprint: 40 (contas) + 20 (IPs) = 60
        when(fingerprintRiskService.getFingerprintRiskScore("fp-123")).thenReturn(60);

        int result = riskScoringService.calculateRiskScore(transaction);

        assertEquals(60, result); // 60 do fingerprint
    }

    @Test
    void testNenhumaCondicao() {
        // IP não tem risco
        when(cacheService.getRecentRisk("1.2.3.4")).thenReturn(Optional.empty());
        // Valor da transação normal
        when(transactionAmountRiskService.calculateAmountRisk(transaction)).thenReturn(0);
        // Fingerprint sem nada suspeito
        when(fingerprintRiskService.getFingerprintRiskScore("fp-123")).thenReturn(0);

        int result = riskScoringService.calculateRiskScore(transaction);

        assertEquals(0, result); // Score mínimo
    }
}
