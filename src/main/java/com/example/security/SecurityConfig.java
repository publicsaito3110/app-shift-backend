package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    /**
     * [Bean] PasswordEncoder
     *
     * PasswordEncoderをLibPasswordEncoderでBean定義
     *
     * @return LibPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }


    /**
     * [Bean] DaoAuthenticationProvider
     *
     * DaoAuthenticationProviderからuserDetailsServiceImplを呼び出すように指定
     * DaoAuthenticationProviderを呼び出すとuserDetailsServiceImplでログイン認可が行われる
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    /**
     * [Bean] SecurityFilterChain
     *
     * Spring Securityによる認証認可の設定
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                // 未認証を許容するURI
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 認証を必要とするURI
                .anyRequest().authenticated()
        ).cors(cors -> cors
                // CORSの有効化
                .configurationSource(corsConfigurationSource())
        ).csrf(csrf -> csrf
                // CSRFを無効にするURI
                .ignoringRequestMatchers("/api/**")
        ).addFilterBefore(new AuthorizeFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    /**
     * SecurityFilterChain
     *
     * CORSのOrigin設定
     * トークンヘッダーの追加とOrigin許可するサーバを登録
     *
     * @return CorsConfigurationSource
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addExposedHeader(SecurityConst.JWT_TOKEN_HEADER_REQUEST);
        corsConfiguration.addAllowedOrigin(SecurityConst.ALLOWED_ORIGIN_SERVER);
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsSource;
    }
}