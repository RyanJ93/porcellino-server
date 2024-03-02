package dev.enricosola.porcellino.support;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import dev.enricosola.porcellino.service.UserDetailsServiceImpl;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import dev.enricosola.porcellino.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import org.slf4j.Logger;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private static final String HEADER_NAME = "Authorization";
    private static final String HEADER_KEYWORD = "Bearer";

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    private String extractJWTFromRequest(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AuthTokenFilter.HEADER_NAME), jwt = null;
        if ( authorizationHeader != null && !authorizationHeader.isBlank() ){
            int index = authorizationHeader.indexOf(AuthTokenFilter.HEADER_KEYWORD);
            if ( index == 0 && authorizationHeader.length() > 7 ){
                jwt = authorizationHeader.substring(6).trim();
                if ( jwt.isBlank() ){
                    jwt = null;
                }
            }
        }
        return jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = this.extractJWTFromRequest(request);
            if ( jwt != null && this.jwtUtils.validateJwtToken(jwt) ){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(this.jwtUtils.getUserNameFromJwtToken(jwt));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(Exception ex){
            AuthTokenFilter.logger.error("Cannot set user authentication: {}", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    public AuthTokenFilter(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils){
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }
}
