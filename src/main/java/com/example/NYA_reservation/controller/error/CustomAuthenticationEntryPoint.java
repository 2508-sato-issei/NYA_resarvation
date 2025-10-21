package com.example.NYA_reservation.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

import static com.example.NYA_reservation.validation.ErrorMessage.E0033;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException exception) throws IOException {

        HttpSession session =  request.getSession();
        session.setAttribute("errorMessage", E0033);
        response.sendRedirect("/login");
    }
}
