package com.fiserv.payment.connector.log;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Integer.MIN_VALUE) // Ensure this runs before Spring DispatcherServlet initializes
public class DynamicPortFilter implements Filter {

    private static final ThreadLocal<String> currentRequestPort = new ThreadLocal<>();

    public static String getCurrentRequestPort() {
        return currentRequestPort.get();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            // Automatically captures the network port the request landed on (e.g., 8081)
            int port = request.getLocalPort();
            currentRequestPort.set(String.valueOf(port));
        }

        try {
            chain.doFilter(request, response);
        } finally {
            currentRequestPort.remove(); // Clear thread reference to avoid memory leaks
        }
    }
}
