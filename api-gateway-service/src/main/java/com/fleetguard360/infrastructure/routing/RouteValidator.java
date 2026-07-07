package com.fleetguard360.infrastructure.routing;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/auth/**"
    );

    public final Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();
        boolean isOpen = OPEN_API_ENDPOINTS.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
        return !isOpen;
    };
}
