package com.prueba.grupoevolucion.web.controller;

import com.prueba.grupoevolucion.service.dto.LoginDto;
import com.prueba.grupoevolucion.web.config.JwtUtil;
import com.prueba.grupoevolucion.web.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            Authentication authentication = this.authenticationManager.authenticate(login);

            String jwt = this.jwtUtil.createToken(loginDto.getEmail());
            return ResponseHandler.generateResponse("Exitoso", HttpStatus.OK, jwt);

        } catch (AuthenticationException e) {
            return ResponseHandler.generateResponse("Credenciales incorrectas", HttpStatus.UNAUTHORIZED, null);
        }
    }
}
