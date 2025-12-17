package com.example.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.model.UserDtls;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("username");
        UserDtls userDtls = userRepository.findByEmail(email);

        if (userDtls == null) {
            exception = new LockedException("Invalid username or user not found");
        } else if (Boolean.TRUE.equals(userDtls.getEnabled())) {
            if (Boolean.TRUE.equals(userDtls.getAccountNonLocked())) {
                if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                    userService.increaseFailedAttempt(userDtls);
                } else {
                    userService.userAccountLock(userDtls);
                    exception = new LockedException("Your account is locked - failed attempt 3");
                }
            } else {
                if (userService.unloackAccountTimeExpired(userDtls)) {
                    exception = new LockedException("Your account is unlocked - Please try to login");
                } else {
                    exception = new LockedException("Your account is locked - try after some time");
                }
            }
        } else {
            exception = new LockedException("Your account is inactive");
        }

        // ✅ Encode the message safely before appending
        String encodedMessage = URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8);
        setDefaultFailureUrl("/signin?error=" + encodedMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}