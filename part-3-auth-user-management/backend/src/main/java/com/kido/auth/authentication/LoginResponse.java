package com.kido.auth.authentication;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
