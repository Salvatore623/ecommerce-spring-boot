package com.sublime.ecommerce.security.auth;

import lombok.Builder;

@Builder
public record AuthResponse(String token) {
}
