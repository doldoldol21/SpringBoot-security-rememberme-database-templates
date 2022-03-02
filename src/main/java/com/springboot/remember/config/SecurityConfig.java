package com.springboot.remember.config;

import com.springboot.remember.repository.JpaPersistentTokenRepository;
import com.springboot.remember.repository.PersistentLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PersistentTokenRepository tokenRepository;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("test").password("{noop}test").authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .sameOrigin() // H2-Console에 접속해 영구 토큰을 확인하기 위한 설정
            .and()
            .authorizeRequests()
            .antMatchers("/h2-console/**")
            .permitAll() // H2-Console 접근 허용
            .anyRequest()
            .authenticated() // 나머지 인증 필요
            .and()
            .rememberMe()
            .key("key") // 토큰 암호화 키, 기본값은 무작위
            .userDetailsService(userDetailsService())
            .tokenRepository(tokenRepository)
            // .rememberMeParameter("remember-me") // 클라이언트에서 설정한 파라미터명과 맞춰야함 기본값은 remember-me
            //.tokenValiditySeconds(84000) //토큰 유효기간. 기본값은 14일이며 초단위(s) -1로 설정 할 경우 브라우저 종료와 함께 사라짐
            // .alwaysRemember(true) // Remember-Me 기능이 활성화 되지 않아도(체크박스에 체크하지 않아도, 혹은 체크박스가 아예 없더라도) 항상 적용
            // .rememberMeCookieName("remember-me")    //Remember-Me 응답 쿠키이름 기본값은 remember-me
            .and()
            .formLogin();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(final PersistentLoginRepository repository) {
        return new JpaPersistentTokenRepository(repository);
    }
    // @Bean
    // public PersistentTokenBasedRememberMeServices rememberMeServices(final PersistentTokenRepository repository) {
    //     PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
    //         "key",
    //         userDetailsService(),
    //         repository
    //     );
    //     services.setAlwaysRemember(true);
    //     services.setParameter("remember-me");
    //     return services;
    // }
}
