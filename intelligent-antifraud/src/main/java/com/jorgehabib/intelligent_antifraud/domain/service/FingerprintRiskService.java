package com.jorgehabib.intelligent_antifraud.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgehabib.intelligent_antifraud.application.service.CacheService;

@Service
public class FingerprintRiskService {
  @Autowired
  private CacheService cacheService;

  public int getFingerprintRiskScore(String fingerprint) {
    int accountsAssociated = cacheService.getAccountsCountByFingerprint(fingerprint);
    int ipsAssociated = cacheService.getUniqueIPsCountByFingerprint(fingerprint);
    int recentFrequency = cacheService.getFingerprintFrequencyLast30Min(fingerprint);
    boolean isBlacklisted = cacheService.isFingerprintBlacklisted(fingerprint);

    int fingerprintScore = 0;
    if (isBlacklisted) fingerprintScore += 40;
    fingerprintScore += Math.min(accountsAssociated * 10, 40);
    fingerprintScore += Math.min(ipsAssociated * 5, 20);
    fingerprintScore += Math.min(recentFrequency * 4, 40);
    return fingerprintScore;
}
}
