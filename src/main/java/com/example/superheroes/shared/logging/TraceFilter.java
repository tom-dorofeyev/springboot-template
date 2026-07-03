package com.example.superheroes.shared.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TraceFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String MDC_TRACE_ID = "traceId";
    private static final String MDC_USER_ID = "userId";
    private static final String MDC_METHOD = "method";
    private static final String MDC_PATH = "path";
    private static final String MDC_STATUS_CODE = "statusCode";
    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String traceId = resolveTraceId(request);
        populateRequestMdc(request, traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            logAccessAndCleanup(request, response);
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (StringUtils.hasText(traceId)) {
            return traceId;
        }
        return UUID.randomUUID().toString();
    }

    private void populateRequestMdc(HttpServletRequest request, String traceId) {
        MDC.put(MDC_TRACE_ID, traceId);
        MDC.put(MDC_METHOD, request.getMethod());
        MDC.put(MDC_PATH, request.getRequestURI());
    }

    private void logAccessAndCleanup(HttpServletRequest request, HttpServletResponse response) {
        MDC.put(MDC_STATUS_CODE, String.valueOf(response.getStatus()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticatedNonAnonymous(authentication)) {
            MDC.put(MDC_USER_ID, authentication.getName());
        }

        log.info("{} {} {}", request.getMethod(), request.getRequestURI(), response.getStatus());

        MDC.clear();
    }

    private boolean isAuthenticatedNonAnonymous(Authentication authentication) {
        return authentication != null
            && authentication.isAuthenticated()
            && !ANONYMOUS_USER.equals(authentication.getName());
    }
}
