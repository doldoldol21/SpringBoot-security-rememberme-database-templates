package com.springboot.remember.repository;

import com.springboot.remember.model.PersistentLogin;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

// PersistentTokenRepository 에서 요구하는 4개의 메서드 오버라이드
@AllArgsConstructor
@Slf4j
public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final PersistentLoginRepository repository;

    // 새로운 remember-me 쿠키를 발급할 때 담을 토큰을 생성하기 위한 메서드
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        repository.deleteAllByUsername(token.getUsername());
        repository.save(PersistentLogin.from(token));
    }

    // 토큰 갱신(변경)
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        repository
            .findBySeries(series)
            .ifPresent(persistentLogin -> {
                persistentLogin.updateToken(tokenValue, lastUsed);
                repository.save(persistentLogin);
            });
    }

    // 쿠키 인증
    // 클라이언트가 보내온 쿠키에 담긴 시리즈로 DB 검색
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        log.info("seriesId: {}", seriesId);

        return repository
            .findBySeries(seriesId)
            .map(persistentLogin ->
                new PersistentRememberMeToken(
                    persistentLogin.getUsername(),
                    persistentLogin.getSeries(),
                    persistentLogin.getToken(),
                    persistentLogin.getLastUsed()
                )
            )
            .orElse(null);
        //.orElseThrow(IllegalArgumentException::new);
    }

    // 세션이 종료될 때 DB에서 토큰 제거
    @Override
    public void removeUserTokens(String username) {
        repository.deleteAllInBatch(repository.findByUsername(username));
    }
}
