package dev.enricosola.porcellino.config;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import dev.enricosola.porcellino.service.UserDetailsServiceImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import dev.enricosola.porcellino.support.AuthEntryPointJwt;
import dev.enricosola.porcellino.support.AuthTokenFilter;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private static final String[] PUBLIC_ROUTE_LIST = {
        "/api/v1/users/@me/resend-activation-token",
        "/api/v1/users/signup",
        "/api/v1/auth/**",
        "/api/test/**"
    };

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthTokenFilter authTokenFilter;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt authEntryPointJwt, AuthTokenFilter authTokenFilter){
        this.userDetailsService = userDetailsService;
        this.authEntryPointJwt = authEntryPointJwt;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(this.authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    for ( String publicRoute : WebSecurityConfig.PUBLIC_ROUTE_LIST ) {
                        auth.requestMatchers(publicRoute).permitAll();
                    }
                    auth.anyRequest().authenticated();
                });
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(this.authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
