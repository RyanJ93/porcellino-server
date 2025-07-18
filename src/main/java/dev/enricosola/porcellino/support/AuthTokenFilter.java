package dev.enricosola.porcellino.support;

import dev.enricosola.porcellino.dto.UserTokenDTO;
import dev.enricosola.porcellino.facades.UserTokenStorage;
import dev.enricosola.porcellino.service.AccessTokenService;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String HEADER_NAME = "Authorization";
    private static final String HEADER_KEYWORD = "Bearer";

    private static final String[] TWO_FACTOR_AUTH_ROUTE_LIST = {
            "/api/v1/auth/2fa/challenge"
    };

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessTokenService accessTokenService;

    public AuthTokenFilter(AuthenticationEntryPoint authenticationEntryPoint, AccessTokenService accessTokenService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessTokenService = accessTokenService;
    }

    /**
     * Extracts the access token from the given HTTP servlet request.
     *
     * @param request the HTTP servlet request from which the access token should be extracted.
     * @return the access token as a String, or null if no valid token is found in the request.
     */
    private String extractAccessTokenFromRequest(HttpServletRequest request) {
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
        if ( jwt == null ){
            jwt = UserTokenStorage.getAccessTokenFromRequest(request);
        }
        return jwt;
    }

    /**
     * Determines if the HTTP request has an allowed scope based on its URI and the provided scopes.
     *
     * @param request the HTTP servlet request to be evaluated.
     * @param scopes an array of scope strings representing permitted access levels.
     * @return true if the request has an allowed scope, false otherwise.
     */
    private boolean hasAllowedScope(HttpServletRequest request, String[] scopes) {
        if ( List.of(AuthTokenFilter.TWO_FACTOR_AUTH_ROUTE_LIST).contains(request.getRequestURI()) ){
            return List.of(scopes).contains("2fa");
        }
        return List.of(scopes).contains("auth");
    }

    /**
     * Performs filtering of incoming HTTP requests to ensure that the access token is valid
     * and that the user has the necessary scope to access the requested resource.
     *
     * @param request the incoming HTTP servlet request.
     * @param response the HTTP servlet response.
     * @param filterChain the filter chain.
     * @throws ServletException if a servlet-specific error occurs during processing.
     * @throws IOException if an I/O error occurs during processing.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = this.extractAccessTokenFromRequest(request);
        if ( accessToken != null && !accessToken.isBlank() ){
            UserTokenDTO userTokenDTO = this.accessTokenService.unpack(accessToken);
            if ( !this.hasAllowedScope(request, userTokenDTO.getScopes()) ){
                this.authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("Invalid token scope."));
                return;
            }
            UserDetails userDetails = new AuthenticatedUserDetails(userTokenDTO.getUser());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
