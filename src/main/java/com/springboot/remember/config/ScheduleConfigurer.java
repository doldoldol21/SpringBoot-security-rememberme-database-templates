package com.springboot.remember.config;

import com.springboot.remember.component.ExpiredTokenJpaRepositoryCleaner;
import com.springboot.remember.repository.PersistentLoginRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

// 서버에서 remember-me 토큰 만료시간을 관리해야할 때 써야하는 스케줄러
// PersistentLoginRepository에 findByLastUsedBefore를 쓰고있는데 현재 조건은 마지막 사용시간이 현재시간 - 만료시간 이면 삭제
// 만들어진 시간을 기준 잡으려면 model/PersistentLogin 에 createDate를 추가하여 사용할 것

// Configuration 어노테이션 주석처리하면 작동하지 않는다.
//@Configuration
@AllArgsConstructor
@Slf4j
public class ScheduleConfigurer {

    private final PersistentLoginRepository persistentLoginRepository;

    @Scheduled(fixedDelay = 10_000)
    public void cleanExpiredTokens() {
        log.debug("시작한다 스케줄러");
        // 토큰 유효시간 10초
        new Thread(ExpiredTokenJpaRepositoryCleaner.of(this.persistentLoginRepository, 10_000L)).start();
    }
}
