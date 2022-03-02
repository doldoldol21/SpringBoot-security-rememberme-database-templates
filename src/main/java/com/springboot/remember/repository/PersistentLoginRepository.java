package com.springboot.remember.repository;

import com.springboot.remember.model.PersistentLogin;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {
    Optional<PersistentLogin> findBySeries(final String series);
    List<PersistentLogin> findByUsername(final String username);
    Iterable<PersistentLogin> findByLastUsedBefore(final Date lastUsed);

    @Transactional
    void deleteAllByUsername(final String username);
}
