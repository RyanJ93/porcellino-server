package dev.enricosola.porcellino.interceptor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import dev.enricosola.porcellino.support.AuthenticatedUserDetails;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import dev.enricosola.porcellino.service.PortfolioService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.core.Authentication;
import dev.enricosola.porcellino.service.UserService;
import dev.enricosola.porcellino.entity.Portfolio;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import dev.enricosola.porcellino.entity.User;
import lombok.extern.slf4j.Slf4j;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class PortfolioOwnershipCheckInterceptor implements HandlerInterceptor {
    private final PortfolioService portfolioService;
    private final UserService userService;

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUserDetails authenticatedUserDetails = (AuthenticatedUserDetails)authentication.getPrincipal();
        return this.userService.findByEmail(authenticatedUserDetails.getUsername());
    }

    private Portfolio extractPortfolio(HttpServletRequest request){
        Pattern pattern = Pattern.compile("/api/portfolio/([0-9]).+");
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if ( matcher.matches() && !matcher.group(1).isBlank() ){
            Portfolio portfolio = this.portfolioService.getById(Integer.parseInt(matcher.group(1)));
            if ( portfolio == null ){
                throw new ResponseStatusException(NOT_FOUND, "No such portfolio found.");
            }
            return portfolio;
        }
        return null;
    }

    private void checkPortfolioOwnership(HttpServletRequest request){
        Portfolio portfolio = this.extractPortfolio(request);
        if ( portfolio != null ){
            User user = this.getAuthenticatedUser();
            if ( portfolio.getUser().getId() != user.getId() ){
                PortfolioOwnershipCheckInterceptor.log.warn("Unauthorized access attempt by user ID {} to portfolio {}.", user.getId(), portfolio.getId());
                throw new ResponseStatusException(FORBIDDEN, "Portfolio access denied.");
            }
        }
    }

    public PortfolioOwnershipCheckInterceptor(PortfolioService portfolioService, UserService userService){
        this.portfolioService = portfolioService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if ( request.getRequestURI().indexOf("/api/portfolio/") == 0 ){
            this.checkPortfolioOwnership(request);
        }
        return true;
    }
}
