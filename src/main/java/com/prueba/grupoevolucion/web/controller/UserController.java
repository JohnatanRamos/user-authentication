package com.prueba.grupoevolucion.web.controller;

import com.prueba.grupoevolucion.persistence.entity.UserEntity;
import com.prueba.grupoevolucion.service.UserService;
import com.prueba.grupoevolucion.service.dto.PasswordDto;
import com.prueba.grupoevolucion.web.config.JwtUtil;
import com.prueba.grupoevolucion.web.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usarios", description = "Control de usuarios")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear usuario")
    public ResponseEntity<Object> create(@RequestBody UserEntity user) {
        try {
            return ResponseHandler.generateResponse("Creado", HttpStatus.OK, this.userService.saveUser(user));
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<Object> update(@RequestBody UserEntity user) {
        try {
            return ResponseHandler.generateResponse("Modificado", HttpStatus.OK, this.userService.updateUser(user));
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/getProfile")
    @Operation(summary = "Obtener un usuario segun el subject del token")
    public ResponseEntity<Object> getUSer(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String jwt = authHeader.split(" ")[1].trim();
            String email = this.jwtUtil.getEmail(jwt);
            return ResponseHandler.generateResponse("User", HttpStatus.OK, this.userService.getUserByEmail(email));

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PutMapping("/changePassword/{id}")
    @Operation(summary = "Actualizar contraseña")
    public ResponseEntity<Object> changePassword(@RequestBody PasswordDto passwordDto, @PathVariable Long id) {
        try {
            return ResponseHandler.generateResponse(
                    "Contraseña modificada",
                    HttpStatus.OK,
                    this.userService.changePassword
                            (passwordDto.getNewPassword(), passwordDto.getOldPassword(), id));

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

}
