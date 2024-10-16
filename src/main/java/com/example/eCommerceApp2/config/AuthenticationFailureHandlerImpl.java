package com.example.eCommerceApp2.config;

import com.example.eCommerceApp2.model.UserEntity;
import com.example.eCommerceApp2.repository.UserRepository;
import com.example.eCommerceApp2.service.UserService;
import com.example.eCommerceApp2.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username");
        UserEntity userDetails = userRepository.findByEmail(email);

        if (userDetails.getEnable()){
            if (userDetails.getAccountNonLocked()){
                if(userDetails.getFailedAttempt() <= AppConstant.ATTEMPT_TIME){
                    userService.increaseFailedAttempt(userDetails);
                } else{
                    userService.userAccountLock(userDetails);
                    exception = new LockedException("Your account is locked you can try again sometime later.");
                }
            } else {
                if (userService.unlockAccountTimeExpired(userDetails)){
                    exception = new LockedException("Your account is unlocked you can try now.");
                } else{
                    exception = new LockedException("Your account is Locked, please try after sometime.");
                }
            }
        } else {
            exception = new LockedException("Your account is inactive.");
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
