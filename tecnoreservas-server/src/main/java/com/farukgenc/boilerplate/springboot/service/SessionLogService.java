package com.farukgenc.boilerplate.springboot.service;

import com.farukgenc.boilerplate.springboot.model.SessionLog;
import com.farukgenc.boilerplate.springboot.model.User;
import com.farukgenc.boilerplate.springboot.repository.SessionLogRepository;
import com.farukgenc.boilerplate.springboot.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionLogService {

    @Autowired
    private SessionLogRepository sessionLogRepository;

    @Autowired
    private UserRepository userRepository;

    public void registerLogin(String username, HttpServletRequest request){
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }

        SessionLog sessionLog = new SessionLog();
        sessionLog.setUser(user);
        sessionLog.setLoginAt(LocalDateTime.now());
        sessionLog.setIpAddress(request.getRemoteAddr());

        sessionLogRepository.save(sessionLog);
    }

}
