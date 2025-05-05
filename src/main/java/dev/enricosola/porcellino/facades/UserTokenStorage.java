package dev.enricosola.porcellino.facades;

import dev.enricosola.porcellino.support.AuthTokenKeychain;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import java.util.Date;

public class UserTokenStorage {
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";

    /**
     * Attaches authentication tokens to the HTTP response as secure cookies.
     *
     * @param httpServletResponse the HTTP response to which the cookies will be added.
     * @param authTokenKeychain holds the refresh token and access token to be attached.
     */
    public static void attachToResponse(HttpServletResponse httpServletResponse, AuthTokenKeychain authTokenKeychain) {
        int refreshTokenMaxAge = (int)(authTokenKeychain.getRefreshToken().getExpiration().getTime() - new Date().getTime()) / 1000;
        httpServletResponse.addCookie(UserTokenStorage.makeTokenCookie(
                UserTokenStorage.REFRESH_TOKEN_COOKIE_NAME,
                authTokenKeychain.getRefreshToken().getToken(),
                refreshTokenMaxAge
        ));
        httpServletResponse.addCookie(UserTokenStorage.makeTokenCookie(
                UserTokenStorage.ACCESS_TOKEN_COOKIE_NAME,
                authTokenKeychain.getAccessToken().getToken(),
                null
        ));
    }

    /**
     * Retrieves the refresh token from the "refresh_token" cookie in the provided HTTP servlet request.
     *
     * @param httpServletRequest the HTTP servlet request from which to retrieve the "refresh_token" cookie.
     * @return the value of the "refresh_token" cookie if it exists, or null if the cookie is not found.
     */
    public static String getRefreshTokenFromRequest(HttpServletRequest httpServletRequest) {
        Cookie refreshTokenCookie = UserTokenStorage.findCookieByName(UserTokenStorage.REFRESH_TOKEN_COOKIE_NAME, httpServletRequest);
        return refreshTokenCookie != null ? refreshTokenCookie.getValue() : null;
    }

    /**
     * Retrieves the access token from the "access_token" cookie in the provided HTTP servlet request.
     *
     * @param httpServletRequest the HTTP servlet request from which to retrieve the "access_token" cookie.
     * @return the value of the "access_token" cookie if it exists, or null if the cookie is not found.
     */
    public static String getAccessTokenFromRequest(HttpServletRequest httpServletRequest) {
        Cookie accessTokenCookie = UserTokenStorage.findCookieByName(UserTokenStorage.ACCESS_TOKEN_COOKIE_NAME, httpServletRequest);
        return accessTokenCookie != null ? accessTokenCookie.getValue() : null;
    }

    /**
     * Removes authentication tokens from the HTTP response.
     *
     * @param httpServletResponse the HTTP response to which expired cookies will be added.
     */
    public static void dropFromResponse(HttpServletResponse httpServletResponse) {
        httpServletResponse.addCookie(UserTokenStorage.makeTokenCookie(UserTokenStorage.REFRESH_TOKEN_COOKIE_NAME, null, 0));
        httpServletResponse.addCookie(UserTokenStorage.makeTokenCookie(UserTokenStorage.ACCESS_TOKEN_COOKIE_NAME, null, 0));
    }

    /**
     * Creates a secure HTTP-only cookie with the provided name, value, and optional maximum age.
     *
     * @param name the name of the cookie.
     * @param token the value to be set for the cookie.
     * @param maxAge the maximum age of the cookie in seconds; if null, the cookie will not have a max age.
     * @return a Cookie object configured with the specified parameters and secure attributes.
     */
    private static Cookie makeTokenCookie(String name, String token, Integer maxAge) {
        Cookie cookie = new Cookie(name, token);
        if (maxAge != null){
            cookie.setMaxAge(maxAge);
        }
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Finds a cookie by its name from the provided HTTP servlet request.
     *
     * @param name the name of the cookie to search for.
     * @param httpServletRequest the HTTP servlet request from which to retrieve cookies.
     * @return the cookie with the specified name, or null if no such cookie is found.
     */
    private static Cookie findCookieByName(String name, HttpServletRequest httpServletRequest) {
        for ( Cookie cookie : httpServletRequest.getCookies() ){
            if ( cookie.getName().equals(name) ){
                return cookie;
            }
        }
        return null;
    }
}
