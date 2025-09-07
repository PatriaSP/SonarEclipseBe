package com.patria.apps.controller;

import com.patria.apps.helper.JWTHelperService;
import com.patria.apps.entity.Users;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.repository.BlackListTokenRepository;
import com.patria.apps.response.JWTResponse;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.users.request.UserCreateRequest;
import com.patria.apps.users.request.UserLoginRequest;
import com.patria.apps.users.service.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
@Tag(name = "Auth Controller")
@RequiredArgsConstructor
public class AuthController {

    private final UsersService usersService;
    private final JWTHelperService jWTHelperService;

    private final BlackListTokenRepository blackListTokenRepository;

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public JWTResponse login(@RequestBody UserLoginRequest request) {
        return usersService.login(request);
    }

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse register(
            HttpServletRequest servletRequest,
            @RequestBody UserCreateRequest request
    ) {
        return usersService.create(servletRequest, request);
    }
    
    @GetMapping(
            path = "/logout"
    )
    public ReadResponse<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.replace("Bearer", "");
        jWTHelperService.killToken(token);
        return ReadResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
    }

    @GetMapping(
            path = "/check-session"
    )
    public ReadResponse<String> checkSession(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.replace("Bearer", "").trim();
        boolean checkBlackListToken = blackListTokenRepository.findByTokenAndDeletedAtIsNull(token.trim()).isPresent();
        if (checkBlackListToken) {
            throw new GeneralException(HttpStatus.UNAUTHORIZED, "Not Authorized");
        }

        Optional<Users> user = jWTHelperService.getUserByToken(token, request);

        if (user == null || user.isEmpty()) {
            throw new GeneralException(HttpStatus.UNAUTHORIZED, "Not Authorized");
        }

        return ReadResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Active")
                .build();
    }

}
