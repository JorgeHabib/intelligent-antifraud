package com.jorgehabib.intelligent_antifraud.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jorgehabib.intelligent_antifraud.domain.model.TenantConfig;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Optional<String> getRecentRisk(String ip) {
        return Optional.ofNullable((String) redisTemplate.opsForValue().get("risk:ip:" + ip));
    }

    public int getAccountsCountByFingerprint(String fingerprint) {
        return Optional.ofNullable(redisTemplate.opsForSet().size("fingerprint:accounts:" + fingerprint)).orElse(0L).intValue();
    }

    public int getUniqueIPsCountByFingerprint(String fingerprint) {
        return Optional.ofNullable(redisTemplate.opsForSet().size("fingerprint:ips:" + fingerprint)).orElse(0L).intValue();
    }

    public int getFingerprintFrequencyLast30Min(String fingerprint) {
        return Optional.ofNullable((Integer) redisTemplate.opsForValue().get("fingerprint:freq:" + fingerprint)).orElse(0);
    }

    public boolean isFingerprintBlacklisted(String fingerprint) {
        return redisTemplate.opsForSet().isMember("fingerprint:blacklist", fingerprint);
    }

    public void updateFingerprintFrequency(String fingerprint) {
        redisTemplate.opsForValue().increment("fingerprint:freq:" + fingerprint);
        redisTemplate.expire("fingerprint:freq:" + fingerprint, Duration.ofMinutes(30));
    }

    // Simulação, implementar conforme persistência real do histórico!
    public BigDecimal getAverageTransactionAmountByUser(String userId) { return BigDecimal.ZERO; }
    public BigDecimal getStdDevTransactionAmountByUser(String userId) { return BigDecimal.ZERO; }
    public TenantConfig getTenantConfig(String tenantId) throws Exception { return null; }
}
