package com.springboot.remember.component;

import com.springboot.remember.repository.PersistentLoginRepository;
import java.util.Date;
import java.util.Objects;

public class ExpiredTokenJpaRepositoryCleaner implements Runnable {

    private final PersistentLoginRepository repository;
    private final long tokenValidityInMs;

    private ExpiredTokenJpaRepositoryCleaner(final PersistentLoginRepository repository, final long tokenValidityInMs) {
        if (Objects.isNull(repository)) {
            throw new IllegalArgumentException("PersistentTokenRepository cannot be null.");
        }
        if (tokenValidityInMs < 1) {
            throw new IllegalArgumentException("tokenValidityInMs must be greater then 0. Got " + tokenValidityInMs);
        }
        this.repository = repository;
        this.tokenValidityInMs = tokenValidityInMs;
    }

    // 팩토리 메서드
    public static ExpiredTokenJpaRepositoryCleaner of(
        final PersistentLoginRepository repository,
        final long tokenValidityInMs
    ) {
        return new ExpiredTokenJpaRepositoryCleaner(repository, tokenValidityInMs);
    }

    @Override
    public void run() {
        final long expiredInMs = System.currentTimeMillis() - this.tokenValidityInMs;
        repository.deleteAllInBatch(repository.findByLastUsedBefore(new Date(expiredInMs)));
    }
}
